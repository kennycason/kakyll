package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.view.render.Page
import com.kennycason.kakyll.view.render.PageRendererResolver
import com.kennycason.kakyll.view.render.TemplateEngineResolver
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths


/**
 * Created by kenny on 12/22/17.
 *
 * build a page for each tag linking it to each post sharing the tag
 */
class TagPageRenderer {

    private val rendererResolver = PageRendererResolver()
    private val templateEngineResolver = TemplateEngineResolver()

    fun render() {
        println("└ Rendering tag pages to tags/")

        val config = GlobalContext.config
        val posts = GlobalContext.posts
        val tags = GlobalContext.tags

        // create directory for each tag file to be written to
        val tagFilesPath = Paths.get(Structures.Directories.SITE, "tags")
        Files.createDirectories(tagFilesPath)

        // read template file
        val encoding = Charset.forName(config.encoding)
        val tagsTemplatePath = Paths.get(Structures.Files.INDEX) // tags pages are just lists of posts, like the index. make separate file for this to avoid confusion
        val content = tagsTemplatePath.toFile().readText(encoding)

        val renderer = rendererResolver.resolve(tagsTemplatePath)

        // now for each tag, apply template and write separate tag file
        tags.forEach { tag ->
            val page = renderer.render(content)

//            println("tag filter: ${tag.tag}")
            page.parameters.put("file", tag.tag + ".html") // TODO ?
            page.parameters.put("posts", posts
                    .map(this::flattenToMap)
                    .filter { data ->
//                        println("    ${tag.tag} -> ${data["tags"]}")
                        (data.containsKey("tags")
                        && (data["tags"] as List<String>).contains(tag.tag))
                    }
                    .toList())

//            (page.parameters.get("posts") as List<MutableMap<String, Any>>).forEach { page ->
//                println("  after -> ${page["tags"]} -> ${page["url"]}")
//            }

            page.parameters.put("tag_cloud", tags)

            // apply templates engine
            val templateEngine = templateEngineResolver.resolve()
            val templateHtml = templateEngine.apply(page.content, page.parameters)

            // now inject everything into primary default templates
            val defaultTemplate = Paths.get(Structures.Directories.TEMPLATES, Structures.Files.Templates.DEFAULT).toFile().readText(encoding)
            page.parameters.put("content", templateHtml) // consider clean way to do this

            val renderedHtml = templateEngine.apply(defaultTemplate, page.parameters)

            // output content
            val outputFile = File(tagFilesPath.toFile(), buildTargetFileName(tag.tag))
            outputFile.writeText(renderedHtml, encoding)
        }
    }

    private fun buildTargetFileName(source: String) = FilenameUtils.removeExtension(source) + ".html"

    private fun flattenToMap(data: Page): MutableMap<String, Any> {
        val flattenedMap = mutableMapOf<String, Any>()
        flattenedMap.put("content", data.content)
        flattenedMap.putAll(data.parameters)
        return flattenedMap
    }

}