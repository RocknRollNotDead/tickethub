
CREATE TABLE events
(
    id                      BIGSERIAL    PRIMARY KEY,
    name                    VARCHAR(255)    NOT NULL,
    date                    TIMESTAMPTZ     NOT NULL,
    total_seats             INTEGER          NOT NULL,
    available_seats         INTEGER          NOT NULL,

        CONSTRAINT chk_seats_non_negative
            CHECK (
                total_seats >= 0 AND available_seats >= 0
                ),
        CONSTRAINT chk_total_seats_more_then_available
                CHECK (
                total_seats >= available_seats
                )
);

CREATE TABLE users
(
    id                      BIGSERIAL    PRIMARY KEY,
    name                    VARCHAR(255)    NOT NULL,
    balance                 BIGINT          NOT NULL ,
    CONSTRAINT chk_balance_more_then_null
        CHECK (
            balance >= 0
            )

);

CREATE TABLE bookings
(
    id                      BIGSERIAL    PRIMARY KEY,
    user_id                 BIGINT       NOT NULL,
    event_id                BIGINT       NOT NULL,
    status                  VARCHAR(50)  NOT NULL DEFAULT 'RESERVED',

    CONSTRAINT fk_booking_owner
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE RESTRICT,
    CONSTRAINT fk_booking_event
        FOREIGN KEY (event_id)
            REFERENCES events (id)
            ON DELETE RESTRICT

);

CREATE INDEX idx_bookings_user_id ON bookings(user_id);

CREATE INDEX idx_bookings_event_id ON bookings(event_id);