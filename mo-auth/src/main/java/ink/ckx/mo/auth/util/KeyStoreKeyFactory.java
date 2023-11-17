package ink.ckx.mo.auth.util;

import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * @author chenkaixin
 * @description
 * @since 2023/10/19
 */
public class KeyStoreKeyFactory {

    private final Resource resource;

    private final char[] password;

    private final Object lock;

    private final String type;

    private KeyStore store;

    public KeyStoreKeyFactory(Resource resource, char[] password) {
        this(resource, password, type(resource));
    }

    public KeyStoreKeyFactory(Resource resource, char[] password, String type) {
        this.lock = new Object();
        this.resource = resource;
        this.password = password;
        this.type = type;
    }

    private static String type(Resource resource) {
        String ext = StringUtils.getFilenameExtension(resource.getFilename());
        return ext == null ? "jks" : ext;
    }

    public KeyPair getKeyPair(String alias) {
        return this.getKeyPair(alias, this.password);
    }

    public KeyPair getKeyPair(String alias, char[] password) {
        try {
            synchronized (this.lock) {
                if (this.store == null) {
                    synchronized (this.lock) {
                        this.store = KeyStore.getInstance(this.type);
                        try (InputStream stream = this.resource.getInputStream()) {
                            this.store.load(stream, this.password);
                        }
                    }
                }
            }

            RSAPrivateCrtKey key = (RSAPrivateCrtKey) this.store.getKey(alias, password);
            Certificate certificate = this.store.getCertificate(alias);
            PublicKey publicKey = null;
            if (certificate != null) {
                publicKey = certificate.getPublicKey();
            } else if (key != null) {
                RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
                publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
            }

            return new KeyPair(publicKey, key);
        } catch (Exception var14) {
            throw new IllegalStateException("Cannot load keys from store: " + this.resource, var14);
        }
    }
}