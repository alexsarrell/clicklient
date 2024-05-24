package com.aalpov.clickhouseclient.configuration

import com.aalpov.opendbadapter.configuration.DatabaseProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class AppConfiguration {

    @Bean
    open fun properties(clickhouseProperties: ClickhouseProperties): DatabaseProperties {
        return DatabaseProperties(
            clickhouseProperties.url,
            clickhouseProperties.username,
            clickhouseProperties.password
        )
    }
}
