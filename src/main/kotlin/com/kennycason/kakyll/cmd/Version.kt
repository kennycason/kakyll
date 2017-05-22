package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.config.ConfigLoader
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 *
 */
class Version : Cmd {

    override fun run(args: Array<String>) {
        println("Kakyll v1.0.0")
    }

}
