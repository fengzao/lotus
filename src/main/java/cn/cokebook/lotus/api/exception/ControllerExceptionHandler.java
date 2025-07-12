package cn.cokebook.lotus.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@Component
public class ControllerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<Void> handle(RuntimeException e, HttpServletRequest request) {
        log.error("Request process error, will return 500 with empty body . context : uri  = {} , method = {} ", request.getRequestURL(), request.getMethod(), e);
        return ResponseEntity.internalServerError().build();
    }
}
