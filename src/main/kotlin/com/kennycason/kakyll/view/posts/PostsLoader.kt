package com.kennycason.kakyll.view.posts

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.Posts
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

    fun load(): List<Page> {
        val config = GlobalContext.config
        val pages = mutableListOf<Page>()
        val postsDir = File(GlobalContext.config.posts.directory)

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
            page.parameters.put("content", page.content)

            pages.add(page)
        }
        return pages
    }

}