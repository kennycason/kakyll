package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.ConfigLoader
import java.io.File
import java.nio.file.Paths

/**
 * Created by kenny on 5/18/17.
 */
class PostsRenderer {
    private val config = ConfigLoader().load()
    private val pageRenderer = PageRenderer()

    fun render() {
        File(Structures.Directories.SITE, Structures.Directories.POSTS).mkdirs()

        val postsDir = File(Structures.Directories.POSTS)
        postsDir.walkTopDown().forEach { file ->
            if (file.isDirectory) { return@forEach }
            pageRenderer.render(file.toPath(), Paths.get(Structures.Directories.SITE))
        }

    }
}