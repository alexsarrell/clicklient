package com.aalpov.clickhouseclient.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "clickhouse")
data class ClickhouseProperties(
    val url: String,
    val username: String,
    val password: String
)