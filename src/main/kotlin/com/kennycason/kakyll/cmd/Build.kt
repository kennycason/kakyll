package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.util.Colors
import com.kennycason.kakyll.view.*
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Clean contents of _site directory then rebuild the site.
 */
class Build : Cmd {

    override fun run(args: Array<String>) {
        println("Building site")

        GlobalContext.load()
        val config = GlobalContext.config

        // first clean
        Clean().run(args)

        // build _site dir
        val sitePath = Paths.get(Structures.Directories.SITE)
        Files.createDirectories(sitePath)

        // copy everything to _site directory
        config.pages.forEach { page ->
            try {
                SinglePageRenderer().render(Paths.get(page), sitePath)
            } catch (e: RuntimeException) {
                println("${Colors.ANSI_RED}Failed to render page: [$page] due to [${e.message}]${Colors.ANSI_RESET}")
            }
        }
        config.directories.forEach { directory ->
            DirectoryCopier().copy(directory)
        }

        PostsRenderer().render()

        TagPageRenderer().render()
    }


    val methods = arrayOf("happens [earlier/later/at the wrong time] every year",
            "drifts out of sync with the [sun/moon/zodiac/atomic clock in Colorado]",
            "drifts out of sync with the [Gregorian/Mayan/lunar/iPhone] calendar",
            "might [not happen/happen twice] this year")
}
