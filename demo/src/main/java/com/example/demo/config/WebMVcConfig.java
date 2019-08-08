package com.example.demo.config;

import com.example.demo.util.Constant;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class WebMVcConfig extends WebMvcConfigurerAdapter {
    /**
     * 配置的图片映射
     */
    private static final String imgPath = "file:" + Constant.UPLOAD_PATH + Constant.IMG_FILE_NAME +  "/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将所有访问img/virtual/**的请求映射到文件上传的路径下 C:\Users\wanghao/upload/img（图片的保存路径）
        registry.addResourceHandler("/img/virtual/**").addResourceLocations(imgPath);
        super.addResourceHandlers(registry);
    }
}
