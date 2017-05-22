package com.kennycason.kakyll.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.kennycason.kakyll.Constants
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by kenny on 5/18/17.
 */
class ConfigLoader {
    private val objectMapper = ObjectMapper(YAMLFactory())
    init {
        objectMapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
    }

    fun load(): Config {
        if (!Files.exists(Paths.get(Constants.Files.CONFIG))) {
            throw RuntimeException("Config file [${Constants.Files.CONFIG}] could not be found.")
        }
        return objectMapper.readValue(File(Constants.Files.CONFIG), Config::class.java)
    }

}