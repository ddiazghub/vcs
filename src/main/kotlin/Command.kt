/*
package svcs

import kotlin.io.path.*

class Commands {
    companion object {
        val pwd = Path("").toAbsolutePath()
        val vcs = Vcs(pwd.toString())

        val COMMANDS = Command.values()
            .associateBy { it.commandName }

        fun run(args: Array<String>): String {
            return when (val arg = args.getOrNull(0)) {
                "--help", null -> help()
                else -> when (val command = COMMANDS[arg]) {
                    null -> "'${arg}' is not a command."
                    else -> command.handler(args)
                }
            }
        }

        fun help(): String {
            return "Commands:\n" + COMMANDS.values.joinToString("\n")
        }

        fun config(args: Array<String>): String {
            return when (val name = args.getOrNull(1)) {
                null -> when (val username = vcs.username) {
                    "" -> "Please, tell me who you are."
                    else -> "The username is $username."
                }

                else -> {
                    vcs.config(name)
                    "The username is $name."
                }
            }
        }

        fun add(args: Array<String>): String {
            return when (val filename = args.getOrNull(1)) {
                null -> when (val tracked = vcs.indexFile.readText()) {
                    "" -> Command.Add.description
                    else -> "Tracked files:\n$tracked"
                }

                else -> if (vcs.add(pwd.resolve(filename).toString())) {
                    "The file '$filename' is tracked."
                } else "Can't find '$filename'."
            }
        }

        fun log(args: Array<String>): String {
            return when (val commits = vcs.log) {
                "" -> "No commits yet."
                else -> commits
            }
        }

        fun commit(args: Array<String>): String {
            return if (vcs.commit(args.getOrNull(1) ?: "No message")) {
                "Changes are committed."
            } else "Nothing to commit."
        }

        fun checkout(args: Array<String>): String {
            return when (val commit = args.getOrNull(1)) {
                null -> "Commit id was not passed."
                else -> if (vcs.checkout(commit)) {
                    "Switched to commit $commit."
                } else "Commit does not exist."
            }
        }
    }
}

enum class Command(val commandName: String, val description: String, val handler: (Array<String>) -> String) {
    Config("config", "Get and set a username.", Commands::config),
    Add("add", "Add a file to the index.", Commands::add),
    Log("log", "Show commit logs.", Commands::log),
    Commit("commit", "Save changes.", Commands::commit),
    Checkout("checkout", "Restore a file.", Commands::checkout);

    override fun toString(): String {
        return this.commandName.padEnd(11, ' ') + this.description
    }
}
*/