package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.Directory
import com.kennycason.kakyll.util.Colors
import org.apache.commons.io.FileUtils
import java.io.File

/**
 *
 */
class DirectoryCopier {

    fun copy(directory: Directory) {
        println("└ Preparing to copy directory [${directory.name}]")
        val directoryFile = File(directory.name)
        if (!directoryFile.exists()) {
            println("${Colors.ANSI_RED}    └ Skipping, directory not found.${Colors.ANSI_RESET}")
            return
        }

        // copy directory as-is without rendering any files
        if (directory.render) {
            println("    └ Rendering each file within directory [${directory.name}]")
            // copy directory and render files if needed
            copyWithRender(directoryFile, File(Structures.Directories.SITE, directory.name), File(Structures.Directories.SITE))
        }
        else {
            println("    └ Copying directory [${directory.name}] as-is")
            FileUtils.copyDirectory(
                    directoryFile,
                    File(Structures.Directories.SITE, directory.name))
        }
    }

    private fun copyWithRender(currentDirectory: File, copyToDirectory: File, baseDirectory: File) {
        copyToDirectory.mkdirs()
        currentDirectory.listFiles().forEach { file ->
            if (file.isDirectory) {
                println("    └ Recursively copying directory [${file.absolutePath}]")
                copyWithRender(file, File(copyToDirectory, file.name), baseDirectory)
            }
            else {
                try {
                    SinglePageRenderer().render(file.toPath(), baseDirectory.toPath())
                } catch (e: RuntimeException) {
                    println("${Colors.ANSI_RED}    └ Failed to render page: [${file.absolutePath}] due to [${e.message}]${Colors.ANSI_RESET}")
                }
            }
        }
    }
}