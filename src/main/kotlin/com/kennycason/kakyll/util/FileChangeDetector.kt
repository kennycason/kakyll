package com.kennycason.kakyll.util

import com.kennycason.kakyll.cmd.Build
import com.kennycason.kakyll.cmd.Clean
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.AgeFileFilter
import org.apache.commons.io.filefilter.NameFileFilter
import org.apache.commons.io.filefilter.NotFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import java.io.FileFilter
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
import java.nio.file.StandardWatchEventKinds.ENTRY_DELETE
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.concurrent.fixedRateTimer

/**
 * Scan the present working directory
 *
 * TODO handle incremental rebuilds
 * TODO ignore _site/
 * TODO speed up
 */
fun main(args: Array<String>) {
    FileChangeDetector().run()
}

class FileChangeDetector : Runnable {

    override fun run() {
        val currentPath = Paths.get(System.getProperty("user.dir"))
        val directoryWatcher = currentPath.fileSystem.newWatchService()

        registerRecursive(currentPath, directoryWatcher)

        println("Watching [$currentPath] for changes")

        // handle file changes in more simplistic os-agnostic way.
        // file watcher detects changes in directories when file contents change and propagates change oddly.
        // only use file watcher for detecting deletions and new files.
        var lastCheck = System.currentTimeMillis()
        while (true) {
            // check for file modifications
            FileUtils.iterateFilesAndDirs(currentPath.toFile(),
                    TrueFileFilter.INSTANCE,
                    TrueFileFilter.INSTANCE)
                    .forEach { file ->
                        if (file.lastModified() < lastCheck
                                || file.absoluteFile.toString().contains("_site")
                                || file.absoluteFile.toString() == currentPath.toString()) { // sometimes changes are registered to root dir, ignore
                            return@forEach
                        }

                        println("file [${file.absoluteFile}] changed, rebuild site")
                        Build().run(arrayOf())
                    }
            lastCheck = System.currentTimeMillis()

            // check for new/deleted files
            val key = directoryWatcher.take()
            key.pollEvents().forEach { it ->
                val changedPath = it.context() as Path
                if (changedPath.toAbsolutePath().toString().contains("_site")) {
                    return@forEach
                }

                when (it.kind().name()) {
                    "ENTRY_CREATE" -> {
                        println("file [${changedPath.toFile().absoluteFile}] created, rebuild site")
                        Build().run(arrayOf())
                    }
                    "ENTRY_DELETE" -> {
                        println("file [${changedPath.toFile().absoluteFile}] deleted, rebuild site")
                        Build().run(arrayOf())
                    }

                }

            }
            key.reset()
        }
    }

    private fun registerRecursive(root: Path, watchService: WatchService) {
        // register all sub directories
        Files.walkFileTree(root, object : SimpleFileVisitor<Path>() {
            @Throws(IOException::class)
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                dir.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.OVERFLOW,
                        StandardWatchEventKinds.ENTRY_DELETE)
                return FileVisitResult.CONTINUE
            }
        })
    }

}