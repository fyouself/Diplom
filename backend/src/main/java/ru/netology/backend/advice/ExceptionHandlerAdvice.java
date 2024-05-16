package ru.netology.backend.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.backend.exception.BadRequest;
import ru.netology.backend.exception.InternalServerError;
import ru.netology.backend.exception.Unauthorized;

import java.util.UUID;


@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<?> badRequest(BadRequest exception, HttpServletRequest req) {
        String serverScheme = req.getScheme();
        String serverHost = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getServletPath();
        String targetBase = serverScheme + "://" + serverHost + ":" + serverPort + contextPath;
        StringBuilder logText = new StringBuilder();
        ErrorMessageLogin error;
        UUID id = UUID.randomUUID();
        if (exception.getMessage().equals("email")) {
            error = new ErrorMessageLogin(id, new String[]{"Неправильный email"}, new String[]{"Неправильный пароль"});
            logText.append("Incorect emael ").append("path ").append(targetBase);
            logger.error(String.valueOf(logText));
        } else {
            error = new ErrorMessageLogin(id, new String[]{}, new String[]{"Неправильный пароль"});
            logText.append("Incorect password ").append("path ").append(targetBase);
            logger.error(String.valueOf(logText));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<ErrorMessage> unauthorized(Unauthorized exception, HttpServletRequest req) {
        String serverScheme = req.getScheme();
        String serverHost = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getServletPath();
        String targetBase = serverScheme + "://" + serverHost + ":" + serverPort + contextPath;
        StringBuilder logText = new StringBuilder();
        UUID id = UUID.randomUUID();
        ErrorMessage errorMessage = new ErrorMessage(id, exception.getMessage());
        logText.append(exception.getMessage()).append(" ").append("path ").append(targetBase);
        logger.error(String.valueOf(logText));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ErrorMessage> internalServerError(InternalServerError exception, HttpServletRequest req) {
        String serverScheme = req.getScheme();
        String serverHost = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getServletPath();
        StringBuilder logText = new StringBuilder();
        String targetBase = serverScheme + "://" + serverHost + ":" + serverPort + contextPath;
        UUID id = UUID.randomUUID();
        ErrorMessage errorMessage = new ErrorMessage(id, exception.getMessage());
        logText.append(exception.getMessage()).append(" ").append("path ").append(targetBase);
        logger.error(String.valueOf(logText));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
