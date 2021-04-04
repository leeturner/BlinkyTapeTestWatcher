fun main(args: Array<String>) {
    val person = Person("Bruce", "Wayne", 55)
    println(person)
    println("Hello ${person.getFullName()}")
}