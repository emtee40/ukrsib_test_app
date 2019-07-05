package com.semitop7.configuration;

import com.semitop7.component.Slf4jMDCFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Slf4jMDCFilterConfiguration {
    @Autowired
    private Slf4jMDCFilter slf4jMDCFilter;

    @Bean
    public FilterRegistrationBean servletRegistrationBean() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(slf4jMDCFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}