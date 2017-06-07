package com.kennycason.kakyll

import com.kennycason.kakyll.cmd.*

/**
 * The primary entry point into Kakyll
 */
fun main(args: Array<String>) {
    Kakyll().run(args)
}

class Kakyll {

    private val commands = mapOf<String, Cmd>(
            Pair("new", New()),
            Pair("clean", Clean()),
            Pair("build", Build()),
            Pair("serve", Serve()),
            Pair("version", Version())
    )

    fun run(args: Array<String>) {
        if (args.isEmpty()) {
            throw RuntimeException("Must provide command. Valid commands are: ${commands.keys}")
        }
        val command = args[0].toLowerCase()
        if (!commands.containsKey(command)) {
            throw RuntimeException("Unknown command [$command]. Valid commands are: ${commands.keys}")
        }
        commands.get(command)?.run(args)

    }

}