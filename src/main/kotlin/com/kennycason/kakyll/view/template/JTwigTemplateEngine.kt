package com.kennycason.kakyll.view.template

import groovy.text.SimpleTemplateEngine
import groovy.text.markup.MarkupTemplateEngine
import org.jtwig.JtwigModel
import org.jtwig.JtwigTemplate
import org.jtwig.environment.DefaultEnvironmentConfiguration
import org.jtwig.environment.EnvironmentConfiguration
import org.jtwig.resource.resolver.ReferenceRelativeResourceResolver
import org.jtwig.resource.resolver.RelativeResourceResolver
import org.rythmengine.Rythm
import org.rythmengine.RythmEngine
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by kenny on 5/18/17.
 */
class JTwigTemplateEngine : TemplateEngine {

    override fun apply(content: String, parameters: Map<String, Any>): String {
//        val configuration = DefaultEnvironmentConfiguration()
//        configuration.resourceConfiguration.
        val template = JtwigTemplate.inlineTemplate(content)
        return template.render(JtwigModel.newModel(parameters))
    }

}