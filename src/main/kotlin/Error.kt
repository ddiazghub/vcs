package svcs

import java.nio.file.Path

sealed class VcsError(message: String) : Exception(message);

class NoSuchFile(path: Path) : VcsError("The file $path does not exist")
class FileNotInRoot(path: Path) : VcsError("The file $path must be in the vcs root directory to be added")
class NothingToCommit() : VcsError("There is nothing to commit")
class NoSuchCommit(commit: String) : VcsError("The commit $commit does not exist")
