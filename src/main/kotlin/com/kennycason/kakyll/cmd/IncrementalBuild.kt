package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.Directory
import com.kennycason.kakyll.util.FileChange
import com.kennycason.kakyll.view.GlobalContext
import com.kennycason.kakyll.view.SiteRenderer
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds

class IncrementalBuild {
    private val renderableExtensions = setOf("html", "hbs", "handlebars", "md", "markdown")

    fun run(changes: List<FileChange>) {
        if (changes.isEmpty()) {
            return
        }

        val root = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize()
        val normalizedChanges = changes
                .map { it.copy(path = normalize(root, it.path)) }
                .filterNot { shouldSkip(root, it.path) }
                .distinct()

        if (normalizedChanges.isEmpty()) {
            return
        }

        println("Incremental rebuild for ${normalizedChanges.size} change(s)")

        if (normalizedChanges.any { isConfigChange(root, it.path) }) {
            println("└ Config changed; running full rebuild")
            Build().run(arrayOf())
            return
        }

        val config = GlobalContext.config
        val staticDirectories = config.directories.filterNot(Directory::render)
        val renderedDirectories = config.directories.filter(Directory::render)

        normalizedChanges
                .filter { isInDirectory(root, it.path, staticDirectories) }
                .forEach { applyStaticChange(root, it) }

        val dynamicChanges = normalizedChanges.filter { change ->
            isPostChange(root, change.path)
                    || isTemplateChange(root, change.path)
        }
        if (dynamicChanges.isNotEmpty()) {
            dynamicChanges
                    .filter { it.kind == StandardWatchEventKinds.ENTRY_DELETE && isPostChange(root, it.path) }
                    .forEach { deleteOutputFile(postOutputPath(root, it.path)) }

            println("└ Posts or templates changed; rerendering generated HTML only")
            GlobalContext.load()
            SiteRenderer().renderDynamicContent()
            return
        }

        normalizedChanges
                .filter { isConfiguredPage(root, it.path) }
                .forEach { applyPageChange(root, it) }

        normalizedChanges
                .filter { isInDirectory(root, it.path, renderedDirectories) }
                .forEach { applyRenderedDirectoryChange(root, it) }
    }

    private fun applyStaticChange(root: Path, change: FileChange) {
        val relativePath = root.relativize(change.path)
        val outputPath = Paths.get(Structures.Directories.SITE).resolve(relativePath)

        if (change.kind == StandardWatchEventKinds.ENTRY_DELETE) {
            deleteOutputFile(outputPath)
            return
        }

        val inputFile = change.path.toFile()
        if (!inputFile.exists()) {
            return
        }

        if (inputFile.isDirectory) {
            println("└ Copying changed static directory [$relativePath]")
            FileUtils.copyDirectory(inputFile, outputPath.toFile())
        } else {
            println("└ Copying changed static file [$relativePath]")
            Files.createDirectories(outputPath.parent)
            FileUtils.copyFile(inputFile, outputPath.toFile())
        }
    }

    private fun applyPageChange(root: Path, change: FileChange) {
        val relativePath = root.relativize(change.path)
        if (change.kind == StandardWatchEventKinds.ENTRY_DELETE) {
            deleteOutputFile(renderedOutputPath(relativePath))
            return
        }

        println("└ Rendering changed page [$relativePath]")
        GlobalContext.load()
        SiteRenderer().renderConfiguredPage(relativePath)
    }

    private fun applyRenderedDirectoryChange(root: Path, change: FileChange) {
        val relativePath = root.relativize(change.path)
        if (change.kind == StandardWatchEventKinds.ENTRY_DELETE) {
            deleteOutputFile(renderedOutputPath(relativePath))
            return
        }

        println("└ Rendering changed file in rendered directory [$relativePath]")
        GlobalContext.load()
        SiteRenderer().renderRenderedDirectoryPath(relativePath)
    }

    private fun deleteOutputFile(path: Path) {
        val file = path.toFile()
        if (!file.exists()) {
            return
        }
        println("└ Deleting output [$path]")
        if (file.isDirectory) {
            FileUtils.deleteDirectory(file)
        } else {
            FileUtils.deleteQuietly(file)
        }
    }

    private fun normalize(root: Path, path: Path): Path {
        val absolutePath = if (path.isAbsolute) path else root.resolve(path)
        return absolutePath.normalize()
    }

    private fun shouldSkip(root: Path, path: Path): Boolean {
        val relativePath = root.relativize(path)
        return relativePath.any { it.toString() == Structures.Directories.SITE }
                || relativePath.any { it.toString() == ".git" }
                || relativePath.fileName?.toString()?.contains(".DS_Store", true) == true
    }

    private fun isConfigChange(root: Path, path: Path) =
            root.relativize(path) == Paths.get(Structures.Files.CONFIG)

    private fun isTemplateChange(root: Path, path: Path): Boolean {
        val relativePath = root.relativize(path)
        return relativePath.startsWith(Paths.get(Structures.Directories.TEMPLATES))
    }

    private fun isPostChange(root: Path, path: Path): Boolean {
        val relativePath = root.relativize(path)
        return relativePath.startsWith(Paths.get(GlobalContext.config.posts.directory))
    }

    private fun isConfiguredPage(root: Path, path: Path): Boolean {
        val relativePath = root.relativize(path)
        return GlobalContext.config.pages
                .map(Paths::get)
                .contains(relativePath)
    }

    private fun isInDirectory(root: Path, path: Path, directories: List<Directory>): Boolean {
        val relativePath = root.relativize(path)
        return directories.any { relativePath.startsWith(Paths.get(it.name)) }
    }

    private fun postOutputPath(root: Path, path: Path): Path {
        val outputFileName = FilenameUtils.removeExtension(path.fileName.toString()) + ".html"
        return Paths.get(
                Structures.Directories.SITE,
                GlobalContext.config.posts.directory,
                outputFileName)
    }

    private fun renderedOutputPath(relativePath: Path): Path {
        if (isRenderable(relativePath)) {
            return Paths.get(
                    Structures.Directories.SITE,
                    FilenameUtils.removeExtension(relativePath.toString()) + ".html")
        }
        return Paths.get(Structures.Directories.SITE).resolve(relativePath)
    }

    private fun isRenderable(path: Path) =
            renderableExtensions.contains(FilenameUtils.getExtension(path.toString()).lowercase())
}
