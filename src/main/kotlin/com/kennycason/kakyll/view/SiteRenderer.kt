package com.kennycason.kakyll.view

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.Config
import com.kennycason.kakyll.config.Directory
import com.kennycason.kakyll.util.Colors
import org.apache.commons.io.FileUtils
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class SiteRenderer {

    fun renderAll(copyDirectories: Boolean = true) {
        val config = GlobalContext.config
        val sitePath = Paths.get(Structures.Directories.SITE)
        Files.createDirectories(sitePath)

        renderPages(config, sitePath)

        if (copyDirectories) {
            config.directories.forEach { directory ->
                DirectoryCopier().copy(directory)
            }
        } else {
            renderDirectories(config.directories.filter(Directory::render))
        }

        PostsRenderer().render()
        renderTagPages(cleanFirst = !copyDirectories)
        ImagePageRenderer().render()
    }

    fun renderDynamicContent() {
        renderAll(copyDirectories = false)
    }

    fun renderConfiguredPage(page: Path) {
        val sitePath = Paths.get(Structures.Directories.SITE)
        Files.createDirectories(sitePath)
        renderPage(page, sitePath)

        val config = GlobalContext.config
        if (page == Paths.get(config.tags.template)) {
            renderTagPages(cleanFirst = true)
        }
        if (page.fileName.toString() == Structures.Files.IMAGES) {
            ImagePageRenderer().render()
        }
    }

    fun renderRenderedDirectoryPath(path: Path) {
        val sitePath = Paths.get(Structures.Directories.SITE)
        Files.createDirectories(sitePath)
        val file = path.toFile()
        if (!file.exists()) {
            return
        }
        if (file.isDirectory) {
            file.walkTopDown()
                    .filter { it.isFile }
                    .forEach { renderPage(it.toPath(), sitePath) }
        } else {
            renderPage(path, sitePath)
        }
    }

    private fun renderPages(config: Config, sitePath: Path) {
        config.pages.forEach { page ->
            renderPage(Paths.get(page), sitePath)
        }
    }

    private fun renderDirectories(directories: List<Directory>) {
        directories.forEach { directory ->
            DirectoryCopier().copy(directory)
        }
    }

    private fun renderTagPages(cleanFirst: Boolean) {
        if (cleanFirst) {
            FileUtils.deleteQuietly(Paths.get(Structures.Directories.SITE, "tags").toFile())
        }
        TagPageRenderer().render()
    }

    private fun renderPage(page: Path, sitePath: Path) {
        try {
            SinglePageRenderer().render(page, sitePath)
        } catch (e: RuntimeException) {
            println("${Colors.ANSI_RED}Failed to render page: [$page] due to [${e.message}]${Colors.ANSI_RESET}")
        }
    }
}
