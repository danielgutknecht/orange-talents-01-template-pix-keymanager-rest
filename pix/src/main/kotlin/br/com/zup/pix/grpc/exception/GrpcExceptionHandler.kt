package br.com.zup.pix.grpc.exception

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton

@Singleton
class GrpcExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {

    override fun handle(request: HttpRequest<*>, exception: StatusRuntimeException): HttpResponse<Any> {
        val code = exception.status
        val description = exception.status.description ?: ""

        val (httpStatus, message) = when (code) {
            Status.ALREADY_EXISTS -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, description)
            Status.INVALID_ARGUMENT -> Pair(
                HttpStatus.BAD_REQUEST,
                description.ifBlank { "Request data is invalid" })
            Status.NOT_FOUND -> Pair(HttpStatus.NOT_FOUND, description)
            else -> Pair(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An internal error has occurred. (${code} - $description)"
            )
        }

        return HttpResponse.status<JsonError>(httpStatus).body(JsonError(message))
    }
}