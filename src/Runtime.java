import jca.FindlayProvider;
import sun.misc.Unsafe;

import javax.crypto.*;
import java.io.IOError;
import java.lang.reflect.Field;
import java.security.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

public class Runtime {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        patchJCESigningOut();
        Security.addProvider(new FindlayProvider());

        SecureRandom secureRandom = new SecureRandom();

        //This is normal
        KeyPair keys = KeyPairGenerator.getInstance("RSA").generateKeyPair();

        //This should not panic
        Signature sig = Signature.getInstance("Findlay","FP");
        sig.initSign(keys.getPrivate());
        sig.update((byte) 0x01);
        byte[] signature = sig.sign();

        Signature verifySig = Signature.getInstance("Findlay", "FP");
        verifySig.initVerify(keys.getPublic());
        verifySig.update((byte) 0x01);
        if( !verifySig.verify(signature) ){
            System.err.println("Signature does not verify, aborting");
            System.exit(1);
        }

        //This should not panic - even though we should have a signed JCE for this
        KeyGenerator gen = KeyGenerator.getInstance("Findlay","FP");
        gen.init(256, secureRandom);
        SecretKey key = gen.generateKey();

        //This should also not panic
        Cipher AESWrapPad = Cipher.getInstance("Findlay","FP");
        AESWrapPad.init(Cipher.ENCRYPT_MODE,key);
        byte[] encrypted = AESWrapPad.doFinal(signature);

        Cipher AESUnwrapPad = Cipher.getInstance("Findlay", "FP");
        AESUnwrapPad.init(Cipher.DECRYPT_MODE,key);
        byte[] unencrypted = AESUnwrapPad.doFinal(encrypted);

        if( ! Arrays.equals(signature, unencrypted) ){
            System.err.println("Round trip encryption test failure, aborting");
            System.exit(1);
        } else {
            System.out.println("Successfully generated key, and encrypted data using the forbidden API");
        }
    }

    private static void patchJCESigningOut() {
        try {
            Class<?> jceSecurityClass = Class.forName("javax.crypto.JceSecurity");
            Field field = jceSecurityClass.getDeclaredField("verificationResults");
            field.setAccessible(true);

            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);

            // This is not the provider you are looking for, you don't need to see its identification, move along
            unsafe.putObject(unsafe.staticFieldBase(field), unsafe.staticFieldOffset(field), new HashMap<Object, Boolean>() {
                @Override
                public Boolean get(Object key) {
                    return Boolean.TRUE;
                }

                @Override
                public Boolean computeIfAbsent(Object key, Function<? super Object, ? extends Boolean> mappingFunction) {
                    return super.computeIfAbsent(key, object -> Boolean.TRUE);
                }
            });
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
