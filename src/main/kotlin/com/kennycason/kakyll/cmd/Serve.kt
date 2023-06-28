package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.util.FileChangeDetector
import com.kennycason.kakyll.view.GlobalContext
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.ResourceHandler


/**
 * Host current blog on local server
 */
class Serve : Cmd {

    override fun run(args: Array<String>) {
        Build().run(args)

        val config = GlobalContext.config
        println("Starting server on http://localhost:${config.port}")

        System.setProperty("org.eclipse.jetty.LEVEL", "INFO")

        // start parallel thread to watch for changes
        Thread(FileChangeDetector()).start()

        val server = Server()
        val connector = ServerConnector(server)
        connector.port = config.port
        server.addConnector(connector)

        val resourceHandler = ResourceHandler()
        resourceHandler.isDirectoriesListed = true
        resourceHandler.resourceBase =  Structures.Directories.SITE + "/"
        resourceHandler.cacheControl = "no-cache"

        val handlerList = HandlerList()
        handlerList.handlers = arrayOf(resourceHandler)
        server.handler = handlerList
        server.start()
        server.join()
    }

}