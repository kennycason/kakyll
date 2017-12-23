package com.kennycason.kakyll.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.exception.KakyllException
import java.io.File
import java.io.InputStream
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
        if (!Files.exists(Paths.get(Structures.Files.CONFIG))) {
            throw KakyllException("Config file [${Structures.Files.CONFIG}] could not be found.")
        }
        return load(File(Structures.Files.CONFIG).inputStream())
    }

    fun load(inputStream: InputStream): Config {
        return objectMapper.readValue(inputStream, Config::class.java)
    }

}