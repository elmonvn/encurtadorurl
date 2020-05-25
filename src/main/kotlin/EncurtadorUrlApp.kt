package desafio.encurtadorurl

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.google.common.hash.Hashing
import org.apache.commons.validator.routines.UrlValidator
import java.nio.charset.StandardCharsets

class EncurtadorUrlApp

val ctrlr = EncurtadorUrlController()

fun Application.module() {
    try {
        install(DefaultHeaders)
        install(CallLogging)
        install(Routing) {
            get("/list") {
                call.respondText(ctrlr.getAllSavedUrls().toString())
            }

            get("/{hash}") {
                val query = ctrlr.getUrlByHash(call.parameters["hash"].toString())
                if (query != null)
                    call.respondRedirect(query)
                else
                    call.respond(HttpStatusCode.BadRequest)
            }

            post("/{...}") {
                val orig = call.request.uri.substring(1)
                if (UrlValidator(arrayOf("http", "https")).isValid(orig)) {
                    val srv = call.request.origin.scheme + "://" + call.request.host() + ":" + call.request.port() + "/"
                    val id = Hashing.murmur3_32().hashString(orig, StandardCharsets.UTF_8).toString()
                    ctrlr.saveHashFromUrl(id, orig)
                    call.respond(HttpStatusCode.OK, srv + "$id")
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
    catch (ex: Exception) {
        error("Erro ocorreu: " + ex.message)
    }
}

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        8080,
        watchPaths = listOf("EncurtadorUrlAppKt"),
        module = Application::module
    ).start(wait = true)
}
