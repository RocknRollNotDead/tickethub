package ru.codeportfolio.tickethub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.codeportfolio.tickethub.model.Action;

import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {
    private final Queue<String> auditLog;
    public void incrementAction(Action action) {
        switch (action) {
            case CREATE_BOOK -> incrementActionToLog(
                    "Create booking");
            case CREATE_EVENT -> incrementActionToLog(
                    "Create event");

        }
    }

    private void incrementActionToLog(String action) {
        if (auditLog.size() > 10) {
            auditLog.remove();
        }
        auditLog.add(action);
    }
}
