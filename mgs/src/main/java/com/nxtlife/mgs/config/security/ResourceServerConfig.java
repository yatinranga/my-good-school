package com.nxtlife.mgs.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //name of your resource server
    private static final String RESOURCE_ID = "resource-server-api";
    private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('read')";
    private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('write')";
    private static final String SECURED_PATTERN = "/api/**";
    private static final String FORGOT_PASSWORD_PATTERN = "/forgot-password";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID);
    }

    ///todo - Allow ananoymous people- Does it work for DoIExist and signup??.
    //Say authorize on all patterns and permit them freely or check if they've the access
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers(FORGOT_PASSWORD_PATTERN).antMatchers("/doIExist").antMatchers("/signup")
                .antMatchers(SECURED_PATTERN).and().authorizeRequests()
                .antMatchers(FORGOT_PASSWORD_PATTERN).permitAll()
                .antMatchers("/doIExist").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers(HttpMethod.POST,SECURED_PATTERN).access(SECURED_WRITE_SCOPE)
                .anyRequest().access(SECURED_READ_SCOPE);
    }
}
