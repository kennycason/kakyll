package com.kennycason.kakyll.view

import com.kennycason.kakyll.Constants
import com.kennycason.kakyll.config.ConfigLoader
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
    private val config = ConfigLoader().load()

    fun copy(directoryName: String) {
        val directory = File(directoryName)
        if (!directory.exists()) {
            throw RuntimeException("Directory [$directoryName] does not exist.")
        }
        FileUtils.copyDirectory(
                directory,
                File(Constants.Directories.SITE, directoryName))
    }
}