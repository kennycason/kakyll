package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.view.render.Page
import com.kennycason.kakyll.view.render.PageRendererResolver
import com.kennycason.kakyll.view.render.TemplateEngineResolver
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Renders a page displaying all images from posts
 */
class ImagePageRenderer {

    private val rendererResolver = PageRendererResolver()
    private val templateEngineResolver = TemplateEngineResolver()

    fun render() {
        println("└ Rendering images page")

        val config = GlobalContext.config
        val posts = GlobalContext.posts
        val tags = GlobalContext.tags
        val images = GlobalContext.images

        // Create the output directory if it doesn't exist
        val sitePath = Paths.get(Structures.Directories.SITE)
        Files.createDirectories(sitePath)

        // Read the images template file
        val encoding = Charset.forName(config.encoding)
        val imagesTemplatePath = Paths.get("images.hbs")

        if (!imagesTemplatePath.toFile().exists()) {
            println("Images template file not found: ${imagesTemplatePath.toAbsolutePath()}")
            return
        }

        val content = imagesTemplatePath.toFile().readText(encoding)
        val renderer = rendererResolver.resolve(imagesTemplatePath)
        val page = renderer.render(content)

        // Add parameters to the page
        page.parameters["posts"] = posts.map(this::flattenToMap).toList()
        page.parameters["tag_cloud"] = tags
        page.parameters["all_images"] = images["all_images"] ?: emptyList<Any>()
        page.parameters["all_main_images"] = images["all_main_images"] ?: emptyList<Any>()
        page.parameters["all_thumbnails"] = images["all_thumbnails"] ?: emptyList<Any>()

        // Apply the template engine
        val templateEngine = templateEngineResolver.resolve()
        val templateHtml = templateEngine.apply(page.content, page.parameters)

        // Inject into the default template
        val defaultTemplate = Paths.get(Structures.Directories.TEMPLATES, Structures.Files.Templates.DEFAULT).toFile().readText(encoding)
        page.parameters["content"] = templateHtml

        val renderedHtml = templateEngine.apply(defaultTemplate, page.parameters)

        // Output the content
        val outputFile = File(sitePath.toFile(), "images.html")
        outputFile.writeText(renderedHtml, encoding)
    }

    private fun flattenToMap(data: Page): MutableMap<String, Any> {
        val flattenedMap = mutableMapOf<String, Any>()
        flattenedMap["content"] = data.content
        flattenedMap.putAll(data.parameters)
        return flattenedMap
    }
}
