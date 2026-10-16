package com.yin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yin
 */
@Slf4j
public class RequestTimeInterceptor implements HandlerInterceptor {
    //    路径匹配器，支持通配符的比较,springframework自带
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
//        log.info("结束请求");
//        每次请求完毕后，都会清楚当前请求的用户id
//        BaseContext.clearUserId();
    }

    /**
     * 判断路径是否可以放行方法
     *
     * @param urls       传递要放行的路径
     * @param requestURI 请求的路径
     * @return 返回是否放行
     */
    public boolean check(String[] urls, String requestURI) {
        //比较路径是否放行
        for (String url : urls) {
//            match：第一个参数为放行的路径，第二个为请求的路径
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
