# Junit Test Watcher API Powered Blinky Tape
This repo demonstrates a [Junit 5](https://junit.org/junit5/) [TestWatcher API](https://junit.org/junit5/docs/5.5.1/api/org/junit/jupiter/api/extension/TestWatcher.html) implementation that hooks up your tests to a BlinkyTape LED strip.

![Junit TestWatcher API BlinkyTape](https://github.com/leeturner/BlinkyTapeTestWatcher/blob/main/images/junit-blinkytape.gif)

## How it works

The `BlinkyTapeTestWatcher` class implements the TestWatcher API and simply tracks the success, failure or whether a test is disabled or not.  It also implements the `AfterAllCallback` interface allows us to override a method called `afterAll` that is executed when all the tests have finished.

<details>
  <summary>Click to see the BlinkyTapeTestWatcher implementation!</summary>

```kotlin
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
```
</details>

The `afterAll` method performs 3 checks:

1) If any of the tests have failed it renders the BlinkyTape as all red
1) If non of the tests have failed but there were disabled tests it renders the BlinkyTape as all orange
1) If all of the tests pass then it renders the BlinkyTape as all green

Most of the code for the actual BlinkyTape integration was taken from [night-fever](https://github.com/leeturner/night-fever).

I am pretty sure that the use case for this is pretty limited :grinning: but if you wanted to give this a go, the repo has a few tests that you can play with:

* [AllSuccessButOneDisabledUnitTest.kt](https://github.com/leeturner/BlinkyTapeTestWatcher/blob/main/src/test/kotlin/AllSuccessButOneDisabledUnitTest.kt)
* [AllSuccessUnitTest.kt](https://github.com/leeturner/BlinkyTapeTestWatcher/blob/main/src/test/kotlin/AllSuccessUnitTest.kt)
* [OneFailedUnitTest.kt](https://github.com/leeturner/BlinkyTapeTestWatcher/blob/main/src/test/kotlin/OneFailedUnitTest.kt)
