package in_.droidcon.india

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}