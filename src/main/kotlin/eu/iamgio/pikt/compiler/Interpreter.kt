package eu.iamgio.pikt.compiler

import eu.iamgio.pikt.eval.Evaluator
import eu.iamgio.pikt.properties.PiktProperties
import java.io.File

/**
 * Abstract interpreter that allows handling process stream.
 *
 * @param evaluator evaluator containing output code
 * @param properties Pikt properties
 * @author Giorgio Garofalo
 */
abstract class AbstractInterpreter(evaluator: Evaluator, properties: PiktProperties) : AbstractCompiler(evaluator, properties) {

    override val sourceKotlinFile = File(outputFolder, properties.output + ".kts")

    override fun applyEvaluatorSettings() {}

    override fun getTargets() = listOf(properties.interpretationTarget!!)

    override fun onPreCompile(target: CompilationTarget) {}

    override fun generateCommand(target: CompilationTarget): String {
        return target.commandGenerator.generateInterpretCommand(sourceKotlinFile, properties)
    }

    override fun onPostCompile(target: CompilationTarget) {}
}

/**
 * Interprets Kotlin code.
 *
 * @param evaluator evaluator containing output code
 * @param properties Pikt properties
 * @author Giorgio Garofalo
 */
class Interpreter(evaluator: Evaluator, properties: PiktProperties) : AbstractInterpreter(evaluator, properties) {

    override fun printProcessLine(line: String, isError: Boolean) {
        if(isError) {
            System.err.println(line)
        } else {
            println(line)
        }
    }
}