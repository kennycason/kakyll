package com.kennycason.kakyll.view.template

import org.rythmengine.Rythm
import org.rythmengine.RythmEngine
import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by kenny on 5/18/17.
 */
class RythmTemplateEngine : TemplateEngine {
    val config = mutableMapOf<String, Any>()
    val rythmEngine = RythmEngine(config)

    override fun apply(content: String, parameters: Map<String, Any>): String {
        return rythmEngine.render(content, parameters)
    }

}