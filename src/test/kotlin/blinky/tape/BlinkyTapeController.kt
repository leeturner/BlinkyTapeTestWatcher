package blinky.tape

import jssc.SerialPort
import jssc.SerialPortException

class BlinkyTapeController(portName: String = "/dev/tty.usbmodem1412201"): AutoCloseable {

    private val serialPort: SerialPort = SerialPort(portName)

    init {
        serialPort.openPort()
        serialPort.setParams(
            SerialPort.BAUDRATE_256000,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE
        )
    }

    override fun close() {
        this.serialPort.closePort()
    }

    fun renderFrame(frame: BlinkyTapeFrame) {
        // Creates an array big enough to hold each LED color and the terminator.
        val data = ByteArray((frame.lightCount + 1) * 3)
        var offset: Int
        for (led in 0 until frame.lightCount) {
            // 3 bytes, RGB, limited to a maximum of 254 as 255 is special.
            offset = led * 3
            data[offset] = Math.min(frame.getColorOfLight(led).red, 254).toByte()
            data[offset + 1] = Math.min(frame.getColorOfLight(led).green, 254).toByte()
            data[offset + 2] = Math.min(frame.getColorOfLight(led).blue, 254).toByte()
        }

        // The sketch only reads three bytes at a time so send 3 with the final 0xFF
        offset = frame.lightCount * 3
        data[offset] = 0x0
        data[offset + 1] = 0x0
        data[offset + 2] = 0xFF.toByte()
        try {
            serialPort.writeBytes(data)
            Thread.sleep(20)
        } catch (spe: SerialPortException) {
            throw BlinkyTapeException("Couldn't write to serial port: ", spe)
        }
    }

}