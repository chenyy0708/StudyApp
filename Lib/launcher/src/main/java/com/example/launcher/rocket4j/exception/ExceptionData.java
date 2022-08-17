package com.example.launcher.rocket4j.exception;

import java.util.HashMap;
import java.util.Map;

public class ExceptionData {

    private ExceptionData() {
    }

    public static class Builder {

        private Builder(){}

        public Builder(String group, String tag) {
            this.group = group;
            this.tag = tag;
        }

        //必传，异常tag
        private String tag;

        //必传，异常分组
        private String group;

        //
        private String errorMessage;

        //可选
        private Throwable ex;

        //自定义数据，可选
        private Map<String, Object> extData;

        public Builder addException(Throwable ex) {
            this.ex = ex;
            return this;
        }

        public Builder putExtra(String key, Object value) {
            if (extData == null) {
                extData = new HashMap<>();
            }
            extData.put(key, value);
            return this;
        }

        public Builder addErrorMsg(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder setExtra(Map<String,Object> extraData){
            if (extData == null){
                this.extData = extraData;
            }else{
                this.extData.putAll(extraData);
            }
            return this;
        }

        public ExceptionData get() {
            ExceptionData data = new ExceptionData();
            data.setGroup(this.group);
            data.setTag(this.tag);
            data.setException(this.ex);
            data.setErrorMessage(this.errorMessage);
            data.setExtData(this.extData);
            return data;
        }
    }

    //必传，异常tag
    private String tag;

    //必传，异常分组
    private String group;

    //
    private String errorMessage;

    //可选
    private Throwable ex;

    //自定义数据，可选
    private Map<String, Object> extData;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setException(Throwable ex) {
        this.ex = ex;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static Builder createBuilder(String group, String tag) {
        return new Builder(group, tag);
    }

    public void setExtData(Map<String, Object> extData) {
        this.extData = extData;
    }

    public String getGroup() {
        return group;
    }

    public Map<String, Object> getExtData() {
        return extData;
    }

    public String getTag() {
        return tag;
    }

    public Throwable getExcetion() {
        return ex;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
