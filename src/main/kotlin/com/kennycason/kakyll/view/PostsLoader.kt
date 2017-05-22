package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.ConfigLoader
import com.kennycason.kakyll.view.render.PageRendererResolver
import com.kennycason.kakyll.view.render.Page
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Paths

/**
 *
 */
class PostsLoader {
    private val config = ConfigLoader().load()
    private val rendererResolver = PageRendererResolver()

    fun load(): List<Page> {
        val pages = mutableListOf<Page>()
        val postsDir = File(Structures.Directories.POSTS)
        postsDir.walkTopDown().forEach { file ->
            if (file.isDirectory) { return@forEach }

            val encoding = Charset.forName(config.encoding)
            val content = file.readText(encoding)

            val renderer = rendererResolver.resolve(file.toPath())
            val page = renderer.render(content)
            page.parameters.put("url", config.baseUrl + "/posts/" + file.nameWithoutExtension + ".html")
            pages.add(page)
        }

        return pages
    }
}