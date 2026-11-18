package com.michaeltchuang.cron

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import uniffi.algokit_composer_ffi.CommonTxnParams
import uniffi.algokit_composer_ffi.ComposerParams
import uniffi.algokit_composer_ffi.PaymentParams
import uniffi.algokit_composer_ffi.SuggestedParams
import uniffi.algokit_composer_ffi.TxnParams
import uniffi.algokit_composer_ffi.TxnParamsKind
import uniffi.algokit_composer_ffi.compose
import uniffi.algokit_crypto_ffi.falcon1024AccountFromEntropy
import uniffi.algokit_crypto_ffi.falcon1024AccountFromMnemonic
import uniffi.algokit_crypto_ffi.falcon1024CanonicalAddress
import uniffi.algokit_crypto_ffi.falcon1024PrivateKeySize
import uniffi.algokit_crypto_ffi.falcon1024PublicKeySize
import uniffi.algokit_crypto_ffi.falcon1024RawSign
import uniffi.algokit_crypto_ffi.falcon1024SignatureMaxSize
import uniffi.algokit_crypto_ffi.falcon1024Verify
import uniffi.algokit_crypto_ffi.mnemonicFromSeed
import uniffi.algokit_transact_ffi.PqSignature
import uniffi.algokit_transact_ffi.SignedTransaction
import uniffi.algokit_transact_ffi.decodeSignedTransaction
import uniffi.algokit_transact_ffi.decodeTransaction
import uniffi.algokit_transact_ffi.encodeSignedTransaction
import uniffi.algokit_transact_ffi.encodeTransaction
import uniffi.algokit_transact_ffi.encodeTransactionRaw
import uniffi.algokit_transact_ffi.getTransactionId
import uniffi.algokit_transact_ffi.pqSchemeFalcon1024
import uniffi.algokit_transact_ffi.publicKeyFromAddress
import java.security.MessageDigest

class Falcon1024Tests {
    // Public go-algorand known-answer fixture: entropy bytes 1..32. Never fund this account.
    private val falconTestEntropy = ByteArray(32) { (it + 1).toByte() }
    private val falconTestAddress = "ZEJ4BLG3XWAUUZQGCEDJLYIC6D2NCWHRSX5DJMDPE54PXXR7G3PCQTARXU"

    @Test
    fun falcon1024AccountRecoveryMatchesKnownAnswer() {
        val account = falcon1024AccountFromEntropy(falconTestEntropy)
        val recovered = falcon1024AccountFromMnemonic(mnemonicFromSeed(falconTestEntropy))
        val canonical = falcon1024CanonicalAddress(account.publicKey)

        assertEquals(falconTestAddress, account.addressString)
        assertTrue("Exercise non-zero address salt", account.salt.toInt() > 0)
        val addressPreimage =
            "PQA".toByteArray(Charsets.UTF_8) +
                pqSchemeFalcon1024() + byteArrayOf(account.salt.toByte()) + account.publicKey
        assertArrayEquals(account.address, MessageDigest.getInstance("SHA-512/256").digest(addressPreimage))
        assertEquals(1793, account.publicKey.size)
        assertEquals(2305, account.privateKey.size)
        assertEquals(falcon1024PublicKeySize().toInt(), account.publicKey.size)
        assertEquals(falcon1024PrivateKeySize().toInt(), account.privateKey.size)
        assertArrayEquals(account.publicKey, recovered.publicKey)
        assertArrayEquals(account.privateKey, recovered.privateKey)
        assertArrayEquals(account.address, recovered.address)
        assertEquals(account.addressString, recovered.addressString)
        assertEquals(account.salt, recovered.salt)
        assertEquals(account.addressString, canonical.addressString)
        assertEquals(account.salt, canonical.salt)
        assertArrayEquals(account.address, canonical.address)
        assertArrayEquals(account.address, publicKeyFromAddress(account.addressString))
    }

