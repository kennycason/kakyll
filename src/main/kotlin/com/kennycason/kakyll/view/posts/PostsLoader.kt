package com.kennycason.kakyll.view.posts

import com.kennycason.kakyll.util.Colors
import com.kennycason.kakyll.util.DateParser
import com.kennycason.kakyll.view.GlobalContext
import com.kennycason.kakyll.view.render.Page
import com.kennycason.kakyll.view.render.PageRendererResolver
import com.kennycason.kakyll.view.render.TemplateEngineResolver
import java.io.File
import java.nio.charset.Charset

/**
 * load all post data into structure for downstream processing
 */
class PostsLoader {
    private val rendererResolver = PageRendererResolver()
    private val dateParser = DateParser()

    fun load(): List<Page> {
        val config = GlobalContext.config
        val pages = mutableListOf<Page>()
        val postsDir = File(config.posts.directory)

        postsDir.walkTopDown().forEach { file ->
            if (file.isDirectory || shouldSkip(file.absolutePath)) { return@forEach }

            try {
                // load file raw text
                val encoding = Charset.forName(config.encoding)
                val content = file.readText(encoding)

                // render markdown, html, hbs, to html
                val renderer = rendererResolver.resolve(file.toPath())
                val page: Page = renderer.render(content)

                // set some basic parameters for template
                val relativeUrl = "/${config.posts.directory}/" + file.nameWithoutExtension + ".html"

                page.parameters["original_file"] = file.name
                page.parameters["file"] = file.nameWithoutExtension + ".html"
                page.parameters["url"] = relativeUrl
                page.parameters["absolute_url"] = config.baseUrl + relativeUrl
                page.parameters["content"] = page.content
                page.parameters["date"] = dateParser.prettyParse(file.nameWithoutExtension)
                page.parameters["timestamp"] = dateParser.parse(file.nameWithoutExtension).millis

                pages.add(page)

            } catch (e: RuntimeException) {
                println("${Colors.ANSI_RED}failed to render page: [${file.name}] due to [${e.message}]${Colors.ANSI_RESET}")
            }
        }
        return pages.sortedByDescending { page -> page.parameters.get("timestamp") as Long }
    }

    // handle this more generically
    private fun shouldSkip(path: String) = path.contains(".DS_Store", true)

}