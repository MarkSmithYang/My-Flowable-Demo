package com.yb.flowable.common;

/**
 * Description:接口返回结果统一封账类
 * author biaoyang
 * date 2019/3/14 001410:27
 */
public class ResponseResult<T> {

    private String message;
    private Integer statusCode;
    private T resultData;

    public static <T> ResponseResult<T> successResultData(T resultData) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setMessage("success");
        result.setStatusCode(200);
        result.setResultData(resultData);
        return result;
    }

    public static <T> ResponseResult<T> errorMessage(String errorMessage) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setMessage(errorMessage);
        result.setStatusCode(400);
        result.setResultData(null);
        return result;
    }

    public static <T> ResponseResult<T> statusCode(Integer statusCode) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setStatusCode(statusCode);
        return result;
    }

    public ResponseResult<T> message(String message) {
        this.message=message;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }
}
