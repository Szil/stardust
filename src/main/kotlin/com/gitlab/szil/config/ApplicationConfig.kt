package com.gitlab.szil.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

/**
 * Created by Szilank on 09/04/2017.
 */
class ApplicationConfig {

    constructor() {
        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
        try {
            val AppConfig = mapper.readValue<AppConfig>(javaClass.classLoader.getResource("config/appConfig.yml"))
            System.getenv()
            println(AppConfig.hikarucp.jdbcUrl)
            println(AppConfig.undertow.port)
        } catch (ex: Exception) {
            println(ex.printStackTrace())
        }
    }

}

data class AppConfig(var hikarucp: Hikarucp, var undertow: Undertow)

data class Hikarucp(var jdbcUrl: String, var username: String, var password: String)

data class Undertow(var port: Int)