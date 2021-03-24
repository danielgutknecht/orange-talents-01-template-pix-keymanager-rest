package br.com.zup.pix.controller.response

import br.com.zup.pix.FindPixKeyReply
import br.com.zup.pix.controller.shared.AccountType
import br.com.zup.pix.controller.shared.KeyType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class PixKeyDetailsResponse(
    val clientId: UUID,
    val pixId: UUID,
    val key: PixKeyDetails
) {
    companion object {
        fun of(reply: FindPixKeyReply): PixKeyDetailsResponse = PixKeyDetailsResponse(
            clientId = UUID.fromString(reply.clientId),
            pixId = UUID.fromString(reply.pixId),
            key = PixKeyDetails.of(reply.key)
        )
    }
}

data class PixKeyDetails(
    val keyType: KeyType,
    val keyValue: String,
    val createdAt: LocalDateTime,
    val account: BankAccountDetails
) {
    companion object {
        fun of(reply: FindPixKeyReply.PixKey): PixKeyDetails = PixKeyDetails(
            keyType = KeyType.valueOf(reply.keyType.name),
            keyValue = reply.keyValue,
            createdAt = reply.createdAt.let {
                Instant
                    .ofEpochSecond(it.seconds, it.nanos.toLong())
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDateTime()
            },
            account = BankAccountDetails.of(reply.account)
        )
    }
}

data class BankAccountDetails(
    val accountType: AccountType,
    val institution: String,
    val agency: String,
    val number: String,
    val owner: AccountOwnerDetails
) {
    companion object {
        fun of(reply: FindPixKeyReply.PixKey.BankAccount): BankAccountDetails = BankAccountDetails(
            accountType = AccountType.valueOf(reply.accountType.name),
            institution = reply.institution,
            agency = reply.agency,
            number = reply.number,
            owner = AccountOwnerDetails(reply.owner.name, reply.owner.cpf)
        )
    }
}

data class AccountOwnerDetails(
    val name: String,
    val cpf: String
)