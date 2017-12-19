package com.kennycason.kakyll.view.posts

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.Posts
import com.kennycason.kakyll.util.DateParser
import com.kennycason.kakyll.view.GlobalContext
import com.kennycason.kakyll.view.render.PageRendererResolver
import com.kennycason.kakyll.view.render.Page
import com.kennycason.kakyll.view.render.TemplateEngineResolver
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Paths

/**
 * load all post data into structure for downstream processing
 */
class PostsLoader {
    private val rendererResolver = PageRendererResolver()
    private val templateEngineResolver = TemplateEngineResolver()
    private val dateParser = DateParser()

    fun load(): List<Page> {
        val config = GlobalContext.config()
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
            val url = config.baseUrl + "/posts/" + file.nameWithoutExtension + ".html"

            page.parameters.put("original_file", file.name)
            page.parameters.put("file", file.nameWithoutExtension + ".html")
            page.parameters.put("url", url)
            page.parameters.put("content", page.content)
            page.parameters.put("date", dateParser.parseToString(file.nameWithoutExtension))
            page.parameters.put("timestamp", dateParser.parse(file.nameWithoutExtension).millis)

            pages.add(page)
        }
        return pages.sortedByDescending { page -> page.parameters.get("timestamp") as Long }
    }

}