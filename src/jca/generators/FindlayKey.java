package jca.generators;

import javax.crypto.KeyGenerator;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class FindlayKey extends KeyGeneratorSpi {
    private SecureRandom appRandom = new SecureRandom();
    private int keySize = 128;
    @Override
    protected void engineInit(SecureRandom secureRandom) {
        this.appRandom = secureRandom;
    }

    @Override
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new UnsupportedOperationException("Can't engineInit yet");
    }

    @Override
    protected void engineInit(int i, SecureRandom secureRandom) {
        this.keySize = i;
        engineInit(secureRandom);
    }

    @Override
    protected SecretKey engineGenerateKey() {
        try {
            KeyGenerator aes = KeyGenerator.getInstance("AES");
            aes.init(this.keySize,this.appRandom);
            return aes.generateKey();
        }  catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
