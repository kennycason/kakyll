package com.kennycason.kakyll.view.render

import com.kennycason.kakyll.exception.KakyllException
import com.kennycason.kakyll.view.GlobalContext
import com.kennycason.kakyll.view.render.HtmlPageRenderer
import com.kennycason.kakyll.view.template.*
import org.apache.commons.io.FilenameUtils

/**
 * Determine which templates engine to use based on the configuration file.
 *
 * Only supports handlebars for now.
 */
class TemplateEngineResolver {
    private val templateEngines = mapOf(
            Pair("handlebars", HandlebarsTemplateEngine())
    )

    fun resolve(): TemplateEngine {
        val config = GlobalContext.config
        templateEngines[config.templateEngine]?.let { renderer ->
            return renderer
        }
        throw KakyllException("Could not find templates engine for [${config.templateEngine}]")
    }

}