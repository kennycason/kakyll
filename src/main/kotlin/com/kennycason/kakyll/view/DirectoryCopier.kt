package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 *
 */
class DirectoryCopier {

    fun copy(directoryName: String) {
        println("└ Copying directory [$directoryName]")

        val directory = File(directoryName)
        if (!directory.exists()) {
            throw RuntimeException("Directory [$directoryName] does not exist.")
        }
        FileUtils.copyDirectory(
                directory,
                File(Structures.Directories.SITE, directoryName))
    }
}