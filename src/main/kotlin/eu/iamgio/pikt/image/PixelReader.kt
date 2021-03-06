package eu.iamgio.pikt.image

import eu.iamgio.pikt.expression.Expression
import eu.iamgio.pikt.expression.ExpressionParser
import eu.iamgio.pikt.expression.ExpressionType
import eu.iamgio.pikt.properties.ColorsProperties

/**
 * Pixel-by-pixel reader of a [PiktImage]
 *
 * @param pixels collection of [Pixel]s
 * @author Giorgio Garofalo
 */
class PixelReader(private val pixels: PixelArray, val colors: ColorsProperties) {

    /**
     * Current pixel index.
     */
    var index: Int = -1

    /**
     * Amount of pixels.
     */
    val size: Int
        get() = pixels.size

    /**
     * Gets the next non-whitespace pixel available.
     * @return next pixel, <tt>null</tt> if there is none
     */
    fun next(): Pixel? {
        index++
        if(index >= pixels.size) return null

        return pixels[index].let { pixel ->
            if(pixel.isWhitespace) {
                next()
            } else {
                pixel
            }
        }
    }

    /**
     * Executes a task for every non-null pixel
     * @param task task to be executed if the current pixel is not <tt>null</tt>.
     */
    fun whileNotNull(task: (Pixel) -> Unit) {
        var pixel: Pixel? = null
        while(next()?.also { pixel = it } != null) {
            task(pixel!!)
        }
    }

    /**
     * Creates a copy of this reader sliced from [start] to [end].
     * @return sliced copy of this reader
     */
    fun sliced(start: Int, end: Int) = PixelReader(pixels.sliced(start, end), colors)

    /**
     * Subdivides this reader into one minor reader for each statement.
     * @return list of minor readers.
     */
    fun subdivide(): List<PixelReader> {
        val readers = mutableListOf<PixelReader>()

        var startIndex = 0

        while(true) {
            val pixel = next()
            if(startIndex != index && (pixel == null || pixel.hasStatement)) {
                readers += sliced(startIndex, index - 1)
                startIndex = index
            }
            if(pixel == null) return readers
        }
    }

    /**
     * Reads the next expression as Kotlin code, be it a string, a number, a boolean or an object.
     * @return following value
     */
    fun nextExpression(type: ExpressionType? = null): Expression = ExpressionParser(this).eval(type)

    /**
     * Prints an error preceded by a standard prefix
     */
    fun error(message: String) = System.err.println("Error at index $index: $message")
}