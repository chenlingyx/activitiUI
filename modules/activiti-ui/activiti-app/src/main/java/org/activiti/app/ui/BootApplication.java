package org.activiti.app.ui;

import com.google.common.collect.Lists;
import org.activiti.app.conf.ApplicationConfiguration;
import org.activiti.app.servlet.ApiDispatcherServletConfiguration;
import org.activiti.app.servlet.AppDispatcherServletConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author chenling
 * @date 2019/12/15 3:35
 * @since V1.0.0
 */
@SpringBootApplication //(exclude = {SecurityAutoConfiguration.class})
@Import({ApplicationConfiguration.class})
public class BootApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class,args);
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BootApplication.class);
    }


    @Bean
    public ServletRegistrationBean apiDispatcher(){
        DispatcherServlet api = new DispatcherServlet();
        api.setContextClass(AnnotationConfigWebApplicationContext.class);
        api.setContextConfigLocation(ApiDispatcherServletConfiguration.class.getName());
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(api);
        registrationBean.setUrlMappings(Lists.newArrayList("/api/*"));
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);
        registrationBean.setName("api");
        return registrationBean;
    }


    @Bean
    public ServletRegistrationBean appDispatcher(){
        DispatcherServlet app = new DispatcherServlet();
        app.setContextClass(AnnotationConfigWebApplicationContext.class);
        app.setContextConfigLocation(AppDispatcherServletConfiguration.class.getName());
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(app);
        registrationBean.setUrlMappings(Lists.newArrayList("/app/*"));
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);
        registrationBean.setName("app");
        return registrationBean;
    }


    @Bean
    public FilterRegistrationBean filterRegistration(){
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean(new DelegatingFilterProxy());
        filterRegistration.setUrlPatterns(Lists.newArrayList("/*"));

        filterRegistration.setName("springSecurityFilterChain");
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setOrder(-1);
        return filterRegistration;

    }
}
