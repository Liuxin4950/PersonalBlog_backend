package com.liuxin.interceptor;

import com.liuxin.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private static final String LOGIN_URL = "login";  // 将 "login" 提取为常量
    private static final String TOKEN_HEADER = "token";  // 将 "token" 提取为常量
    private static final String AUDIO_PATH = "/tts_outputs/";  // 添加常量来标识音频文件路径

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        log.info("请求的URL: {}", url);

        // 如果是音频文件请求，直接放行
        if (url.startsWith(AUDIO_PATH)) {
            log.info("音频文件请求，放行");
            return true;
        }
        // 如果是登录请求，直接放行
        if (url.contains(LOGIN_URL)) {
            log.info("登录操作请求，放行");
            return true;
        }

        // 获取请求中的token
        String jwt = request.getHeader(TOKEN_HEADER);

        // 如果token为空，返回401错误
        if (!StringUtils.hasLength(jwt)) {
            log.warn("请求头中缺少 token，返回401未授权");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("请先登录！");
            return false;
        }

        // 解析token并进行合法性校验
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            log.error("令牌解析失败: {}", e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("token解析失败疑似过期请重新登录");
            return false;
        }

        // 令牌合法，放行
        log.info("令牌合法，放行");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 后置处理逻辑（可以留空）
        log.debug("请求处理完成，准备渲染视图");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 页面加载完成后执行的逻辑（可以留空）
        log.debug("请求已完成，响应已发送");
    }
}
