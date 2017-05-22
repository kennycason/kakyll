package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Constants
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.FileSystem
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Generate a new blog
 */
class New : Cmd {

    override fun run(args: Array<String>) {
        println("Generating new blog")

        // attempt to create directory
        val directory = parseDirectory(args)
        println("Creating directory: " + directory)
        if (Files.exists(directory)) {
            throw RuntimeException("Unable to create blog because directory [$directory] already exists/")
        }
        Files.createDirectories(directory)

        // create base folders
        println("Creating default blog template in directory [$directory]")
        Files.createDirectories(Paths.get(directory.toString(), Constants.Directories.TEMPLATES))
        Files.createDirectories(Paths.get(directory.toString(), Constants.Directories.POSTS))

        // create assets folder
        val assetsPath = Paths.get(directory.toString(), Constants.Directories.ASSETS)
        Files.createDirectories(assetsPath)
        Files.createDirectories(Paths.get(assetsPath.toString(), Constants.Directories.SASS))
        Files.createDirectories(Paths.get(assetsPath.toString(), Constants.Directories.CSS))
        Files.createDirectories(Paths.get(assetsPath.toString(), Constants.Directories.JS))
        Files.createDirectories(Paths.get(assetsPath.toString(), Constants.Directories.IMAGES))
        // copy template files over

        copyResourceToFile("config.yml", directory)
        copyResourceToFile("index.html", directory)
        copyResourceToFile("about.html", directory)
    }

    private fun parseDirectory(args: Array<String>): Path {
        if (args.size < 2) {
            throw RuntimeException("Unable to parse directory name. Try: 'hakyll new my_site_directory'")
        }
        return Paths.get(args[1])
    }

    private fun copyResourceToFile(resource: String, destination: Path) {
        val inputUrl = javaClass.getResource(Constants.TEMPLATE_RESOURCE_PATH + resource)
        FileUtils.copyURLToFile(inputUrl, Paths.get(destination.toString(), resource).toFile())
    }

}