    @Test
    fun falcon1024SigningIsDeterministicAndRejectsTampering() {
        val account = falcon1024AccountFromEntropy(falconTestEntropy)
        val message = "Falcon-1024 offline test".toByteArray(Charsets.UTF_8)
        val signature = falcon1024RawSign(account.privateKey, message)

        assertTrue(signature.isNotEmpty())
        assertTrue(signature.size <= falcon1024SignatureMaxSize().toInt())
        assertArrayEquals(signature, falcon1024RawSign(account.privateKey, message))
        assertTrue(falcon1024Verify(account.publicKey, signature, message))

        val tamperedMessage = message.copyOf().also { it[0] = (it[0].toInt() xor 1).toByte() }
        val tamperedSignature = signature.copyOf().also { it[it.lastIndex] = (it.last().toInt() xor 1).toByte() }
        assertFalse(falcon1024Verify(account.publicKey, signature, tamperedMessage))
        assertFalse(falcon1024Verify(account.publicKey, tamperedSignature, message))
        val otherAccount = falcon1024AccountFromEntropy(ByteArray(32) { 7 })
        assertFalse(falcon1024Verify(otherAccount.publicKey, signature, message))
    }

    @Test
    fun falcon1024PaymentRoundTripsAcrossAlgoKitLibraries() {
        val account = falcon1024AccountFromEntropy(falconTestEntropy)
        val note = "Offline Falcon payment; never broadcast".toByteArray(Charsets.UTF_8)
        val unsignedBytes =
            compose(
                txnParams =
                    listOf(
                        TxnParams(
                            kind = TxnParamsKind.PAYMENT,
                            payment =
                                PaymentParams(
                                    common = CommonTxnParams(sender = account.addressString, note = note),
                                    receiver = account.addressString,
                                    amount = 1234uL,
                                ),
                        ),
                    ),
                composerParams =
                    ComposerParams(
                        suggestedParams =
                            SuggestedParams(
                                fee = 3000uL,
                                flatFee = true,
                                firstRoundValid = 100uL,
                                lastRoundValid = 1100uL,
                                genesisHash = ByteArray(32) { 1 },
                                genesisId = "offline-falcon-test-v1",
                            ),
                    ),
            ).single()
        val transaction = decodeTransaction(unsignedBytes)
        val signingBytes = encodeTransaction(transaction)
        // Falcon signs the domain-separated payload, not bare transaction msgpack.
        assertArrayEquals("TX".toByteArray(Charsets.UTF_8) + encodeTransactionRaw(transaction), signingBytes)
        val signature = falcon1024RawSign(account.privateKey, signingBytes)
        val scheme = pqSchemeFalcon1024()
        assertArrayEquals("f1".toByteArray(Charsets.UTF_8), scheme)
        val signed =
            SignedTransaction(
                transaction = transaction,
                pqSignature =
                    PqSignature(
                        publicKey = account.publicKey,
                        scheme = scheme,
                        signature = signature,
                        salt = account.salt,
                    ),
            )
        val encoded = encodeSignedTransaction(signed)
        val decoded = decodeSignedTransaction(encoded)
        val pqSignature = requireNotNull(decoded.pqSignature)

        assertNull(decoded.signature)
        assertNull(decoded.multisignature)
        assertNull(decoded.logicSignature)
        assertNull(decoded.authAddress)
        assertArrayEquals(account.publicKey, pqSignature.publicKey)
        assertArrayEquals(scheme, pqSignature.scheme)
        assertArrayEquals(signature, pqSignature.signature)
        assertEquals(account.salt, pqSignature.salt)
        assertEquals(falconTestAddress, decoded.transaction.sender)
        assertEquals(falconTestAddress, requireNotNull(decoded.transaction.payment).receiver)
        assertEquals(1234uL, requireNotNull(decoded.transaction.payment).amount)
        assertEquals(3000L, requireNotNull(decoded.transaction.fee).toLong())
        assertEquals(100uL, decoded.transaction.firstValid)
        assertEquals(1100uL, decoded.transaction.lastValid)
        assertArrayEquals(note, decoded.transaction.note)
        assertArrayEquals(unsignedBytes, signingBytes)
        assertArrayEquals(unsignedBytes, encodeTransaction(decoded.transaction))
        assertArrayEquals(encoded, encodeSignedTransaction(decoded))
        assertEquals(getTransactionId(transaction), getTransactionId(decoded.transaction))
        assertTrue(falcon1024Verify(pqSignature.publicKey, pqSignature.signature, encodeTransaction(decoded.transaction)))
        val changedTransaction = decoded.transaction.copy(note = "Tampered payment".toByteArray(Charsets.UTF_8))
        assertFalse(falcon1024Verify(pqSignature.publicKey, pqSignature.signature, encodeTransaction(changedTransaction)))
    }
}
