package com.yin.config.error;

/**
 * 异常处理枚举嘞
 */
public enum ExceptionEnum implements BaseErrorInfoInterface {
    // 数据操作错误定义
    SUCCESS("2000", "成功!"),
    BODY_NOT_MATCH("4000","请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("4001","请求的数字签名不匹配!"),
    NOT_FOUND("4004", "未找到该资源!"),
    INTERNAL_SERVER_ERROR("5000", "服务器内部错误!"),
    CODE_DELETE("9999","删除失败，此节点下还有数据"),
    CODE_VERIFY("9998","参数校验失败"),
    CODE_DELETE_DICTIONARY("9997","字典数据不存在"),
    CODE_SQL_EXCEPTION("9996","SQL执行异常"),
    CODE_A_EXISTS("9995","数据已存在"),
    CODE_CZ("9994","操作成功"),
    CODE_CZ_ERR("9993","操作失败"),
    CODE_VERIFY_PAGE("9992","分页参数数据验证失败!"),
    SERVER_BUSY("5003","服务器正忙，请稍后再试!");


    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }
    //获取int类型
    @Override
    public Integer getResultCodeInt() {
        return Integer.valueOf(resultCode);
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
    public static String getMsgByCode(Integer code){
        for(ExceptionEnum e : ExceptionEnum.values()){
//            System.out.println(JSONObject.toJSONString(e));
//            System.out.println(JSONObject.toJSONString(e.getResultCode()));
//            System.out.println(JSONObject.toJSONString(e.getResultCodeInt()));
//            System.out.println(JSONObject.toJSONString(e.getResultMsg()));
            if(e.getResultCodeInt().equals(code)){
                return e.getResultMsg();
            }
        }
        // 如果找不到对应code的msg，可以返回null或者其他默认值
        return "其他异常";
    }
    public static String getMsgByCode(String code){
        for(ExceptionEnum e : ExceptionEnum.values()){
            if(e.getResultCode().equals(code)){
                return e.getResultMsg();
            }
        }
        // 如果找不到对应code的msg，可以返回null或者其他默认值
        return "其他异常";
    }
}
