package com.kennycason.kakyll.integration

import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.cmd.Build
import com.kennycason.kakyll.cmd.IncrementalBuild
import com.kennycason.kakyll.cmd.New
import com.kennycason.kakyll.util.FileChange
import org.apache.commons.io.FileUtils
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

        val post = Paths.get(Structures.Directories.POSTS)
                .toFile()
                .listFiles { file -> file.extension == "md" || file.extension == "markdown" }!!
                .first()
                .toPath()
        assertTrue(Paths.get("_site/tags/kakyll.html").toFile().exists())
        val updatedPost = post.toFile().readText()
                .replace("Welcome To Kakyll", "Incremental Kakyll")
                .replace("tags: kakyll, kotlin", "tags: incremental")
        post.toFile().writeText(updatedPost)

        IncrementalBuild().run(listOf(FileChange(post, StandardWatchEventKinds.ENTRY_MODIFY)))

        assertTrue(sentinel.toFile().exists(), "Incremental post rebuild should not clean or recopy static directories")
        assertTrue(Paths.get("_site/index.html").toFile().readText().contains("Incremental Kakyll"))
        assertFalse(Paths.get("_site/tags/kakyll.html").toFile().exists(), "Stale tag pages should be removed")
        assertTrue(Paths.get("_site/tags/incremental.html").toFile().exists(), "New tag pages should be generated")
    }

    private fun staticFileChangeCopiesOnlyChangedFile() {
        val staticFile = Paths.get("assets/incremental-static.txt")
        staticFile.toFile().writeText("copied incrementally")

        IncrementalBuild().run(listOf(FileChange(staticFile, StandardWatchEventKinds.ENTRY_CREATE)))

        val outputFile = Paths.get("_site/assets/incremental-static.txt")
        assertTrue(outputFile.toFile().exists(), "Changed static file should be copied into _site")
        assertTrue(outputFile.toFile().readText().contains("copied incrementally"))
    }
}
