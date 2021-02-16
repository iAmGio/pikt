package eu.iamgio.pikt.expression

import eu.iamgio.pikt.image.PixelReader

/**
 * Parses [Expression]s
 *
 * @param reader pixel reader
 * @author Giorgio Garofalo
 */
class ExpressionParser(private val reader: PixelReader) {

    /**
     * Analizes the next pixels an finds the expression type.
     * @return type of the expression
     */
    private fun analyze(): ExpressionType {
        // Store initial index of the reader in order to roll back after reading the pixels in the expression.
        val startIndex = reader.index

        var type: ExpressionType? = null
        reader.whileNotNull { pixel ->
            type = when {
                // A complex expression must remain complex
                type == ExpressionType.COMPLEX -> {
                    type
                }
                // An expression is a string literal if at least one pixel in it is a non-number character.
                // Every non-character in the string will be treated as a variable.
                pixel.isCharacter && !pixel.isNumber -> {
                    ExpressionType.STRING
                }
                // An expression is a number if every pixel in it is an integer.
                (type == null || type == ExpressionType.NUMBER) && pixel.isCharacter && pixel.isNumber -> {
                    ExpressionType.NUMBER
                }
                // An expression is a boolean if its only pixel matches either bool.true or bool.false
                type == null && pixel.isBoolean -> {
                    ExpressionType.BOOLEAN
                }
                // If the expression is not a string and none of the above match, the expression is complex.
                // Complex expressions need additional evaluations (wip)
                pixel.operator != null -> {
                    ExpressionType.COMPLEX
                }
                else -> type
            }
        }

        reader.index = startIndex
        return type ?: ExpressionType.COMPLEX
    }

    /**
     * Analyzes and evaluates raw pixels into an expression.
     * @return parsed expression
     */
    fun eval(): Expression {
        val type = analyze()

        val code = when(type) {
            ExpressionType.STRING -> "\"${nextString()}\""
            ExpressionType.NUMBER -> nextString(requireNumber = true)
            ExpressionType.BOOLEAN -> reader.next()?.booleanContent ?: "false"
            ExpressionType.COMPLEX -> nextComplex()
        }

        return Expression(type, code)
    }

    /**
     * @param requireNumber whether a number is expected
     * @return following string literal containing characters and variables
     */
    private fun nextString(requireNumber: Boolean = false): String {
        val builder = StringBuilder()

        reader.whileNotNull { pixel ->
            builder.append(
                    if(pixel.isCharacter && (!requireNumber || pixel.isNumber)) {
                        pixel.characterContent
                    } else {
                        if(requireNumber) {
                            System.err.println("member not expected while parsing number.")
                            ""
                        } else {
                            "\${$pixel}"
                        }
                    }
            )
        }

        return builder.toString()
    }

    /**
     * Splits a complex expression into simple expressions.
     * @return subdivision of the complex expression into minor expressions and operators
     */
    private fun splitComplex(): List<ExpressionMember> {
        val members = mutableListOf<ExpressionMember>()

        var startIndex = reader.index

        while(true) {
            val pixel = reader.next()
            val operator = pixel?.operator

            if(startIndex > 0 && (pixel == null || operator != null)) {
                members += ExpressionParser(reader.sliced(startIndex, reader.index - 1).also { it.next() }).eval()
                startIndex = reader.index

                if(operator != null) {
                    members += operator
                }
            }
            if(pixel == null) return members
        }
    }

    /**
     * Reads the following complex expression and converts it to Kotlin code
     * @return Kotlin code of complex expression
     */
    private fun nextComplex(): String {
        return splitComplex().joinToString("") { it.code }
    }
}