package eu.iamgio.pikt.tests

import eu.iamgio.pikt.compiler.AbstractInterpreter
import eu.iamgio.pikt.compiler.CompilationTarget
import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.image.PiktImage
import eu.iamgio.pikt.properties.ColorsPropertiesRetriever
import eu.iamgio.pikt.properties.PiktProperties
import eu.iamgio.pikt.registerStatements
import java.io.File
import javax.imageio.ImageIO


/**
 * Launcher for Pikt tests.
 *
 * @author Giorgio Garofalo
 */
class PiktTestLauncher {

    private val tempDirectory = File(System.getProperty("java.io.tmpdir") + File.separator + "pikt-test")

    private val properties = PiktProperties(
            source = File(tempDirectory, "ignored"),
            output = "out",
            compilationTargets = emptyList(),
            interpretationTarget = CompilationTarget.JVM,
            jvmCompilerPath = System.getProperty("jvmcompiler"), // Set -Djvmcompiler property before running
            nativeCompilerPath = null,
            colors = ColorsPropertiesRetriever().retrieve()
    )

    init {
        tempDirectory.mkdir()
        registerStatements()
    }

    /**
     * Launches the interpreter and returns all the non-error messages.
     * @param name PNG image name without extension
     * @return non-error lines in order
     */
    fun launch(name: String): List<String> {
        val lines = mutableListOf<String>()

        val image = PiktImage(ImageIO.read(javaClass.getResourceAsStream("/$name.png")), properties.colors)

        val evaluator = Evaluator()
        evaluator.evaluate(image)

        val interpreter = object : AbstractInterpreter(evaluator, properties) {
            override fun printProcessLine(line: String, isError: Boolean) {
                if(isError) {
                    System.err.println(line)
                } else {
                    println("[$name] $line")
                    lines.add(line)
                }
            }
        }

        interpreter.compile()

        return lines
    }
}