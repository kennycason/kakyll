package com.kennycason.kakyll.integration

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.cmd.Build
import com.kennycason.kakyll.cmd.IncrementalBuild
import com.kennycason.kakyll.cmd.New
import com.kennycason.kakyll.util.FileChange
import org.apache.commons.io.FileUtils
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IncrementalBuildTest {

    private lateinit var tempDir: Path
    private lateinit var siteDir: Path

    @Before
    fun setUp() {
        tempDir = Files.createTempDirectory("kakyll-incremental-test")
        siteDir = Paths.get(tempDir.toString(), "sample_site")
        New().run(arrayOf("new", siteDir.toString()))
    }

    @After
    fun tearDown() {
        FileUtils.deleteDirectory(tempDir.toFile())
    }

    @Test
    fun pageChangeRendersPageWithoutRecopyingStaticDirectories() {
        runScenario("page")
    }

    @Test
    fun postChangeRerendersGeneratedHtmlWithoutRecopyingStaticDirectories() {
        runScenario("post")
    }

    @Test
    fun postBodyChangeSkipsUnchangedAggregatePages() {
        runScenario("post-body")
    }

    @Test
    fun postImageMetadataChangeRerendersSharedMetadataPages() {
        runScenario("post-images")
    }

    @Test
    fun staticFileChangeCopiesOnlyChangedFile() {
        runScenario("static")
    }

    private fun runScenario(name: String) {
        val java = Paths.get(System.getProperty("java.home"), "bin", "java").toString()
        val process = ProcessBuilder(
                java,
                "-cp",
                System.getProperty("java.class.path"),
                "com.kennycason.kakyll.integration.IncrementalBuildTestRunner",
                name)
                .directory(siteDir.toFile())
                .redirectErrorStream(true)
                .start()
        val output = process.inputStream.bufferedReader().readText()
        val exitCode = process.waitFor()
        assertEquals(0, exitCode, output)
    }
}

object IncrementalBuildTestRunner {

    @JvmStatic
    fun main(args: Array<String>) {
        Build().run(arrayOf("build"))
        when (args[0]) {
            "page" -> pageChangeRendersPageWithoutRecopyingStaticDirectories()
            "post" -> postChangeRerendersGeneratedHtmlWithoutRecopyingStaticDirectories()
            "post-body" -> postBodyChangeSkipsUnchangedAggregatePages()
            "post-images" -> postImageMetadataChangeRerendersSharedMetadataPages()
            "static" -> staticFileChangeCopiesOnlyChangedFile()
            else -> throw IllegalArgumentException("Unknown scenario [${args[0]}]")
        }
    }

    private fun pageChangeRendersPageWithoutRecopyingStaticDirectories() {
        val sentinel = Paths.get("_site/assets/incremental-sentinel.txt")
        sentinel.toFile().writeText("keep me")

        val aboutPage = Paths.get("about.hbs")
        aboutPage.toFile().appendText("\n<p>Incremental page edit</p>\n")

        IncrementalBuild().run(listOf(FileChange(aboutPage, StandardWatchEventKinds.ENTRY_MODIFY)))

        assertTrue(sentinel.toFile().exists(), "Incremental page rebuild should not clean or recopy static directories")
        assertTrue(Paths.get("_site/about.html").toFile().readText().contains("Incremental page edit"))
    }

    private fun postChangeRerendersGeneratedHtmlWithoutRecopyingStaticDirectories() {
        val sentinel = Paths.get("_site/assets/incremental-sentinel.txt")
        sentinel.toFile().writeText("keep me")

        val post = samplePost()
        assertTrue(Paths.get("_site/tags/kakyll.html").toFile().exists())
        val updatedPost = post.toFile().readText()
                .replace("tags: kakyll, kotlin", "tags: incremental")
        post.toFile().writeText(updatedPost)

        val output = captureOutput {
            IncrementalBuild().run(listOf(FileChange(post, StandardWatchEventKinds.ENTRY_MODIFY)))
        }

        assertTrue(sentinel.toFile().exists(), "Incremental post rebuild should not clean or recopy static directories")
        assertFalse(Paths.get("_site/tags/kakyll.html").toFile().exists(), "Stale tag pages should be removed")
        assertTrue(Paths.get("_site/tags/incremental.html").toFile().exists(), "New tag pages should be generated")
        assertTrue(Paths.get("_site/images.html").toFile().readText().contains("/tags/incremental.html"))
        assertTrue(output.contains("Rendering tag pages"), output)
        assertTrue(output.contains("Rendering images page"), output)
    }

    private fun postBodyChangeSkipsUnchangedAggregatePages() {
        val sentinel = Paths.get("_site/assets/incremental-sentinel.txt")
        sentinel.toFile().writeText("keep me")

        val post = samplePost()
        post.toFile().appendText("\nIncremental body edit\n")

        val output = captureOutput {
            IncrementalBuild().run(listOf(FileChange(post, StandardWatchEventKinds.ENTRY_MODIFY)))
        }

        val outputPost = Paths.get("_site", Structures.Directories.POSTS, post.toFile().nameWithoutExtension + ".html")
        assertTrue(sentinel.toFile().exists(), "Incremental post rebuild should not clean or recopy static directories")
        assertTrue(outputPost.toFile().readText().contains("Incremental body edit"))
        assertTrue(output.contains("Rendering changed posts"), output)
        assertTrue(output.contains("Skipping tag cloud page; tag metadata unchanged"), output)
        assertTrue(output.contains("Skipping tag pages; tag metadata unchanged"), output)
        assertTrue(output.contains("Skipping images page; image metadata unchanged"), output)
        assertFalse(output.contains("Rendering tag pages"), output)
        assertFalse(output.contains("Rendering images page"), output)
    }

    private fun postImageMetadataChangeRerendersSharedMetadataPages() {
        val post = samplePost()
        val updatedPost = post.toFile().readText()
                .replace(
                        "main_image: /assets/images/post_main_image.png",
                        "main_image: /assets/images/post_main_image_v2.png")
        post.toFile().writeText(updatedPost)

        val output = captureOutput {
            IncrementalBuild().run(listOf(FileChange(post, StandardWatchEventKinds.ENTRY_MODIFY)))
        }

        assertTrue(Paths.get("_site/images.html").toFile().readText().contains("/assets/images/post_main_image_v2.png"))
        assertTrue(output.contains("Rendering images page"), output)
        assertTrue(output.contains("tags.hbs"), output)
        assertTrue(output.contains("Rendering tag pages"), output)
    }

    private fun staticFileChangeCopiesOnlyChangedFile() {
        val staticFile = Paths.get("assets/incremental-static.txt")
        staticFile.toFile().writeText("copied incrementally")

        IncrementalBuild().run(listOf(FileChange(staticFile, StandardWatchEventKinds.ENTRY_CREATE)))

        val outputFile = Paths.get("_site/assets/incremental-static.txt")
        assertTrue(outputFile.toFile().exists(), "Changed static file should be copied into _site")
        assertTrue(outputFile.toFile().readText().contains("copied incrementally"))
    }

    private fun samplePost(): Path {
        return Paths.get(Structures.Directories.POSTS)
                .toFile()
                .listFiles { file -> file.extension == "md" || file.extension == "markdown" }!!
                .first()
                .toPath()
    }

    private fun captureOutput(block: () -> Unit): String {
        val originalOut = System.out
        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))
        try {
            block()
        } finally {
            System.setOut(originalOut)
        }
        return output.toString(Charsets.UTF_8.name())
    }
}
