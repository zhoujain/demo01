package com.zhoujian.aspectdemo.constant;

/**
 * @Description: 通用常量
 * @author: jeecg-boot
 */
public interface CommonConstant {

    //public final static String X_ACCESS_TOKEN = "X-Access-Token";

    public final static String AUTHORIZATION = "Authorization";

    /** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_OK_200 = 200;

    public static final String CHAR_1 = "\\";

    public static final String CONFIG_TYPE = "configType"; //数据类别
    public static final String DATABASE_TYPE = "databaseType"; //数据库类被
    public static final String ENCODING_TYPE = "encodingType"; // 编码
    public static final String REDIS_MODEL = "redisModel"; // redis模式
    public static final String TRANSFER_MODEL = "transferModel"; // 传输模式
    public static final String TRANSFER_PROTOCOL = "transferProtocol"; // 协议


    public static final String TABLE_TYPE_DATABASE = "database";
    public static final String TABLE_TYPE_TABLE = "table";
    public static final String TABLE_TYPE_VIEW = "view";


    public static final String OPERATION_TYPE_ADD = "add";
    public static final String OPERATION_TYPE_REMOVE = "remove";
    public static final String OPERATION_TYPE_NEED = "need";

    /**
     * 没有删除
     */
    public static final Integer DELETE_MARK_0 = 0;

    /**
     * 已经删除
     */
    public static final Integer DELETE_MARK_1 = 1;

    public static final String TASK_SOURCE = "数据注册";

    public static final String TASK_SCENE = "库表检测";

    public static final String PLAN_IN_WAY_1 = "定时任务";

    public static final String PLAN_IN_WAY_2 = "间隔任务";

    public static final String PLAN_RATE_UNIT_DAY = "天";

    public static final String PLAN_RATE_UNIT_WEEK = "周";

    public static final String PLAN_RATE_UNIT_MONTH = "月";

    // 检查任务正常
    public static final String CONFIG_STATE_1 = "1";

    // 不正常
    public static final String CONFIG_STATE_0 = "0";

    public static final String CONFIG_DATA_MODEL_1 = "发布";

    public static final String CONFIG_DATA_MODEL_2 = "订阅";

    public static final String CONFIG_IS_FRONT_LIBRARY_0 = "否";

    public static final String CONFIG_IS_FRONT_LIBRARY_1 = "是";

    public static final String FIELD_IS_BASELINE_0 = "0";

    /**
     * 是基线
     */
    public static final String FIELD_IS_BASELINE_1 = "1";

    public static final String API_PARAMETER_IN = "IN";
    public static final String API_PARAMETER_OUT = "OUT";

    public static final int FILE_STORE_START_STATE_1 = 1;

    public static final int FILE_STORE_START_STATE_0 = 0;

    /**
     * 数据表
     */
    public static final String TABLE_TYPE_1 = "1";
    public static final String TABLE_TYPE_1_NAME = "table";
    /**
     * 数据视图
     */
    public static final String TABLE_TYPE_2 = "2";
    public static final String TABLE_TYPE_2_NAME = "view";

    // 文件夹
    public static final String FTP_TYPE_FOLD = "fold";
    public static final String FTP_TYPE_FILE = "file";
}
