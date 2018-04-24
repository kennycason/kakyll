package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.exception.KakyllException
import com.kennycason.kakyll.util.FileChangeDetector
import com.kennycason.kakyll.view.GlobalContext
import com.kennycason.kakyll.view.GlobalContext.config
import com.kennycason.kakyll.view.GlobalContext.posts
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils.isBlank
import org.apache.commons.lang3.StringUtils.isNotBlank
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
    private val DEFAULT_PORT = 8080

    override fun run(args: Array<String>) {
        Build().run(args)

        val inetSocketAddress = buildInetSocketAddress()
        println("Starting server on http://${inetSocketAddress.hostName}:${inetSocketAddress.port}")

        System.setProperty("org.eclipse.jetty.LEVEL", "INFO")

        // start parallel thread to watch for changes
        Thread(FileChangeDetector()).start()

        val server = Server(inetSocketAddress)
        val connector = ServerConnector(server)
        server.addConnector(connector)

        val resourceHandler = ResourceHandler()
        resourceHandler.isDirectoriesListed = true
        resourceHandler.resourceBase =  Structures.Directories.SITE + "/"
        resourceHandler.cacheControl = "no-cache"

        val handlerList = HandlerList()
        handlerList.setHandlers(arrayOf(resourceHandler))
        server.setHandler(handlerList)
        server.start()
        server.join()
    }

    private fun buildInetSocketAddress(): InetSocketAddress {
        val config = GlobalContext.config
        val baseUrl = config.baseUrl

        if (isBlank(baseUrl)) {
            return InetSocketAddress("localhost", DEFAULT_PORT)
        }

        val pieces = config.baseUrl.split(':')

        if (pieces.size == 1) {
            return InetSocketAddress(pieces[0], DEFAULT_PORT)
        }

        if (pieces.size == 2) {
            try {
                return InetSocketAddress(pieces[0], Integer.valueOf(pieces[1]))
            }
            catch (e: NumberFormatException) {
                throw KakyllException("Invalid port in base_url: ${baseUrl}")
            }
        }

        throw KakyllException("Invalid base_url: ${baseUrl}")
    }

}