package com.kennycason.kakyll.view

import com.kennycason.kakyll.Constants
import com.kennycason.kakyll.config.ConfigLoader
import java.io.File
import java.nio.file.Paths

/**
 * Created by kenny on 5/18/17.
 */
class PostsRenderer {
    private val config = ConfigLoader().load()
    private val singleFileRenderer = PageRenderer()

    fun render() {
        File(Constants.Directories.SITE, Constants.Directories.POSTS).mkdirs()

        val postsDir = File(Constants.Directories.POSTS)
        postsDir.walkTopDown().forEach { file ->
            if (file.isDirectory) { return@forEach }
            singleFileRenderer.render(file.toPath(), Paths.get(Constants.Directories.SITE))
        }

    }
}