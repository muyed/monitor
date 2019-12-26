package com.muye.monitor.common.exception;

public class MonitorException extends RuntimeException {

    private String msg;
    private String code;

    public MonitorException(){
        super();
    }

    public MonitorException(String msg){
        super(msg);
        this.msg = msg;
    }

    public MonitorException(String code, String msg){
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public MonitorException(String code, String msg, Throwable e){
        super(msg, e);
        this.code = code;
        this.msg = msg;
    }

    public MonitorException(String msg, Throwable e){
        super(msg, e);
        this.msg = msg;
    }

    public MonitorException(Throwable e){
        super(e);
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }
}
