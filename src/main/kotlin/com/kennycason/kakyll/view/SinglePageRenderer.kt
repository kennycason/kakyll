package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.util.Colors
import com.kennycason.kakyll.view.render.NoOpPageRenderer
import com.kennycason.kakyll.view.render.Page
import com.kennycason.kakyll.view.render.PageRendererResolver
import com.kennycason.kakyll.view.render.TemplateEngineResolver
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
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
class SinglePageRenderer {
    private val rendererResolver = PageRendererResolver()
    private val templateEngineResolver = TemplateEngineResolver()

    fun render(input: Path, output: Path) {
        println("└ Rendering [${input.toAbsolutePath()}]")
        if (!input.toFile().exists()) {
            println("${Colors.ANSI_RED}    └ Skipping, file not found.${Colors.ANSI_RESET}")
            return
        }

        val config = GlobalContext.config
        val posts = GlobalContext.posts
        val tags = GlobalContext.tags

        val encoding = Charset.forName(config.encoding)
        val content = input.toFile().readText(encoding)


        val renderer = rendererResolver.resolve(input)

        // if no renderer just copy as is
        if (renderer is NoOpPageRenderer) {
            val outputFile = File(output.toFile(), input.toString())
            println("    └ No Renderer for file [$input], copying as-is to [${outputFile}]")
            FileUtils.copyFile(input.toFile(), outputFile)
            return
        }

        // convert to content
        val page = renderer.render(content)

        // set all global data
        page.parameters["file"] = input.fileName
        page.parameters["posts"] = posts.map(this::transformToMap).toList()
        page.parameters["tag_cloud"] = tags

        // apply templates engine
        val templateEngine = templateEngineResolver.resolve()
        val templateHtml = templateEngine.apply(page.content, page.parameters)

        // now inject everything into primary default templates
        val defaultTemplate = Paths.get(Structures.Directories.TEMPLATES, Structures.Files.Templates.DEFAULT).toFile().readText(encoding)
        page.parameters["content"] = templateHtml // consider clean way to do this

        val renderedHtml = templateEngine.apply(defaultTemplate, page.parameters)

        // output content
        val outputFile = File(output.toString(), buildTargetFileName(input))
        outputFile.writeText(renderedHtml, encoding)
    }

    private fun buildTargetFileName(source: Path) = FilenameUtils.removeExtension(source.toString()) + ".html"

    private fun transformToMap(data: Page): MutableMap<String, Any> {
        val flattenedMap = mutableMapOf<String, Any>()
        flattenedMap["content"] = data.content
        flattenedMap.putAll(data.parameters)
        return flattenedMap
    }

}