package com.yin.common;

import com.yin.common.aop.R;
import com.yin.config.error.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {


    private String code;
    private String message;
    private R rTime;
    private T data;

    //自定义消息
    public static <T> Result<T> ok(String message, T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String message, T data) {
        Result result = new Result();
        result.setCode("500");
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    //根据code值获取返回消息
    public static <T> Result<T> ok(Integer code, T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage(ExceptionEnum.getMsgByCode(code));
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(Integer code, T data) {
        Result result = new Result();
        result.setCode("500");
        result.setMessage(ExceptionEnum.getMsgByCode(code));
        result.setData(data);
        return result;
    }

    //根据code值获取返回消息
    public static <T> Result<T> ok(ExceptionEnum code, T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage(ExceptionEnum.getMsgByCode(code.getResultCodeInt()));
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(ExceptionEnum code, T data) {
        Result result = new Result();
        result.setCode("500");
        result.setMessage(ExceptionEnum.getMsgByCode(code.getResultCodeInt()));
        result.setData(data);
        return result;
    }

    //自定义消息还有返回耗时
    public static <T> Result<T> ok(String message, R time, T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage(message);
        result.setData(data);
        result.setRTime(time);
        return result;
    }

    public static <T> Result<T> error(String message, R time, T data) {
        Result result = new Result();
        result.setCode("500");
        result.setMessage(message);
        result.setData(data);
        result.setRTime(time);
        return result;
    }

    public static <T> Result<T> okInsert(T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("插入成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> okUpdate(T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("修改成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> okDelete(T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("删除成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> okSel(T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("查询成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> errInsert(T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("插入失败");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> errUpdate(T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("修改失败");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> errDelete(T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("删除失败");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> errSel(T data) {
        Result result = new Result();
        result.setCode("200");
        result.setMessage("查询失败");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> rInsert(Integer type, T data) {
        Result result = new Result();
        if (type >= 1) {
            result.setCode("200");
            result.setMessage("插入成功");
            result.setData(data);
        } else {
            result.setCode("500");
            result.setMessage("插入失败");
            result.setData(data);
        }

        return result;
    }

    public static <T> Result<T> rUpdate(Integer type, T data) {
        Result result = new Result();
        if (type >= 1) {
            result.setCode("200");
            result.setMessage("修改成功");
            result.setData(data);
        } else {
            result.setCode("500");
            result.setMessage("修改失败");
            result.setData(data);
        }
        return result;
    }

    public static <T> Result<T> rDelete(Integer type, T data) {
        Result result = new Result();
        if (type >= 1) {
            result.setCode("200");
            result.setMessage("删除成功");
            result.setData(data);
        } else {
            result.setCode("500");
            result.setMessage("删除失败");
            result.setData(data);
        }
        return result;
    }

    //自定义消息
    public static <T> Result<T> res(T data, Boolean b) {
        Result result = new Result();
        if (b) {
            result.setCode("200");
            result.setMessage("成功");
            result.setData(data);
        } else {
            result.setCode("500");
            result.setMessage("失败");
            result.setData(data);
        }
        return result;
    }
}
