package com.kennycason.kakyll.view.template

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.io.FileTemplateLoader
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by kenny on 5/18/17.
 */
class HandlebarsTemplateEngine : TemplateEngine {

    override fun apply(content: String, parameters: Map<String, Any>): String {
        val templateLoader = FileTemplateLoader("templates")
        val handlebars = Handlebars(templateLoader)
        val template = handlebars.compileInline(content)
        return template.apply(parameters)
    }

}