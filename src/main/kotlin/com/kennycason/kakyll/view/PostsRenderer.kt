package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.ConfigLoader
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Paths

/**
 * Created by kenny on 5/18/17.
 */
class PostsRenderer {
    private val config = ConfigLoader().load()
    private val postsLoader = PostsLoader()

    fun render() {
        println("Writing posts to directory [${config.posts.directory}]")
        val outputDir = File(Structures.Directories.SITE, config.posts.directory)
        outputDir.mkdirs()

        val posts = postsLoader.load()
        val encoding = Charset.forName(config.encoding)

        posts.forEach { post ->
            // output content
            val outputFile = File(outputDir.path, post.parameters.get("file") as String)
            outputFile.writeText(post.content, encoding)
        }
    }

}