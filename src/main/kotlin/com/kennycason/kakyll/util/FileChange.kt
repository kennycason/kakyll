package com.kennycason.kakyll.util

import java.nio.file.Path
import java.nio.file.WatchEvent

data class FileChange(
    val path: Path,
    val kind: WatchEvent.Kind<*>
)
