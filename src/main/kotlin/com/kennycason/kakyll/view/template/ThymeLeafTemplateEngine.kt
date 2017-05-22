package com.kennycason.kakyll.view.template

import com.kennycason.kakyll.Constants
import org.rythmengine.Rythm
import org.rythmengine.RythmEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.FileTemplateResolver
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * Created by kenny on 5/18/17.
 */
class ThymeLeafTemplateEngine : TemplateEngine {
    private val templateEngine = buildTemplateEngine()


    override fun apply(content: String, parameters: Map<String, Any>): String {
        val context = Context(Locale.ENGLISH, parameters)

        return templateEngine.process(content, context)
    }

    private fun buildTemplateEngine(): org.thymeleaf.TemplateEngine {
        val templatesPath = Paths.get(Constants.Directories.TEMPLATES)
        val templateResolver = FileTemplateResolver()
        templateResolver.setTemplateMode(TemplateMode.HTML)
        templateResolver.prefix = templatesPath.toAbsolutePath().toString() + File.separatorChar

        val templateEngine = org.thymeleaf.TemplateEngine()
        templateEngine.setTemplateResolver(templateResolver)

        return templateEngine
    }

}