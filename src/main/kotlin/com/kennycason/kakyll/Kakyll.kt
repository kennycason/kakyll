package com.kennycason.kakyll

import com.kennycason.kakyll.cmd.*

/**
 * The primary entry point into Kakyll
 */
fun main(args: Array<String>) {
    Kakyll().run(args)
}

class Kakyll {

    fun run(args: Array<String>) {
        if (args.isEmpty()) {
            throw RuntimeException("Must provide command. Valid commands are 'new', 'clean', 'build', 'serve', 'deploy")
        }
        when (args[0].toLowerCase()) {
            "new" -> New().run(args)
            "clean" -> Clean().run(args)
            "build" -> Build().run(args)
            "serve" -> Serve().run(args)
            "version" -> Version().run(args)
        }

    }

}