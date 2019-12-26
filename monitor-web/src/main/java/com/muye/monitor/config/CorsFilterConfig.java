package com.muye.monitor.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*", filterName = "corsFilter")
public class CorsFilterConfig implements Filter {

    private String allowOrigin;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String currentOrigin = request.getHeader("Origin");
        String method = request.getMethod();
        if (this.matchAllowOrigin(request, currentOrigin)) {
            response.setHeader("Access-Control-Allow-Origin", currentOrigin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET,POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Max-Age", "3600");
            String requestHeaders = request.getHeader("Access-Control-Request-Headers");
            if (StringUtils.isNotBlank(requestHeaders) && method.equals("OPTIONS")) {
                response.setHeader("Access-Control-Allow-Headers", requestHeaders);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean matchAllowOrigin(HttpServletRequest request, String currentOrigin) {
        String defaultAllowOrigin = "^.*\\.3songshu\\.com$";
        if (StringUtils.isBlank(currentOrigin)) {
            return false;
        } else if (currentOrigin.matches(defaultAllowOrigin)) {
            return true;
        } else if (StringUtils.isBlank(this.allowOrigin)) {
            return false;
        } else {
            List<String> allowOriginList = Arrays.asList(this.allowOrigin.split(","));
            if (allowOriginList != null && !allowOriginList.isEmpty()) {
                Iterator var5 = allowOriginList.iterator();

                String allow;
                do {
                    if (!var5.hasNext()) {
                        return false;
                    }

                    allow = (String)var5.next();
                } while(!currentOrigin.matches(allow));

                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowOrigin = filterConfig.getInitParameter("allowOrigin");
    }

    @Override
    public void destroy() {
    }
}
