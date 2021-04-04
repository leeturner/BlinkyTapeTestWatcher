package blinky.tape

import java.awt.Color

data class BlinkyTapeFrame(val lightCount: Int = 60, val defaultColour: Color = Color.BLACK) {

    private var lights = Array(this.lightCount) { defaultColour }

    fun setAllLightsToColor(color: Color) {
        for (i in lights.indices) {
            lights[i] = color
        }
    }

    fun getColorOfLight(lightNumber: Int) = this.lights[lightNumber]
}