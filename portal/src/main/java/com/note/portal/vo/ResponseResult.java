package com.note.portal.vo;

/**
 * @Description: 返回给前端的数据,因为是和数据库相关，所以命名为SqlResult
 * @Author 翁阳
 * @Date 2019年09月24日
 **/
public class ResponseResult {
    //true: 表示操作成功
    //false： 表示操作失败
    private Boolean success;
    //提示的信息或者返回给前端的数据
    private Object data;

    public ResponseResult() {
    }

    public ResponseResult(Boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
