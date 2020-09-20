package com.py7hon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 启动类
 *
 * @author Seven
 * @version 1.0
 * @date 2020/9/19 22:52
 */
@SpringBootApplication
@Controller
public class ThresholdSchemeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThresholdSchemeApplication.class, args);
    }

    @RequestMapping({"/", "/home", "/index"})
    public String home() {
        return "index";
    }
}
