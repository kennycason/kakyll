package com.kennycason.kakyll.cmd

import com.fasterxml.jackson.databind.JsonNode
import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.ConfigLoader
import com.kennycason.kakyll.view.DirectoryCopier
import com.kennycason.kakyll.view.PostsRenderer
import com.kennycason.kakyll.view.PageRenderer
import com.kennycason.kakyll.view.render.FlexMarkPageRenderer
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Clean contents of _site directory then rebuild the site.
 */
class Build : Cmd {

    override fun run(args: Array<String>) {
        val config = ConfigLoader().load()

        // first clean
        Clean().run(args)

        // build _site dir
        val sitePath = Paths.get(Structures.Directories.SITE)
        Files.createDirectories(sitePath)

        // copy everything to _site directory
        config.pages.forEach { page ->
            PageRenderer().render(Paths.get(page), sitePath)
        }
        config.directories.forEach { directory ->
            DirectoryCopier().copy(directory)
        }
        PostsRenderer().render()
    }

}
