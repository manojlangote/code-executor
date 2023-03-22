package com.code.exe;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.code.exe.rest", "com.code.exe.service", "com.code.exe.utils"})
public class AppConfig {
}