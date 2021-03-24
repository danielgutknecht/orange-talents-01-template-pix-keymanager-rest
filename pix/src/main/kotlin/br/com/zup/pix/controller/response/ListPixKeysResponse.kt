package br.com.zup.pix.controller.response

import br.com.zup.pix.ListAllPixKeysReply
import br.com.zup.pix.controller.shared.AccountType
import br.com.zup.pix.controller.shared.KeyType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class ListPixKeysResponse(
    val clientId: UUID,
    val keys: List<PixKey>
) {
    companion object {
        fun of(reply: ListAllPixKeysReply): ListPixKeysResponse = ListPixKeysResponse(
            clientId = UUID.fromString(reply.clientId),
            keys = reply.keysList.map { PixKey.of(it) }
        )
    }
}

data class PixKey(

    val pixId: UUID,
    val keyType: KeyType,
    val keyValue: String,
    val accountType: AccountType,
    val createdAt: LocalDateTime

) {
    companion object {
        fun of(reply: ListAllPixKeysReply.PixKey): PixKey = PixKey(
            pixId = UUID.fromString(reply.pixId),
            keyType = KeyType.valueOf(reply.keyType.name),
            keyValue = reply.keyValue,
            accountType = AccountType.valueOf(reply.accountType.name),
            createdAt = reply.createdAt.let {
                Instant
                    .ofEpochSecond(it.seconds, it.nanos.toLong())
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDateTime()
            }
        )
    }
}