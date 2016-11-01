package com.robot.simplenews.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 */
public class CookieInterceptor implements Interceptor {

//  Logger logger = Logger.getLogger(CookieInterceptor.class.getSimpleName());

  public CookieInterceptor() {

  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request original = chain.request();
//    if (!TextUtils.isEmpty(mUserStorage.getCookie()) && !original.url()
//        .toString()
//        .contains("loginUsernameEmail")) {
//      Request request = original.newBuilder()
//          .addHeader("Cookie", "u=" + URLEncoder.encode(mUserStorage.getCookie()) + ";")
//          .build();
//      return chain.proceed(request);
//    } else {
//      for (String header : chain.proceed(original).headers("Set-Cookie")) {
//        if (header.startsWith("u=")) {
//          String cookie = header.split(";")[0].substring(2);
//          logger.debug("cookie:" + cookie);
//          if (!TextUtils.isEmpty(cookie)) {
//            Constants.Cookie = cookie;
//          }
//        }
//      }
//    }
    return chain.proceed(original);
  }
}
