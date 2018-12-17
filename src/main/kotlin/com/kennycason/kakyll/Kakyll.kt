package com.kennycason.kakyll

import com.kennycason.kakyll.cmd.*

/**
 * The primary entry point into Kakyll
 */
fun main(args: Array<String>) {
    Kakyll().run(args)
}

class Kakyll {

    private val commands = mapOf(
            Pair("new", New()),
            Pair("clean", Clean()),
            Pair("build", Build()),
            Pair("serve", Serve()),
            Pair("version", Version()),
            Pair("deploy", Deploy())
    )

    fun run(args: Array<String>) {
        if (args.isEmpty()) {
            println("Must provide command. Valid commands are: ${commands.keys}")
            return
        }
        val command = args[0].toLowerCase()
        if (!commands.containsKey(command)) {
            println("Unknown command [$command]. Valid commands are: ${commands.keys}")
            return
        }
        commands[command]?.run(args)
    }

}