package com.aalpov.clickhouseclient

import com.aalpov.opendbadapter.annotations.ClickhouseTablesScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@ClickhouseTablesScan(basePackages = ["com.aalpov.clickhouseclient.tables"])
open class Application

fun main(args: Array<String>) {
    try {
        runApplication<Application>(*args)
    } catch (ex: java.lang.Exception) {
        ex.printStackTrace()
    }
}

