package com.kennycason.kakyll.view.template

import groovy.text.SimpleTemplateEngine
import groovy.text.markup.MarkupTemplateEngine
import org.rythmengine.Rythm
import org.rythmengine.RythmEngine
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by kenny on 5/18/17.
 */
class GroovySimpleTemplateEngine : TemplateEngine {
    val templateEngine = MarkupTemplateEngine()

    override fun apply(content: String, parameters: Map<String, Any>): String {
        val stringWriter = StringWriter()
        templateEngine
                .createTemplate(content)
                .make(parameters)
                .writeTo(stringWriter)

        return stringWriter.toString()
    }

}