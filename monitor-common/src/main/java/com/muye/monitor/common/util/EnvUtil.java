package com.muye.monitor.common.util;

public class EnvUtil {

    private static String env = System.getProperty("spring.profiles.active");

    private static String appName = System.getProperty("project.name");

    /**
     * 获取应用名称
     *
     * @return
     */
    public static String getAppName() {
        return appName;
    }

    /**
     * 日常环境
     *
     * @return
     */
    public static boolean isDaily() {
        if ("daily".equals(env)) {
            return true;
        }
        return false;
    }


    /**
     * 本地环境
     *
     * @return
     */
    public static boolean isLocal() {
        if ("local".equals(env)) {
            return true;
        }
        return false;
    }

    /**
     * 预发环境
     *
     * @return
     */
    public static boolean isPre() {
        if ("pre".equals(env)) {
            return true;
        }
        return false;
    }

    /**
     * 生产环境
     *
     * @return
     */
    public static boolean isProd() {
        if ("prod".equals(env)) {
            return true;
        }
        return false;
    }

    /**
     * 压测环境
     *
     * @return
     */
    public static boolean isTesting() {
        if ("testing".equals(env)) {
            return true;
        }
        return false;
    }
}
