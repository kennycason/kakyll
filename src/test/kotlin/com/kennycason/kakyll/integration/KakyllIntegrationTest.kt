package com.kennycason.kakyll.integration

import com.kennycason.kakyll.cmd.Build
import com.kennycason.kakyll.cmd.New
import com.kennycason.kakyll.Structures
import org.apache.commons.io.FileUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration test for Kakyll that tests the full workflow:
 * 1. Create a new site with 'kakyll new'
 * 2. Build the site with 'kakyll build'
 * 3. Verify the output
 */
class KakyllIntegrationTest {

    private lateinit var tempDir: Path
    private lateinit var siteDir: Path
    private lateinit var originalWorkingDir: String

    @Before
    fun setUp() {
        // Save the original working directory
        originalWorkingDir = System.getProperty("user.dir")

        // Create a temporary directory for testing
        tempDir = Files.createTempDirectory("kakyll-test")

        // Create a site directory inside the temp directory
        siteDir = Paths.get(tempDir.toString(), "sample_site")
    }

    @After
    fun tearDown() {
        // Restore the original working directory
        System.setProperty("user.dir", originalWorkingDir)

        // Clean up the temporary directory
        FileUtils.deleteDirectory(tempDir.toFile())
    }

    @Test
    fun testNewAndBuildCommands() {
        try {
            // Run the 'new' command to create a new site
            val newArgs = arrayOf("new", siteDir.toString())
            New().run(newArgs)

            // Verify that the site directory was created
            assertTrue(Files.exists(siteDir), "Site directory should exist")

            // Verify that essential directories and files were created
            assertTrue(Files.exists(Paths.get(siteDir.toString(), Structures.Directories.TEMPLATES)), "Templates directory should exist")
            assertTrue(Files.exists(Paths.get(siteDir.toString(), Structures.Directories.POSTS)), "Posts directory should exist")
            assertTrue(Files.exists(Paths.get(siteDir.toString(), Structures.Files.CONFIG)), "Config file should exist")

            // Change the working directory to the site directory
            val originalDir = System.getProperty("user.dir")
            System.setProperty("user.dir", siteDir.toString())

            try {
                // Run the 'build' command programmatically by executing the main method
                // This ensures that GlobalContext is initialized in the correct directory
                val buildProcess = ProcessBuilder("java", "-cp", System.getProperty("java.class.path"),
                    "com.kennycason.kakyll.KakyllKt", "build")
                    .directory(siteDir.toFile())
                    .redirectErrorStream(true)
                    .start()

                // Wait for the build process to complete
                val exitCode = buildProcess.waitFor()

                // Read the output for debugging
                val output = buildProcess.inputStream.bufferedReader().readText()
                println("Build process output: $output")

                // Check that the build was successful
                assertEquals(0, exitCode, "Build process should exit with code 0")

                // Verify that the _site directory was created
                val siteOutputDir = Paths.get(siteDir.toString(), Structures.Directories.SITE)
                assertTrue(Files.exists(siteOutputDir), "_site directory should exist")

                // Verify that essential output files were created
                assertTrue(Files.exists(Paths.get(siteOutputDir.toString(), "index.html")), "index.html should exist")
                assertTrue(Files.exists(Paths.get(siteOutputDir.toString(), "about.html")), "about.html should exist")
                assertTrue(Files.exists(Paths.get(siteOutputDir.toString(), "tags.html")), "tags.html should exist")

                // Verify that the posts directory was created in the output
                val postsOutputDir = Paths.get(siteOutputDir.toString(), Structures.Directories.POSTS)
                assertTrue(Files.exists(postsOutputDir), "posts directory should exist in _site")

                // Verify that at least one post was generated
                val postFiles = postsOutputDir.toFile().listFiles { file -> file.name.endsWith(".html") }
                assertTrue(postFiles != null && postFiles.isNotEmpty(), "At least one post should be generated")

                // Read the content of the index.html file
                val indexHtml = FileUtils.readFileToString(Paths.get(siteOutputDir.toString(), "index.html").toFile(), "UTF-8")

                // Print the actual content for debugging
                println("Index HTML content: $indexHtml")

                // Verify that essential HTML elements are present
                assertTrue(indexHtml.contains("<title>"), "Title tag should be present in index.html")
                assertTrue(indexHtml.contains("<a href=\"/index.html\" class=\"navbar-brand\">Kakyll</a>"), "Navbar brand should be present in index.html")
                assertTrue(indexHtml.contains("<li><a href=\"/about.html\">About</a></li>"), "About link should be present in index.html")
                assertTrue(indexHtml.contains("<li><a href=\"/tags.html\">Tags</a></li>"), "Tags link should be present in index.html")

                // Read the content of the first post
                val postFile = postFiles!!.first()
                val postHtml = FileUtils.readFileToString(postFile, "UTF-8")

                // Verify that the post contains the expected content
                assertTrue(postHtml.contains("Welcome To Kakyll"), "Post should contain the title")
                assertTrue(postHtml.contains("Mr. Foo"), "Post should contain the author")

                // Verify that the post contains the main image
                assertTrue(postHtml.contains("main_image"), "Post should contain the main image")

                // Verify that the post contains meta tags for social media
                assertTrue(postHtml.contains("<meta property=\"og:title\""), "Post should contain Open Graph title meta tag")
                assertTrue(postHtml.contains("<meta property=\"og:image\""), "Post should contain Open Graph image meta tag")
                assertTrue(postHtml.contains("<meta name=\"twitter:card\""), "Post should contain Twitter card meta tag")
            } finally {
                // Restore the original working directory
                System.setProperty("user.dir", originalDir)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
