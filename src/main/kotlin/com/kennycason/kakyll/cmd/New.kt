package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.util.PostDateGenerator
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.nio.charset.Charset
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
        Files.createDirectories(Paths.get(directory.toString(), Structures.Directories.TEMPLATES))
        Files.createDirectories(Paths.get(directory.toString(), Structures.Directories.POSTS))

        // create assets folder
        val assetsPath = Paths.get(directory.toString(), Structures.Directories.ASSETS)
        Files.createDirectories(assetsPath)
        Files.createDirectories(Paths.get(assetsPath.toString(), Structures.Directories.CSS))
        Files.createDirectories(Paths.get(assetsPath.toString(), Structures.Directories.JS))
        Files.createDirectories(Paths.get(assetsPath.toString(), Structures.Directories.IMAGES))

        // copy root files over
        copyResourceToFile(Structures.Files.CONFIG, directory)
        copyResourceToFile(Structures.Files.INDEX, directory)
        copyResourceToFile(Structures.Files.ABOUT, directory)

        // copy template files over
        val templateDirectory = Paths.get(directory.toString(), Structures.Directories.TEMPLATES)
        copyResourceToFile("template/header.hbs", templateDirectory)
        copyResourceToFile("template/footer.hbs", templateDirectory)

        // copy sample post
        val postsDirectory = Paths.get(directory.toString(), Structures.Directories.POSTS).toFile()
        val postContents = IOUtils.toString(javaClass.getResource(Structures.TEMPLATE_RESOURCE_PATH + Structures.Files.SAMPLE_POST), "UTF-8")
        FileUtils.writeStringToFile(
                File(postsDirectory, PostDateGenerator().now() + "-" + Structures.Files.SAMPLE_POST),
                postContents,
                "UTF-8")
    }

    private fun parseDirectory(args: Array<String>): Path {
        if (args.size < 2) {
            throw RuntimeException("Unable to parse directory name. Try: 'hakyll new my_site_directory'")
        }
        return Paths.get(args[1])
    }

    private fun copyResourceToFile(resource: String, destination: Path) {
        val inputUrl = javaClass.getResource(Structures.TEMPLATE_RESOURCE_PATH + resource)
        FileUtils.copyURLToFile(inputUrl, Paths.get(destination.toString(), File(resource).name).toFile())
    }

}