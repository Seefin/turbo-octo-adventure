package jca.signatures;

import java.security.*;
import java.util.Arrays;

public class FindlaySignature extends SignatureSpi {
    private PrivateKey privKey;

    private PublicKey pubKey;
    private byte[] data;
    private int written;

    @Override
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        if( this.pubKey == null && this.privKey != null ){
            throw new InvalidKeyException("Already initalised for signing");
        }
        this.pubKey = publicKey;
    }

    @Override
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        if( this.privKey == null && this.pubKey != null ){
            throw new InvalidKeyException("Already initialised for verification");
        }
        this.privKey = privateKey;
    }

    @Override
    protected void engineUpdate(byte b) throws SignatureException {
        if( this.privKey == null && this.pubKey == null ){
            throw new SignatureException("Please initialise me");
        }
        if( data == null ){
            data = new byte[8];
        }
        if( written == data.length - 1){
            data = Arrays.copyOf(data, data.length + 8);
        }
        data[written++] = b;
    }

    @Override
    protected void engineUpdate(byte[] b, int off, int len) throws SignatureException {
        for(int i = off; (i < off + len) && (off + len < b.length); i++){
            engineUpdate(b[i]);
        }
    }

    @Override
    protected byte[] engineSign() throws SignatureException {
        try {
            Signature sig = Signature.getInstance("MD5withRSA");
            sig.initSign(this.privKey);
            sig.update(data);
            return sig.sign();
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureException("Can't initialise ECDSA signer\n"+ e.getMessage(),e);
        } catch (InvalidKeyException e) {
            throw new SignatureException("Can't sign with this key\n"+ e.getMessage(),e);
        }
    }

    @Override
    protected boolean engineVerify(byte[] sigBytes) throws SignatureException {
        try {
            Signature sig = Signature.getInstance("MD5withRSA");
            sig.initVerify(this.pubKey);
            sig.update(data);
            return sig.verify(sigBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureException("Can't initialise ECDSA verifier\n"+ e.getMessage(),e);
        } catch (InvalidKeyException e) {
            throw new SignatureException("Can't verify with this key\n"+ e.getMessage(),e);
        }
    }

    @Override
    protected void engineSetParameter(String param, Object value) throws InvalidParameterException {
        throw new UnsupportedOperationException("Can't engineSetParameter yet");
    }

    @Override
    protected Object engineGetParameter(String param) throws InvalidParameterException {
        throw new UnsupportedOperationException("Can't engineGetParameter yet");
    }
}
