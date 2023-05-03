package com.eli.server.exceptionsHandler;

import com.eli.server.dto.ServerErrorData;
import com.eli.server.exceptions.ServerException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ServerExceptionsHandler {

    @ExceptionHandler
    @ResponseBody
    public ServerErrorData response(Exception exception, HttpServletResponse httpServletResponse) {

        if (exception instanceof ServerException) {

            ServerException serverException = (ServerException) exception;

            int errorCode = serverException.getErrorType().getInternalErrorCode();
            String errorMessage = serverException.getErrorType().getErrorMessage();
            String errorType = String.valueOf(serverException.getErrorType());

            httpServletResponse.setStatus(errorCode);

            if (serverException.getErrorType().isShowStackTrace()) {
                serverException.printStackTrace();
            }
            return new ServerErrorData(errorType, errorMessage);
        }
        httpServletResponse.setStatus(601);
        return new ServerErrorData("General Error", "Something went wrong , try again later");
    }
}
