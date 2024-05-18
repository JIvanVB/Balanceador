import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

@Serializable
data class PostData(val data: String)

fun main(): Unit = runBlocking {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }
    }

    client.use {
        // Realizar 100 peticiones GET concurrentes
        (0..1000).map { index ->
            async {
                delay(index * 25L)
                val response: HttpResponse = it.get("http://localhost/hello/${LocalDateTime.now()}")
                println("Request #$index: ${response.bodyAsText()}")
            }
        }
        // Esperar a que todas las peticiones terminen
        .awaitAll()
    }
    client.close()
}