package br.com.zup.pix.grpc.key

import br.com.zup.pix.KeyManagerServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel

@Factory
class KeyManagerFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    fun getKeyManagerService() = KeyManagerServiceGrpc.newBlockingStub(channel)

}