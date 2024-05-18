import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val ports = mapOf(
    8080 to "Hello from 8080",
    8081 to "el puerto 8081",
    8082 to "otro puerto 8082",
    8083 to "otro puerto 8083",
    8084 to "otro puerto 8084"
)

fun main() {
    runBlocking {
        ports.forEach { port ->
            launch {
                embeddedServer(Netty, port = port.key) {
                    module(port.key)
                }.start(wait = false)
            }
        }
    }
}

fun Application.module(port:Int) {
    routing {
        get("/") {
            call.respondText("Hello, world!", ContentType.Text.Plain)
            println("Hello, world! $port")
        }
        get("/hello/{name}") {
            val name = call.parameters["name"]
            call.respondText("Hello, $name!", ContentType.Text.Plain)
            println("Hello, $name! $port")
        }
    }
}