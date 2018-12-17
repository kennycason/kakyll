package com.kennycason.kakyll.util

import com.kennycason.kakyll.cmd.Build
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

/**
 * Scan the present working directory
 *
 * TODO handle incremental rebuilds
 */

class FileChangeDetector : Runnable {

    override fun run() {
        val currentPath = Paths.get(System.getProperty("user.dir"))
        val directoryWatcher = currentPath.fileSystem.newWatchService()

        registerRecursive(currentPath, directoryWatcher)

        println("Watching [$currentPath] for changes")

        while (true) {
            val key = directoryWatcher.take()
            var changed = false
            key.pollEvents().forEach { it ->
                val changedPath = it.context() as Path
                if (shouldSkip(changedPath)) {
                    return@forEach
                }

                when (it.kind().name()) {
                    "ENTRY_CREATE" -> println("file [${changedPath.toFile().absoluteFile}] created")
                    "ENTRY_MODIFY" -> println("file [${changedPath.toFile().absoluteFile}] modified")
                    "ENTRY_DELETE" -> println("file [${changedPath.toFile().absoluteFile}] deleted")
                }
                changed = true
            }
            key.reset()
            if (changed) {
                Build().run(arrayOf())
            }
        }
    }

    private fun registerRecursive(root: Path, watchService: WatchService) {
        // register all sub directories
        Files.walkFileTree(root, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                // println("register -> " + dir)
                if (shouldSkip(dir)) {
                    // println(" - skipping")
                    return FileVisitResult.SKIP_SUBTREE
                }

                dir.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_DELETE)
                return FileVisitResult.CONTINUE
            }
        })
    }

    private fun shouldSkip(path: Path) = shouldSkip(path.toAbsolutePath().toString())

    private fun shouldSkip(path: String) =
            path.contains("_site")
                    || path.contains(".DS_Store", true)
                    || path.contains(".git")
}