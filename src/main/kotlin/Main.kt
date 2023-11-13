package svcs

import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists

fun main(args: Array<String>) {
    val output = Commands.run(args)

    println(output)
}