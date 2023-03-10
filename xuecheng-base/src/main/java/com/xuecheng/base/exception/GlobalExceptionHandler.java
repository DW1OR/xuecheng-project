package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.StringJoiner;

@Slf4j
@ControllerAdvice//控制器增强
public class GlobalExceptionHandler {

    //处理XueChengException异常
    @ResponseBody//将信息返回为json格式
    @ExceptionHandler(XueChengException.class) //此方法捕获XueChengException异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //状态码返回500
    public RestErrorResponse doXueChengPlusException(XueChengException e) {
        log.error("捕获异常：{}", e.getErrMessage());
        e.printStackTrace();
        String errMessage = e.getErrMessage();
        return new RestErrorResponse(errMessage);
    }

    //捕获不可预知异常Exception
    @ResponseBody //将信息返回为json格式
    @ExceptionHandler(Exception.class) //此方法捕获Exception异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //状态码返回500
    public RestErrorResponse doException(Exception e) {
        log.error("捕获异常：{}", e.getMessage());
        e.printStackTrace();
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
    }

    @ResponseBody //将信息返回为json格式
    @ExceptionHandler(MethodArgumentNotValidException.class) //此方法捕获MethodArgumentNotValidException异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //状态码返回500
    public RestErrorResponse doMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        //校验的错误信息
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        //收集错误
        StringJoiner stj = new StringJoiner(","); //1.8特性，用于字符串拼接
        //StringBuffer errors = new StringBuffer();
        fieldErrors.forEach(error -> {
            stj.add(error.getDefaultMessage());
            //errors.append(error.getDefaultMessage()).append(",");
        });
        //return new RestErrorResponse(errors.toString());
        return new RestErrorResponse(stj.toString());
    }
}