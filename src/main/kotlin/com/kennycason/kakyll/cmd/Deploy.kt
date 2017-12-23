package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.view.GlobalContext
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import org.apache.commons.io.FileUtils
import org.eclipse.jetty.server.LocalConnector
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.ResourceHandler
import java.net.InetSocketAddress
import java.io.IOException
import org.eclipse.jetty.server.ServerConnector
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths


/**
 * Execute Deploy command from config
 */
class Deploy : Cmd {

    override fun run(args: Array<String>) {
        val sitePath = Paths.get(Structures.Directories.SITE)
        if (!Files.exists(sitePath)) {
            Build().run(args)
        }

        val config = GlobalContext.config

        println("Executing deploy command [${config.deploy}]")

        // TODO make this not tied to /bin/bash
        // quick hack to work with my rsync command that has wildcard...
        val process = Runtime.getRuntime()
                .exec(arrayOf<String>(
                        "/bin/bash",
                        "-c",
                        config.deploy
                ))

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val errorReader = BufferedReader(InputStreamReader(process.errorStream))

        var line: String?
        do {
            line = reader.readLine()
            if (line == null) { break }
            println(line)

        } while (true)

        do {
            line = errorReader.readLine()
            if (line == null) { break }
            println(line)

        } while (true)
    }


}