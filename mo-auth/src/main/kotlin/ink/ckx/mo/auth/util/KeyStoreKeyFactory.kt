package ink.ckx.mo.auth.util

import org.springframework.core.io.Resource
import org.springframework.util.StringUtils
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyStore
import java.security.PublicKey
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.RSAPublicKeySpec

/**
 * @author chenkaixin
 * @description
 * @since 2023/10/19
 */
class KeyStoreKeyFactory @JvmOverloads constructor(
    private val resource: Resource,
    private val password: CharArray,
    private val type: String = type(resource)
) {

    private val lock: Any = Any()
    private var store: KeyStore? = null

    fun getKeyPair(alias: String): KeyPair {
        return this.getKeyPair(alias, password)
    }

    private fun getKeyPair(alias: String, password: CharArray): KeyPair {
        return try {
            synchronized(lock) {
                if (store == null) {
                    synchronized(lock) {
                        store = KeyStore.getInstance(type)
                        resource.inputStream.use { stream -> store!!.load(stream, this.password) }
                    }
                }
            }
            val key = store!!.getKey(alias, password) as RSAPrivateCrtKey
            val certificate = store!!.getCertificate(alias)
            val publicKey: PublicKey?
            if (certificate != null) {
                publicKey = certificate.publicKey
            } else {
                val spec = RSAPublicKeySpec(key.modulus, key.publicExponent)
                publicKey = KeyFactory.getInstance("RSA").generatePublic(spec)
            }
            KeyPair(publicKey, key)
        } catch (var14: Exception) {
            throw IllegalStateException("Cannot load keys from store: $resource", var14)
        }
    }

    companion object {
        private fun type(resource: Resource): String = StringUtils.getFilenameExtension(resource.filename) ?: "jks"
    }
}