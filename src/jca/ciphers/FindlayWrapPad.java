package jca.ciphers;

import javax.crypto.*;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

public class FindlayWrapPad extends CipherSpi {
    private int mode = -1;
    private Key key = null;
    private SecureRandom appRandom = new SecureRandom();

    private Cipher AES;

    @Override
    protected void engineSetMode(String s) throws NoSuchAlgorithmException {
        throw new UnsupportedOperationException("Can't engineSetMode yet");
    }

    @Override
    protected void engineSetPadding(String s) throws NoSuchPaddingException {
        throw new UnsupportedOperationException("Can't engineSetPadding yet");
    }

    @Override
    protected int engineGetBlockSize() {
        throw new UnsupportedOperationException("Can't engineGetBlockSize yet");
    }

    @Override
    protected int engineGetOutputSize(int i) {
        throw new UnsupportedOperationException("Can't engineGetOutputSize yet");
    }

    @Override
    protected byte[] engineGetIV() {
        throw new UnsupportedOperationException("Can't engineGetIV yet");
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        throw new UnsupportedOperationException("Can't engineGetParameters yet");
    }

    @Override
    protected void engineInit(int i, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        this.mode = i;
        this.key = key;
        this.appRandom = secureRandom;
        try {
            this.AES = Cipher.getInstance("AES");
            this.AES.init(this.mode, this.key, this.appRandom);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void engineInit(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        throw new UnsupportedOperationException("Can't engineInit yet");
    }

    @Override
    protected void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        throw new UnsupportedOperationException("Can't engineInit yet");
    }

    @Override
    protected byte[] engineUpdate(byte[] bytes, int i, int i1) {
        return this.AES.update(bytes,i,i1);
    }

    @Override
    protected int engineUpdate(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException {
        throw new UnsupportedOperationException("Can't engineUpdate yet");
    }

    @Override
    protected byte[] engineDoFinal(byte[] bytes, int i, int i1) throws IllegalBlockSizeException, BadPaddingException {
        return this.AES.doFinal(bytes,i,i1);
    }

    @Override
    protected int engineDoFinal(byte[] bytes, int i, int i1, byte[] bytes1, int i2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        throw new UnsupportedOperationException("Can't engineDoFinal yet");
    }
}
