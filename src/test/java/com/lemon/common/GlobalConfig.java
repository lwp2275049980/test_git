package com.lemon.common;

public class GlobalConfig {
    //定义一个全局静态开关，用于控制日志输出
    //true--调试模式--日志输出控制台
    //false--自动化执行模式--日志重定向到本地文件并集成到Allure报表
    public static final boolean IS_DEBUG = true;

    //项目地址
    public static final String base_uri = "http://mall.lemonban.com:8107";
}
