package com.nxtlife.mgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nxtlife.mgs.config.FileStorageProperties;

//import com.nxtlife.mgs.security.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class MgsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MgsApplication.class, args);
	}

//	@Bean
//	public BCryptPasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	
	@Bean 
	public SpringApplicationContext springApplicationContext()
	{
		return new SpringApplicationContext();
	}
	
//	@Bean(name="AppProperties")
//	public AppProperties getAppProperties()
//	{
//		return new AppProperties();
//	}
}
