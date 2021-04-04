package blinky

import blinky.tape.BlinkyTapeController
import blinky.tape.BlinkyTapeFrame
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import java.awt.Color
import java.util.*

class BlinkyTapeTestWatcher : TestWatcher, AfterAllCallback {

    private val testResultsStatus = mutableListOf<TestResultStatus>()

    override fun testDisabled(context: ExtensionContext, reason: Optional<String>) {
        this.testResultsStatus.add(TestResultStatus.DISABLED)
    }

    override fun testSuccessful(context: ExtensionContext) {
        this.testResultsStatus.add(TestResultStatus.SUCCESS)
    }

    override fun testAborted(context: ExtensionContext, cause: Throwable?) {
        this.testResultsStatus.add(TestResultStatus.ABORTED)
    }

    override fun testFailed(context: ExtensionContext, cause: Throwable?) {
        this.testResultsStatus.add(TestResultStatus.FAILED)
    }

    override fun afterAll(extensionContext: ExtensionContext) {
        // first lets check to see if we have any test failures so we can leave the blinky tape as red
        if (this.testResultsStatus.any { it == TestResultStatus.FAILED }) {
            this.renderFrame(RED_BLINKY_TAPE_FRAME)
            return
        }

        // if no test failures then we can take a look to see if there are any disabled tests.  If there are then
        // we can set the blinky tape to orange
        if (this.testResultsStatus.any { it == TestResultStatus.DISABLED }) {
            this.renderFrame(ORANGE_BLINKY_TAPE_FRAME)
            return
        }

        // if no failures or disabled tests then we can set the blinky tape to green
        if (this.testResultsStatus.any { it == TestResultStatus.SUCCESS }) {
            this.renderFrame(GREEN_BLINKY_TAPE_FRAME)
            return
        }
    }

    private fun renderFrame(frame: BlinkyTapeFrame) {
        val controller = BlinkyTapeController()
        controller.renderFrame(frame)
        controller.close()
    }

    companion object {
        val ORANGE_BLINKY_TAPE_FRAME = BlinkyTapeFrame(defaultColour = Color.ORANGE)
        val RED_BLINKY_TAPE_FRAME = BlinkyTapeFrame(defaultColour = Color.RED)
        val GREEN_BLINKY_TAPE_FRAME = BlinkyTapeFrame(defaultColour = Color.GREEN)
    }

    private enum class TestResultStatus {
        SUCCESS, ABORTED, FAILED, DISABLED
    }
}