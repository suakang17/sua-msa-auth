package com.sua.authserver.global.config;

import com.sua.authserver.global.tools.MemberToResponseConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsConfig {

    @Bean
    public MemberToResponseConverter converter() {
        return new MemberToResponseConverter();
    }
}
