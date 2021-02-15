package eu.iamgio.pikt.image

import eu.iamgio.pikt.properties.ColorsProperties
import eu.iamgio.pikt.statement.Statement
import eu.iamgio.pikt.statement.Statements
import java.awt.Color

/**
 * Represents a single pixel of a [PiktImage]
 *
 * @param color AWT color of the pixel
 * @author Giorgio Garofalo
 */
class Pixel(private val color: Color) {

    /**
     * [color] as hexadecimal.
     */
    val hex: String = Integer.toHexString(color.rgb).substring(2).toUpperCase()

    /**
     * Whether this pixel is a whitespace (either white or non-opaque), hence should be skipped.
     */
    val isWhitespace: Boolean
        get() = color.rgb == -1 || color.alpha != 255

    /**
     * Whether this pixel is a string character (grayscale 1-255).
     */
    val isCharacter: Boolean
        get() = !isWhitespace && color.red == color.green && color.green == color.blue

    /**
     * Whether this pixel is a numeric character (grayscale 48-57).
     */
    val isNumber: Boolean
        get() = isCharacter && color.red >= '0'.toInt() && color.red <= '9'.toInt()

    /**
     * Character associated to grayscale pixels if [isCharacter] is <tt>true</tt>.
     */
    val characterContent: Char
        get() = color.red.toChar()

    /**
     * @param colors colors scheme
     * @return whether this pixel is a boolean value
     */
    fun isBoolean(colors: ColorsProperties) = matches(colors.boolTrue) || matches(colors.boolFalse)

    /**
     * @return whether the pixel's color matches [hex]
     */
    fun matches(hex: String) = this.hex.equals(hex, ignoreCase = true)

    /**
     * @return statement linked to this pixel if exists. <tt>null</tt> otherwise
     */
    fun getStatement(colors: ColorsProperties): Statement? = Statements.getStatement(this, colors)

    /**
     * @return pixel color as a Kotlin output name
     */
    override fun toString(): String = "`$hex`"
}

/**
 * Represents a collection of [Pixel]s
 *
 * @author Giorgio Garofalo
 */
class PixelArray(private val pixels: Array<Pixel>) {

    /**
     * Size of the array
     */
    val size: Int
        get() = pixels.size

    /**
     * Creates a copy of this array sliced from [start] to [end].
     * @return sliced copy of this array
     */
    fun sliced(start: Int, end: Int): PixelArray = PixelArray(pixels.sliceArray(IntRange(start, end)))

    /**
     * Gets a pixel from the array from given index.
     * @param index item index
     * @return pixel by index
     */
    operator fun get(index: Int): Pixel = pixels[index]

    override fun toString() = "PixelArray(size=${pixels.size})"
}