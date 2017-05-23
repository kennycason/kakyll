package com.kennycason.kakyll.view

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.ConfigLoader
import com.kennycason.kakyll.view.render.Page
import com.kennycason.kakyll.view.render.PageRendererResolver
import com.kennycason.kakyll.view.render.TemplateEngineResolver
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.Paths

/**
 * 1. Takes and input file
 * 2. Determine which templates engine to apply
 *      2a. Apply to all children include files recursively
 * 3. Determine how to render the file to HTML based on extension.
 * 4. Write file out to new location
 */
class PageRenderer {
    private val rendererResolver = PageRendererResolver()
    private val templateEngineResolver = TemplateEngineResolver()
    private val config = ConfigLoader().load()
    private val postsData = PostsLoader().load()

    fun render(input: Path, output: Path) {
        val encoding = Charset.forName(config.encoding)
        val content = input.toFile().readText(encoding)

        // convert to content
        val renderer = rendererResolver.resolve(input)
        val page = renderer.render(content)
        // set all post data for index to render
        page.parameters.put("posts", postsData.map(this::transformToMap).toList())

        // apply templates engine
        val templateEngine = templateEngineResolver.resolve()
        val templateHtml = templateEngine.apply(page.content, page.parameters)

        // now inject everything into primary default templates
        val defaultContent = Paths.get(Structures.Directories.TEMPLATES, Structures.Files.Templates.DEFAULT).toFile().readText(encoding)
        page.parameters.put("content", templateHtml) // consider clean way to do this
        val defaultHtml = templateEngine.apply(defaultContent, page.parameters)

        // output content
        val outputFile = File(output.toString(), buildTargetFileName(input))
        outputFile.writeText(defaultHtml, encoding)
    }

    private fun buildTargetFileName(source: Path) = FilenameUtils.removeExtension(source.toString()) + ".html"

    private fun transformToMap(data: Page): MutableMap<String, Any> {
        val flattenedMap = mutableMapOf<String, Any>()
        flattenedMap.put("content", data.content)
        flattenedMap.putAll(data.parameters)
        return flattenedMap
    }
}