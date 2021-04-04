import blinky.BlinkyTapeTestWatcher
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExtendWith(BlinkyTapeTestWatcher::class)
class OneFailedUnitTest {

    @Test
    internal fun getForename() {
        expectThat(Person("Bruce", "Wayne", 55).forename).isEqualTo("Bruce")
    }

    @Test
    internal fun getSurname() {
        expectThat(Person("Bruce", "Wayne", 55).surname).isEqualTo("Wayne")
    }

    @Test
    internal fun failedTest() {
        expectThat(Person("Bruce", "Wayne", 55).getFullName()).isEqualTo("Batman'")
    }
}