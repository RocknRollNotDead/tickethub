#!/usr/bin/env bash
#
# Нагрузочный тест race condition для POST /events/{id}/book
#
# Что делает:
#   1. Создаёт тестового пользователя.
#   2. Создаёт событие с ограниченным числом мест (SEATS).
#   3. Одновременно запускает CONCURRENCY параллельных запросов на бронирование.
#   4. Сравнивает число успешных броней с количеством мест — если успешных
#      броней больше, чем мест, race condition подтверждена.
#
# Требования: bash, curl. Опционально jq (для красивого парсинга JSON,
# без него скрипт использует grep/sed).
#
# Запуск:
#   chmod +x race_condition_test.sh
#   ./race_condition_test.sh
#   SEATS=5 CONCURRENCY=100 ./race_condition_test.sh

set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
SEATS="${SEATS:-10}"
CONCURRENCY="${CONCURRENCY:-50}"
TMP_DIR="$(mktemp -d)"

cleanup() { rm -rf "$TMP_DIR"; }
trap cleanup EXIT

has_jq() { command -v jq >/dev/null 2>&1; }

echo "== Race condition тест =="
echo "BASE_URL=$BASE_URL  SEATS=$SEATS  CONCURRENCY=$CONCURRENCY"
echo

# --- 1. Создаём пользователя ---------------------------------------------
echo "-> Создаю тестового пользователя..."
USER_RESPONSE=$(curl -s -X POST "$BASE_URL/users" -d "name=LoadTestUser")

if has_jq; then
  USER_ID=$(echo "$USER_RESPONSE" | jq -r '.id')
else
  USER_ID=$(echo "$USER_RESPONSE" | grep -oE '"id":[0-9]+' | head -1 | grep -oE '[0-9]+')
fi

if [[ -z "${USER_ID:-}" || "$USER_ID" == "null" ]]; then
  echo "Не удалось создать пользователя. Ответ сервера:"
  echo "$USER_RESPONSE"
  exit 1
fi
echo "   userId=$USER_ID"

# --- 2. Создаём событие -----------------------------------------------------
echo "-> Создаю событие на $SEATS мест..."
EVENT_DATE=$(date -u +"%Y-%m-%dT%H:%M:%SZ" -d "+7 days" 2>/dev/null || date -u -v+7d +"%Y-%m-%dT%H:%M:%SZ")

curl -s -X POST "$BASE_URL/events" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Load Test Event\",\"date\":\"$EVENT_DATE\",\"totalSeats\":$SEATS,\"availableSeats\":$SEATS}" \
  > /dev/null

echo "-> Ищу id только что созданного события..."
EVENTS_RESPONSE=$(curl -s "$BASE_URL/events")

if has_jq; then
  EVENT_ID=$(echo "$EVENTS_RESPONSE" | jq -r '[.[] | select(.name=="Load Test Event")] | last | .id')
else
  # берём последний встретившийся id перед последним "Load Test Event" — грубый, но рабочий вариант без jq
  EVENT_ID=$(echo "$EVENTS_RESPONSE" | grep -oE '"id":[0-9]+,"name":"Load Test Event"' | tail -1 | grep -oE '[0-9]+' | head -1)
fi

if [[ -z "${EVENT_ID:-}" || "$EVENT_ID" == "null" ]]; then
  echo "Не удалось найти созданное событие. Ответ сервера:"
  echo "$EVENTS_RESPONSE"
  exit 1
fi
echo "   eventId=$EVENT_ID"
echo

# --- 3. Параллельные брони ---------------------------------------------------
echo "-> Запускаю $CONCURRENCY параллельных броней на $SEATS мест..."

for i in $(seq 1 "$CONCURRENCY"); do
  (
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
      "$BASE_URL/events/$EVENT_ID/book?userId=$USER_ID")
    echo "$HTTP_CODE" > "$TMP_DIR/result_$i.txt"
  ) &
done

wait
echo "   все запросы завершены"
echo

# --- 4. Считаем результат -----------------------------------------------------
SUCCESS_COUNT=$(grep -lE '^(200|201|204)$' "$TMP_DIR"/result_*.txt | wc -l | tr -d ' ')
FAIL_COUNT=$((CONCURRENCY - SUCCESS_COUNT))

FINAL_EVENT=$(curl -s "$BASE_URL/events" | { has_jq && jq -c ".[] | select(.id==$EVENT_ID)" || cat; })

if has_jq; then
  FINAL_SEATS=$(echo "$FINAL_EVENT" | jq -r '.availableSeats')
else
  FINAL_SEATS=$(echo "$FINAL_EVENT" | grep -oE "\"id\":$EVENT_ID[^}]*" | grep -oE '"availableSeats":[0-9-]+' | grep -oE '[0-9-]+$')
fi

echo "== Результат =="
echo "Успешных броней:      $SUCCESS_COUNT"
echo "Отклонённых броней:   $FAIL_COUNT"
echo "Мест было изначально: $SEATS"
echo "Мест осталось:        $FINAL_SEATS"
echo

if (( SUCCESS_COUNT > SEATS )); then
  echo "⚠️  RACE CONDITION ПОДТВЕРЖДЕНА: успешных броней ($SUCCESS_COUNT) больше, чем было мест ($SEATS)."
elif [[ "$FINAL_SEATS" =~ ^-?[0-9]+$ ]] && (( FINAL_SEATS < 0 )); then
  echo "⚠️  RACE CONDITION ПОДТВЕРЖДЕНА: availableSeats ушёл в минус ($FINAL_SEATS)."
else
  echo "✅  Мест не переброшено. Похоже, гонка не проявилась в этом запуске — попробуй увеличить CONCURRENCY и запустить ещё раз, гонки не всегда воспроизводятся с первого раза."
fi
