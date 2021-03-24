package br.com.zup.pix.controller.request

import br.com.zup.pix.RemovePixKeyRequest
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull

@Introspected
data class RemovePixKeyRequest(

    @NotNull
    val pixId: UUID?,

    @NotNull
    val clientId: UUID?
) {

    fun toGrpcModel(): RemovePixKeyRequest = RemovePixKeyRequest.newBuilder()
        .setPixId(pixId.toString())
        .setClientId(clientId.toString())
        .build()

}