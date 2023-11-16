package svcs

import java.io.FileNotFoundException
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.*

/**
 * Esta clase tiene las operaciones del control de versiones.
 * Para crear una instancia de esta clase desde java utilicen:
 *
 * @param root La carpeta en la cual se hará el control de versiones o donde se creará la carpeta vcs
 * @sample Vcs vcs = new Vcs(root);
 */
class Vcs(root: String = "") {
    val root = Path(root).toAbsolutePath();
    val vcsRoot = this.root.resolve(VCS_ROOT)
    val configFile = this.root.resolve(CONFIG_FILE)
    val indexFile = this.root.resolve(INDEX_FILE)
    val logFile = this.root.resolve(LOG_FILE)
    val commitDir = this.root.resolve(COMMIT_DIR)
    val dirs = arrayOf(vcsRoot, commitDir)
    val files = arrayOf(configFile, indexFile, logFile)

    val log: String get() = logFile.readText()

    val commits: List<String>
        get() = commitDir.listDirectoryEntries()
            .sortedBy(Path::getLastModifiedTime)
            .map { it.fileName.name }

    val lastCommits: List<String>
        get() {
            val commits = this.commits

            return when (commits.size) {
                0 -> emptyList()
                1 -> commits.subList(0, 1)
                else -> commits.subList(commits.size - 2, commits.size)
            }
        }

    val username: String get() = configFile.readText();

    init {
        dirs.asSequence()
            .filter { !it.exists() }
            .forEach(Path::createDirectory)

        files.asSequence()
            .filter { !it.exists() }
            .forEach(Path::createFile)
    }

    fun resolve(path: String): Path = root.resolve(path)

    fun config(username: String) = configFile.writeText(username)

    fun add(file: String) {
        val path = Path(file).let {
            if (!it.isAbsolute)
                root.resolve(it)
            else if (!it.startsWith(root))
                throw FileNotInRoot(it)
            else if (!it.exists())
                throw NoSuchFile(it)
            else it
        }

        val files = indexFile.readLines()
        val tracked = if (files.size == 1 && files[0].isEmpty()) mutableSetOf()
        else files.toMutableSet()

        tracked.add(path.toString())
        indexFile.writeText(tracked.joinToString("\n"));
    }

    fun commit(message: String = "No message") {
        if (!hasChanges(lastCommits))
            throw NothingToCommit()

        val author = when (val author = configFile.readText()) {
            "" -> "No author"
            else -> author
        }

        val hash = hashCommit(lastCommits.lastOrNull() ?: "")
        doCommit(hash)
        val commitText = "commit $hash\nAuthor: $author\n$message"

        when (val commits = log) {
            "" -> logFile.writeText(commitText)
            else -> logFile.writeText("$commitText\n\n$commits")
        }
    }

    private fun doCommit(hash: String) {
        val commitDir = commitDir.resolve(hash)
        commitDir.createDirectory()

        for (file in indexFile.readLines()) {
            val filepath = Path(file).relativeTo(root)
            Path(file).treeCopy(commitDir.resolve(filepath))
        }
    }

    fun checkout(commit: String) {
        val checkoutDir = commitDir.resolve(commit)

        if (!checkoutDir.exists())
            throw NoSuchCommit(commit)

        for (file in indexFile.readLines()) {
            val trackedFile = Path(file)
            val fileInCommit = checkoutDir.resolve(trackedFile.relativeTo(root))

            if (fileInCommit.exists())
                fileInCommit.treeCopy(trackedFile)
            else
                trackedFile.toFile().deleteRecursively()
        }
    }
}

fun Path.treeHash(digest: MessageDigest) {
    if (this.exists()) {
        val filename = this.fileName.name.toByteArray()

        if (this.isDirectory()) {
            digest.update(filename)

            for (file in this.listDirectoryEntries())
                file.treeHash(digest)
        } else digest.update(filename + this.readBytes())
    }
}

fun Path.treeCopy(destination: Path) {
    if (this.exists()) {
        this.toFile()
            .copyRecursively(destination.toFile(), true)
    }
}