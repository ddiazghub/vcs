package svcs

import kotlin.io.path.Path

val CONFIG_FILE = Path("vcs", "config.txt")
val INDEX_FILE = Path("vcs", "index.txt")
val LOG_FILE = Path("vcs", "log.txt")
val VCS_ROOT = Path("vcs")
val COMMIT_DIR = Path("vcs", "commits")