package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.ConfigLoader
import com.kennycason.kakyll.util.Colors
import com.kennycason.kakyll.view.posts.PostsLoader
import com.kennycason.kakyll.view.render.Page
import com.kennycason.kakyll.view.render.TemplateEngineResolver
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Paths

/**
 * Created by kenny on 5/18/17.
 *
 * take loaded posts and apply templates, then render to html file
 */
class PostsRenderer {
    private val templateEngineResolver = TemplateEngineResolver()

    fun render() {
        val config = GlobalContext.config
        val posts = GlobalContext.posts
        val tags = GlobalContext.tags

        println("└ Rendering posts to directory [${config.posts.directory}]")
        val outputDir = File(Structures.Directories.SITE, config.posts.directory)
        outputDir.mkdirs()

        val encoding = Charset.forName(config.encoding)

        val templateEngine = templateEngineResolver.resolve()

        posts.forEach { post ->
            try {
                println("    └ ${post.parameters["original_file"]}")

                // apply template for global parameters
                post.parameters["posts"] = posts.map(this::transformToMap).toList()
                post.parameters["tag_cloud"] = tags

                // if no template was provided return as-is, it will be injected into default.hbs
                if (config.posts.template.isBlank()) {

                    // only apply default template
                    post.parameters["content"] = post.content
                    val defaultTemplate = Paths.get(
                            Structures.Directories.TEMPLATES, Structures.Files.Templates.DEFAULT).toFile().readText(encoding)
                    val templateHtml = templateEngine.apply(defaultTemplate, post.parameters)

                    // output content
                    val outputFile = File(outputDir.path, post.parameters.get("file") as String)
                    outputFile.writeText(templateHtml, encoding)
                }
                else { // else inject into custom template for posts. this will further be injected into default.hbs

                    // apply provided template
                    val providedTemplate = Paths.get(
                            Structures.Directories.TEMPLATES,
                            config.posts.template).toFile().readText(encoding)

                    post.parameters["content"] = post.content
                    val templateHtml = templateEngine.apply(providedTemplate, post.parameters)

                    // now apply default template
                    post.parameters["content"] = templateHtml
                    val defaultTemplate = Paths.get(
                            Structures.Directories.TEMPLATES, Structures.Files.Templates.DEFAULT).toFile().readText(encoding)
                    val finalHtml = templateEngine.apply(defaultTemplate, post.parameters)

                    val outputFile = File(outputDir.path, post.parameters.get("file") as String)
                    outputFile.writeText(finalHtml, encoding)
                }
            } catch (e: RuntimeException) {
                println("${Colors.ANSI_RED}Failed to render page: [${post.parameters["original_file"]}] due to [${e.message}]${Colors.ANSI_RESET}")
            }
        }
    }

    // remove copy paste SinglePageRenderer.kt
    private fun transformToMap(data: Page): MutableMap<String, Any> {
        val flattenedMap = mutableMapOf<String, Any>()
        flattenedMap["content"] = data.content
        flattenedMap.putAll(data.parameters)
        return flattenedMap
    }

}