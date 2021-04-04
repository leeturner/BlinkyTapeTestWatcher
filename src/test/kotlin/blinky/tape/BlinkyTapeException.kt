package blinky.tape

import java.lang.RuntimeException

class BlinkyTapeException(message: String?, t: Throwable?) : RuntimeException(message, t)