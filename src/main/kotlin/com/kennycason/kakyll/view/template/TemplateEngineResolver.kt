package com.kennycason.kakyll.view.render

import com.kennycason.kakyll.view.GlobalContext
import com.kennycason.kakyll.view.render.HtmlPageRenderer
import com.kennycason.kakyll.view.template.*
import org.apache.commons.io.FilenameUtils

/**
 * Determine which templates engine to use based on the configuration file.
 */
class TemplateEngineResolver {
    private val templateEngines = mapOf(
//            Pair("rythm", RythmTemplateEngine()),
//            Pair("groovy", GroovySimpleTemplateEngine()),
//            Pair("thymeleaf", ThymeLeafTemplateEngine()),
//            Pair("jtwig", JTwigTemplateEngine()),
            Pair("handlebars", HandlebarsTemplateEngine())
    )

    fun resolve(): TemplateEngine {
        val config = GlobalContext.config
        templateEngines[config.templateEngine]?.let { renderer ->
            return renderer
        }
        throw RuntimeException("Could not find templates engine for [${config.templateEngine}]")
    }

}