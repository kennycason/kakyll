package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.util.FileChangeDetector
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
import java.nio.file.Files
import java.nio.file.Paths


/**
 * Host current blog on local server
 */
class Serve : Cmd {

    override fun run(args: Array<String>) {
        val sitePath = Paths.get(Structures.Directories.SITE)
        if (!Files.exists(sitePath)) {
            println("Building site")
            Build().run(args)
        }

        println("Starting server on http://localhost:8080")

        System.setProperty("org.eclipse.jetty.LEVEL", "INFO")

        // start parallel thread to watch for changes
        Thread(FileChangeDetector()).start()

        val server = Server()
        val connector = ServerConnector(server)
        connector.port = 8080
        server.addConnector(connector)

        val resourceHandler = ResourceHandler()
        resourceHandler.isDirectoriesListed = true
        resourceHandler.resourceBase = Structures.Directories.SITE + "/"
        resourceHandler.cacheControl = "no-cache"

        val handlerList = HandlerList()
        handlerList.setHandlers(arrayOf(resourceHandler))
        server.setHandler(handlerList)
        server.start()
        server.join()

    }


}