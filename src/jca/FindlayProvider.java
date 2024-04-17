package jca;

import jca.ciphers.FindlayWrapPad;
import jca.generators.FindlayKey;
import jca.signatures.FindlaySignature;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;

public class FindlayProvider extends Provider {
    public static final String NAME = "FP";
    public static double VERSION = 1.0;
    public static final String INFO = "Test Provider";
    /**
     * Constructs a provider with the specified name, version number,
     * and information.
     *
     * @param name    the provider name.
     * @param version the provider version number.
     * @param info    a description of the provider and its services.
     */
    protected FindlayProvider(String name, double version, String info) {
        super(name, version, info);
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            installStuff();
            return null;
        });
    }

    public FindlayProvider() {
        this(FindlayProvider.NAME,FindlayProvider.VERSION,FindlayProvider.INFO);
    }

    private void installStuff(){
        //Should be OK
        put("Signature.Findlay", FindlaySignature.class.getName());
        //Should cry
        put("KeyGenerator.Findlay", FindlayKey.class.getName());
        put("Cipher.Findlay", FindlayWrapPad.class.getName());
    }
}
