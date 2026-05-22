package com.gordeok.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    /**
     * /uploads/** 요청을 로컬 디스크 경로로 매핑.
     * 예) GET /uploads/profiles/uuid.jpg → ./uploads/profiles/uuid.jpg 파일 반환
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().normalize().toString();

        registry.addResourceHandler(urlPrefix + "/**")
                .addResourceLocations("file:" + absolutePath + "/");
    }
}
