package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Clean/Remove contents of _site directory
 */
class Clean : Cmd {

    override fun run(args: Array<String>) {
        val sitePath = Paths.get(Structures.Directories.SITE)
        if (Files.exists(sitePath)) {
            println("Cleaning site")
            sitePath.toFile().deleteRecursively()

        } else {
            println("Site already clean, doing nothing.")
        }
    }

}
