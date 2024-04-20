package io.vigier.cursorpaging.jpa.serial;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.random.RandomGenerator;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Encrypts serialized data using a symmetric encryption.
 */
@Builder
@RequiredArgsConstructor
public class Encrypter {

    public final static String ALGORITHM_KEY = "ChaCha20";
    public final static int IV_BYTES_LENGTH = 12;

    private final static String ALGORITHM = "ChaCha20-Poly1305";

    /**
     * The random generator to use for generating the initial vector (IV). Potentially this can be replaced by a
     * secure-random implementation provided by a cloud provider (e.g. software.amazon.awssdk.services.kms.KmsClient).
     */
    @Builder.Default
    private final RandomGenerator randomGenerator = new SecureRandom( UUID.randomUUID().toString().getBytes() );

    /**
     * The secret key used for encryption and decryption.
     */
    private final SecretKey secret;


    public byte[] encrypt( final byte[] data ) {
        try {
            return doEncrypt( data );
        } catch ( final RuntimeException e ) {
            throw new CryptoException( e.getCause() );
        }
    }

    @SneakyThrows
    private byte[] doEncrypt( final byte[] data ) {
        final var cipher = Cipher.getInstance( ALGORITHM );
        final IvParameterSpec iv = new IvParameterSpec( getIvBytes() );

        cipher.init( Cipher.ENCRYPT_MODE, secret, iv );
        final byte[] encrypted = cipher.doFinal( data );
        return ByteBuffer.allocate( encrypted.length + IV_BYTES_LENGTH ).put( encrypted ).put( iv.getIV() ).array();
    }

    private byte[] getIvBytes() {
        final var iv = new byte[IV_BYTES_LENGTH];
        randomGenerator.nextBytes( iv );
        return iv;
    }

    public byte[] decrypt( final byte[] data ) {
        try {
            return doDecrypt( data );
        } catch ( final RuntimeException e ) {
            throw new CryptoException( e.getCause() );
        }
    }

    @SneakyThrows
    private byte[] doDecrypt( final byte[] data ) {
        final var cipher = Cipher.getInstance( ALGORITHM );
        final IvParameterSpec iv = new IvParameterSpec( data, data.length - IV_BYTES_LENGTH, IV_BYTES_LENGTH );

        cipher.init( Cipher.DECRYPT_MODE, secret, iv );
        return cipher.doFinal( data, 0, data.length - IV_BYTES_LENGTH );
    }

    /**
     * Get an {@linkplain Encrypter} instance using a random secret key. This method can be used if the requests are
     * always routed to the same handling-instance.
     *
     * @return an {@linkplain Encrypter} instance with a random secret key
     */
    public static Encrypter getInstance() {
        try {
            final KeyGenerator keyGen = KeyGenerator.getInstance( ALGORITHM_KEY );
            keyGen.init( 256, SecureRandom.getInstanceStrong() );
            return Encrypter.builder().secret( keyGen.generateKey() )
                    .build();
        } catch ( final NoSuchAlgorithmException e ) {
            throw new CryptoException( e );
        }
    }

    /**
     * Get an {@linkplain Encrypter} instance using the given key. This method is recommended for web-applications,
     * running in multiple instances behind a load balancer, where the request may be routed to different instances. In
     * such a case the secret should be the same on all instances (e.g. distributed via a Secret Manager, as
     * configuration).
     *
     * @param key the secret key to use for encryption/decryption, must be of algorithm {@value ALGORITHM_KEY}
     * @return an {@linkplain Encrypter} instance
     */
    public static Encrypter getInstance( final SecretKey key ) {
        if ( !key.getAlgorithm().equals( ALGORITHM_KEY ) ) {
            throw new IllegalArgumentException(
                    "Invalid key algorithm '%s', should be '%s'".formatted( key.getAlgorithm(), ALGORITHM_KEY ) );
        }
        return Encrypter.builder().secret( key )
                .build();
    }
}
