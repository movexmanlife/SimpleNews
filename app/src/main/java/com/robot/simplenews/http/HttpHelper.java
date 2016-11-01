package com.robot.simplenews.http;

import java.util.Map;

/**
 * Created by Administrator on 2016/10/28.
 */
public class HttpHelper {
    private static String JSession_Id;
    private Map<? extends String, ? extends String> publicParams;

    public static String getJSession_Id() {
        return JSession_Id;
    }

    public Map<? extends String, ? extends String> getPublicParams() {
        return publicParams;
    }
}
