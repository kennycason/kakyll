package com.kennycason.kakyll.view.render

import com.kennycason.kakyll.config.ConfigLoader
import com.kennycason.kakyll.view.render.IdentityPageRenderer
import com.kennycason.kakyll.view.template.*
import org.apache.commons.io.FilenameUtils

/**
 * Determine which template engine to use based on the configuration file.
 */
class TemplateEngineResolver {
    private val config = ConfigLoader().load()
    private val templateEngines = mapOf(
//            Pair("rythm", RythmTemplateEngine()),
//            Pair("groovy", GroovySimpleTemplateEngine()),
//            Pair("thymeleaf", ThymeLeafTemplateEngine()),
//            Pair("jtwig", JTwigTemplateEngine()),
            Pair("handlebars", HandlebarsTemplateEngine())
    )

    fun resolve(): TemplateEngine {
        templateEngines[config.templateEngine]?.let { renderer ->
            return renderer
        }
        throw RuntimeException("Could not find template engine for [${config.templateEngine}]")
    }

}