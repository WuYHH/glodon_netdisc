package com.glodon.glodon_netdisc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
/**
 * @author wuyuhan
 * @date 2023/8/30 21:41
 */
public class HomeController {

    private final HttpServletRequest request;

    public HomeController(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    @GetMapping("/get-browser-info")
    public String getBrowserInfo() {
        // 获取浏览器信息
        String userAgent = request.getHeader("User-Agent");
        // 获取客户端 IP 地址
        String ipAddress = request.getRemoteAddr();

        // 其他信息，如请求方式、URI等
        String method = request.getMethod();
        String uri = request.getRequestURI();

        return "User Agent: " + userAgent + "<br>"
                + "IP Address: " + ipAddress + "<br>"
                + "Method: " + method + "<br>"
                + "URI: " + uri;
    }
}



