package eu.iamgio.pikt.tests

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue


class PiktTest {

    private val launcher = PiktTestLauncher()
    private fun launch(name: String) = launcher.launch(name)

    @Test
    fun `sum of 3 integers`() {
        with(launch("int_sum")) {
            assertTrue {
                contains("51")
            }
        }
    }

    @Test
    fun `print each element of an int list`() {
        with(launch("list_foreach")) {
            assertTrue {
                equals(listOf("13", "32", "60"))
            }
        }
    }

    @Test
    fun `print numbers from 0 to 10`() {
        with(launch("range_foreach")) {
            assertTrue {
                equals((0..10).map { it.toString() })
            }
        }
    }
}