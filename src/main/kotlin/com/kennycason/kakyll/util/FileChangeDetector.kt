package com.kennycason.kakyll.util

import com.kennycason.kakyll.cmd.Build
import com.kennycason.kakyll.cmd.IncrementalBuild
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.TimeUnit

/**
 * Scan the present working directory
 */

class FileChangeDetector : Runnable {
    private val watchedDirectories = mutableMapOf<WatchKey, Path>()

    override fun run() {
        val currentPath = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize()
        val directoryWatcher = currentPath.fileSystem.newWatchService()

        registerRecursive(currentPath, directoryWatcher)

        println("Watching [$currentPath] for changes")

        while (true) {
            val changes = mutableListOf<FileChange>()
            var overflow = collectChanges(directoryWatcher.take(), directoryWatcher, changes)

            var nextKey = directoryWatcher.poll(150, TimeUnit.MILLISECONDS)
            while (nextKey != null) {
                overflow = collectChanges(nextKey, directoryWatcher, changes) || overflow
                nextKey = directoryWatcher.poll(150, TimeUnit.MILLISECONDS)
            }

            if (overflow) {
                println("File watcher overflowed; running full rebuild")
                Build().run(arrayOf())
            } else if (changes.isNotEmpty()) {
                IncrementalBuild().run(changes)
            }
        }
    }

    private fun collectChanges(
        key: WatchKey,
        watchService: WatchService,
        changes: MutableList<FileChange>
    ): Boolean {
        val directory = watchedDirectories[key] ?: return false
        var overflow = false

        key.pollEvents().forEach { event ->
            if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                overflow = true
                return@forEach
            }

            val changedPath = directory.resolve(event.context() as Path).normalize()
            if (shouldSkip(changedPath)) {
                return@forEach
            }

            when (event.kind().name()) {
                "ENTRY_CREATE" -> println("file [$changedPath] created")
                "ENTRY_MODIFY" -> println("file [$changedPath] modified")
                "ENTRY_DELETE" -> println("file [$changedPath] deleted")
            }

            if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE && Files.isDirectory(changedPath)) {
                registerRecursive(changedPath, watchService)
            }

            changes.add(FileChange(changedPath, event.kind()))
        }

        if (!key.reset()) {
            watchedDirectories.remove(key)
        }

        return overflow
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

                val key = dir.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_DELETE)
                watchedDirectories[key] = dir
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
