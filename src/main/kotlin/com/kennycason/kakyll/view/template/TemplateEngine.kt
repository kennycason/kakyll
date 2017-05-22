package com.kennycason.kakyll.view.template

import com.kennycason.kakyll.view.render.Page
import java.nio.file.Path

/**
 * Created by kenny on 5/18/17.
 */
interface TemplateEngine {
    fun apply(content: String, parameters: Map<String, Any> = mutableMapOf()): String
}