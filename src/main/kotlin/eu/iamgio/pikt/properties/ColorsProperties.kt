package eu.iamgio.pikt.properties

import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * Storage for properties, loaded from a .properties file, that define the "keywords" of the language.
 * A standard file can be created by running Pikt with the -createcolors=<name> argument.
 * The fields of this class refer to a hexadecimal color.
 *
 * @param variable define/set variables
 * @param lambda lambda/code blocks open/close values
 * @param boolean boolean values
 * @param operators operators
 * @author Giorgio Garofalo
 */
data class ColorsProperties(
        val variable: String,
        val lambda: LambdaColorsProperties,
        val boolean: BooleanColorsProperties,
        val operators: OperatorColorsProperties
) : Properties

/**
 * Colors scheme for boolean values.
 *
 * @param boolTrue boolean value 'true'
 * @param boolFalse boolean value 'false'
 * @author Giorgio Garofalo
 */
data class BooleanColorsProperties(
        val boolTrue: String,
        val boolFalse: String
): Properties

/**
 * Colors scheme for lambdas.
 *
 * @param open block open
 * @param close block close
 * @author Giorgio Garofalo
 */
data class LambdaColorsProperties(
        val open: String,
        val close: String
): Properties

/**
 * Colors scheme for operators.
 *
 * @param plus +
 * @param minus -
 * @param times *
 * @param divide /
 * @param modulo %
 * @param and &&
 * @param or ||
 * @param equality ==
 * @param inequality !=
 * @param greater >
 * @param greaterOrEquals >=
 * @param less <
 * @param lessOrEquals <=
 * @author Giorgio Garofalo
 */
data class OperatorColorsProperties(
        val plus: String,
        val minus: String,
        val times: String,
        val divide: String,
        val modulo: String,
        val and: String,
        val or: String,
        val equality: String,
        val inequality: String,
        val greater: String,
        val greaterOrEquals: String,
        val less: String,
        val lessOrEquals: String
): Properties

/**
 * Class that parses JVM properties into a [PiktProperties] instance.
 *
 * @author Giorgio Garofalo
 */
class ColorsPropertiesRetriever : PropertiesRetriever<ColorsProperties> {

    /**
     * External properties.
     */
    private val properties = java.util.Properties()

    /**
     * Internal properties used to fill missing properties.
     */
    private val internalProperties = java.util.Properties()

    /**
     * Loads external properties.
     * @param propertiesPath path to the .properties file
     */
    fun loadProperties(propertiesPath: String) {
        properties.load(FileInputStream(propertiesPath))
    }

    /**
     * Gets the value paired with [key] from the external [properties]. If the value is missing, it gets it from [internalProperties].
     * @return corresponding hex value
     */
    fun get(key: String): String {
        return if(key in properties) {
            properties.getProperty(key)
        } else {
            internalProperties.getProperty(key)
        }
    }

    /**
     * Converts values specified by a .properties file to parsed [ColorsProperties].
     * @return parsed properties
     */
    override fun retrieve(): ColorsProperties {
        internalProperties.load(InputStreamReader(javaClass.getResourceAsStream("/properties/colors.properties")))

        return ColorsProperties(
                get("variable"),
                LambdaColorsProperties(
                        get("lambda.open"),
                        get("lambda.close"),
                ),
                BooleanColorsProperties(
                        get("bool.true"),
                        get("bool.false")
                ),
                OperatorColorsProperties(
                        get("op.plus"),
                        get("op.minus"),
                        get("op.times"),
                        get("op.divide"),
                        get("op.modulo"),
                        get("op.and"),
                        get("op.or"),
                        get("op.equality"),
                        get("op.inequality"),
                        get("op.greater"),
                        get("op.greater_or_equals"),
                        get("op.less"),
                        get("op.less_or_equals")
                )
        )
    }
}