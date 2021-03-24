package br.com.zup.pix.controller

import br.com.zup.pix.FindPixKeyRequest
import br.com.zup.pix.KeyManagerServiceGrpc
import br.com.zup.pix.ListAllPixKeysRequest
import br.com.zup.pix.controller.request.CreatePixKeyRequest
import br.com.zup.pix.controller.request.RemovePixKeyRequest
import br.com.zup.pix.controller.response.ListPixKeysResponse
import br.com.zup.pix.controller.response.PixKeyDetailsResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Controller("/api/v1/pix/keys/{clientId}")
@Validated
class KeyController(
    @Inject private val keyManager: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub
) {

    @Post
    fun create(@QueryValue clientId: UUID, @Body @Valid request: CreatePixKeyRequest): HttpResponse<Any> {

        val response = keyManager.create(request.toGrpcModel())
        val location = HttpResponse.uri("/api/v1/pix/keys/$clientId/${response.pixId}")

        return HttpResponse.created(location)
    }

    @Delete
    fun delete(@QueryValue clientId: UUID, @Body @Valid request: RemovePixKeyRequest): HttpResponse<Any> {
        keyManager.remove(request.toGrpcModel())
        return HttpResponse.ok()
    }

    @Get("/{pixId}")
    fun find(@QueryValue clientId: UUID, @QueryValue pixId: UUID): HttpResponse<Any> {
        val response = keyManager.find(
            FindPixKeyRequest.newBuilder()
                .setPixId(
                    FindPixKeyRequest.FindByPixId.newBuilder()
                        .setClientId(clientId.toString())
                        .setPixId(pixId.toString())
                        .build()
                ).build()
        )
        return HttpResponse.ok<PixKeyDetailsResponse>().body(PixKeyDetailsResponse.of(response))
    }

    @Get
    fun listAll(@QueryValue clientId: UUID): HttpResponse<Any> {
        val response = keyManager.listAllByClient(
            ListAllPixKeysRequest.newBuilder()
                .setClientId(clientId.toString())
                .build()
        )
        return HttpResponse.ok<ListPixKeysResponse>().body(ListPixKeysResponse.of(response))
    }

}