package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.exception.KakyllException
import com.kennycason.kakyll.util.DateParser
import com.kennycason.kakyll.view.GlobalContext
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Generate a new blog post
 */
class NewPost : Cmd {

    override fun run(args: Array<String>) {
        println("Generating new blog post")

        // parse directory
        if (args.size < 3) {
            println("Unable to create new post. Syntax 'new post name-of-post.markdown")
            return
        }

        GlobalContext.load()
        val config = GlobalContext.config
        val fullPostFileName = buildNewPostFileName(args)

        if (Files.exists(Paths.get(config.posts.directory, fullPostFileName))) {
            throw KakyllException("Post with name $fullPostFileName already exists!")
        }


        // copy templates files over
        val postsDirectory = Paths.get(config.posts.directory).toFile()

        // copy sample post to new template
        val postContents = IOUtils.toString(javaClass.getResource(Structures.TEMPLATE_RESOURCE_PATH + Structures.Files.SAMPLE_POST), "UTF-8")
        FileUtils.writeStringToFile(
                File(postsDirectory, fullPostFileName),
                postContents,
                "UTF-8")

        println("Post created, name: ${Paths.get(config.posts.directory, fullPostFileName)}")
    }

    private fun buildNewPostFileName(args: Array<String>): String {
        val sb = StringBuilder()
        (2 until args.size).forEach { i ->
            sb.append(args[i])
            if (i < args.size - 1) {
                sb.append(' ')
            }
        }
        val postFileName = sb.toString()
                .trim()
                .replace(Regex("^\"|\"$|^'|'$"), "")
                .replace(Regex("[ ]+"), "-")
        if (!postFileName.endsWith(".md") && !postFileName.endsWith(".markdown")) {
            throw KakyllException("New post name missing .md or .markdown extension")
        }
        val fullPostFileName = "${DateParser().now()}-$postFileName"
        println("Normalized post file name is: $fullPostFileName")
        return fullPostFileName
    }

    private fun copyResourceToFile(resource: String, destination: Path) {
        val inputUrl = javaClass.getResource(Structures.TEMPLATE_RESOURCE_PATH + resource)
        FileUtils.copyURLToFile(inputUrl, Paths.get(destination.toString(), File(resource).name).toFile())
    }

}