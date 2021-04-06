# Junit Test Watcher API Powered Blinky Tape
This repo demonstrates a [Junit 5](https://junit.org/junit5/) [TestWatcher API](https://junit.org/junit5/docs/5.5.1/api/org/junit/jupiter/api/extension/TestWatcher.html) implementation that hooks up your tests to a BlinkyTape LED strip.

![Junit TestWatcher API BlinkyTape](https://github.com/leeturner/BlinkyTapeTestWatcher/blob/main/images/junit-blinkytape.gif)

## How it works

The `BlinkyTapeTestWatcher` class implements the TestWatcher API and simply tracks the success, failure or whether a test is disabled or not.  It also implements the `AfterAllCallback` interface allows us to override a method called `afterAll` that is executed when all the tests have finished.

The `afterAll` method performs 3 checks:

1) If any of the tests have failed it renders the BlinkyTape as all red
1) If non of the tests have failed but there were disabled tests it renders the BlinkyTape as all orange
1) If all of the tests pass then it renders the BlinkyTape as all green

Most of the code for the actual BlinkyTape integration was taken from [night-fever](https://github.com/leeturner/night-fever).