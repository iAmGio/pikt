package pikt.stdlib

/**
 * Prints a message to the [System.out] stream.
 * @param message text content
 */
fun print(message: Any) {
    kotlin.io.print(message)
}

/**
 * Prints a message to the [System.err] stream.
 * @param message text content
 */
fun printError(message: Any) {
    System.err.println(message)
}