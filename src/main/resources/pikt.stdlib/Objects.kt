package pikt.stdlib

/**
 * Sometimes the output code may contain something like this:
 * var x = ...
 * var y = x()
 *
 * This function ignores the redundant empty call.
 */
operator fun <T> T.invoke(): T = this

/**
 * Generic obj + obj operation.
 */
operator fun Any.plus(other: Any): Any {
    return when {
        this is Number && other is Number -> {
            this + other
        }
        this is String || other is String -> {
            this.toString() + other.toString()
        }
        else -> throw RuntimeException("Invalid operation $this + $other")
    }
}

/**
 * Generic obj - obj operation.
 */
operator fun Any.minus(other: Any): Any {
    return when {
        this is Number && other is Number -> {
            this - other
        }
        else -> throw RuntimeException("Invalid operation $this - $other")
    }
}

/**
 * Generic obj * obj operation.
 */
operator fun Any.times(other: Any): Any {
    return when {
        this is Number && other is Number -> {
            this * other
        }
        this is String && other is Number -> {
            this.repeat(other.toInt())
        }
        this is Number && other is String -> {
            other.repeat(this.toInt())
        }
        else -> throw RuntimeException("Invalid operation $this * $other")
    }
}

/**
 * Generic obj / obj operation.
 */
operator fun Any.div(other: Any): Any {
    return when {
        this is Number && other is Number -> {
            this / other
        }
        else -> throw RuntimeException("Invalid operation $this / $other")
    }
}

/**
 * Generic obj % obj operation.
 */
operator fun Any.rem(other: Any): Any {
    return when {
        this is Number && other is Number -> {
            this % other
        }
        else -> throw RuntimeException("Invalid operation $this % $other")
    }
}

/**
 * Generic < <= > >= operation.
 */
operator fun Any.compareTo(other: Any): Int {
    @Suppress("UNCHECKED_CAST")
    return (this as? Comparable<Any>)?.compareTo(other) ?: 0
}