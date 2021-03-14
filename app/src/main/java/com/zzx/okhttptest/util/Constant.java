package com.zzx.okhttptest.util;

public enum Constant {
    INSTANCE;

    public static final String BASEURL = "http://192.168.101.10:8080/SSHEnv/";
//    public static final String BASEURL = "https://192.168.101.10:8443/SSHEnv/";

    public static final String LOGINURL = BASEURL + "login";
    public static final String POSTSTRING = BASEURL + "postString";

}
