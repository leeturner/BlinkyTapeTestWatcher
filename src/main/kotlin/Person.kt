data class Person(val forename: String, val surname: String, val age: Int) {
    fun getFullName() = "$forename $surname"
}