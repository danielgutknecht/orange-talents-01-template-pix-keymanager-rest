package br.com.zup.pix.controller.request

import br.com.zup.pix.CreatePixKeyRequest
import br.com.zup.pix.controller.shared.AccountType
import br.com.zup.pix.controller.shared.KeyType
import br.com.zup.pix.validator.ValidPixKey
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import br.com.zup.pix.AccountType as GrpcAccountType
import br.com.zup.pix.KeyType as GrpcKeyType

@Introspected
@ValidPixKey
data class CreatePixKeyRequest(

    @field:NotNull
    val clientId: UUID?,

    @field:NotNull
    val keyType: KeyType?,

    @field:NotNull
    val keyValue: String?,

    @field:NotNull
    val accountType: AccountType?
) {
    fun toGrpcModel(): CreatePixKeyRequest = CreatePixKeyRequest.newBuilder()
        .setClientId(clientId!!.toString())
        .setKeyType(GrpcKeyType.valueOf(keyType!!.name))
        .setKeyValue(keyValue)
        .setAccountType(GrpcAccountType.valueOf(accountType!!.name))
        .build()
}