package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.ConfigLoader
import com.kennycason.kakyll.config.Posts
import com.kennycason.kakyll.view.render.PageRendererResolver
import com.kennycason.kakyll.view.render.Page
import com.kennycason.kakyll.view.render.TemplateEngineResolver
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Paths

/**
 *
 */
class PostsLoader {
    private val config = ConfigLoader().load()
    private val rendererResolver = PageRendererResolver()
    private val templateEngineResolver = TemplateEngineResolver()

    fun load(): List<Page> {
        val pages = mutableListOf<Page>()
        val postsDir = File(config.posts.directory)

        postsDir.walkTopDown().forEach { file ->
            if (file.isDirectory) { return@forEach }

            // load file raw text
            val encoding = Charset.forName(config.encoding)
            val content = file.readText(encoding)

            // render markdown, html, hbs, to html
            val renderer = rendererResolver.resolve(file.toPath())
            val page: Page = renderer.render(content)

            // set some basic parameters for template
            page.parameters.put("file", file.nameWithoutExtension + ".html")
            page.parameters.put("url", config.baseUrl + "/posts/" + file.nameWithoutExtension + ".html")

            val pageContent = page.content

            // if no template was provided return as-is, it will be injected into default.hbs
            if (config.posts.template.isBlank()) {

                // only apply default template
                val templateEngine = templateEngineResolver.resolve()
                page.parameters.put("content", page.content)
                val defaultTemplate = Paths.get(
                        Structures.Directories.TEMPLATES, Structures.Files.Templates.DEFAULT).toFile().readText(encoding)
                val templateHtml = templateEngine.apply(defaultTemplate, page.parameters)

                page.parameters.put("content", pageContent) // only set the original page content
                pages.add(Page(templateHtml, page.parameters))
            }
            else { // else inject into custom template for posts. this will further be injected into default.hbs
                println("Loading posts from directory [${config.posts.directory}] and applying template [${config.posts.template}]")

                // apply provided template
                val providedTemplate = Paths.get(
                        Structures.Directories.TEMPLATES,
                        config.posts.template).toFile().readText(encoding)

                val templateEngine = templateEngineResolver.resolve()
                page.parameters.put("content", page.content)
                val templateHtml = templateEngine.apply(providedTemplate, page.parameters)

                // now apply default template
                page.parameters.put("content", templateHtml)
                val defaultTemplate = Paths.get(
                        Structures.Directories.TEMPLATES, Structures.Files.Templates.DEFAULT).toFile().readText(encoding)
                val finalHtml = templateEngine.apply(defaultTemplate, page.parameters)

                // prepare and return
                page.parameters.put("content", pageContent) // only set the original page content
//                println("final post content: " + finalHtml)
//                println("post parameters: " + page.parameters)
                pages.add(Page(finalHtml, page.parameters))
            }
        }
        return pages
    }
}