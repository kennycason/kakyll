package com.kennycason.kakyll.util

import com.kennycason.kakyll.cmd.Build
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
class FileChangeDetector : Runnable {

    override fun run() {
        val currentPath = Paths.get(System.getProperty("user.dir"))
        val directoryWatcher = currentPath.fileSystem.newWatchService()
        registerRecursive(currentPath, directoryWatcher)

        println("Watching [$currentPath] for changes")

        while (true) {
            val key = directoryWatcher.take()

            // watch each event on the folder
            if (key.pollEvents().isNotEmpty()) {
                println("Detected changes, rebuild site")
                Build().run(arrayOf())
            }
//            key.pollEvents().forEach { it ->
//                when(it.kind().name()){
//                    "ENTRY_CREATE" -> println("${it.context()} was created")
//                    "ENTRY_MODIFY" -> println("${it.context()} was modified")
//                    "OVERFLOW" -> println("${it.context()} overflow")
//                    "ENTRY_DELETE" -> println("${it.context()} was deleted")
//                    else -> println("${it.context()} unknown")
//                }
//            }
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