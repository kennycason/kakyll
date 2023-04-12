package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Clean/Remove contents of _site directory
 */
class Clean(private val path: String? = null) : Cmd {

    override fun run(args: Array<String>) {
        when {
            path != null -> cleanPath(path)
            else -> cleanPath(Structures.Directories.SITE)
        }
    }

    private fun cleanPath(pathString: String) {
        val path = Paths.get(pathString)
        if (Files.exists(path)) {
            println("Cleaning $path")
            path.toFile().deleteRecursively()
        }
        else {
            println("$path already clean, doing nothing.")
        }
    }

}
