package jca;

import jca.ciphers.FindlayWrapPad;
import jca.generators.FindlayKey;
import jca.signatures.FindlaySignature;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;

/** Simple POC class
 * <p>
 * This class shows how one would go about implementing a CryptoProvider in the
 * JCE/JCA.
 *
 * We demonstrate how simple it is to add a signature, cipher, and key generator.
 *
 * This class needs signed by SUN/IBM before it will be accepted by any JVM that
 * they have produced.
 *
 * @author connor
 */
public class FindlayProvider extends Provider {
    /** Name of this provider, used in getInstance calls */
    public static final String NAME = "FP";
    /** Provider version, can be used to provide versioning */
    public static double VERSION = 1.0;
    /** Information about this provider - normally something about why it exists */
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
        /* In a context where the Security Manager is awake and enforcing restrictions,
         * this will allow us to install the custom implementations, provided the
         * access rules allow it.
         */
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            installStuff();
            return null;
        });
    }

    /**
     * Constructs a provider with the default name, version number,
     * and information.
     *
     *      * @param name    the provider name.
     *      * @param version the provider version number.
     *      * @param info    a description of the provider and its services.
     */
    public FindlayProvider() {
        this(FindlayProvider.NAME,FindlayProvider.VERSION,FindlayProvider.INFO);
    }

    /**
     * Install our custom implementations into the JCA.
     *
     * We use my surname as the algorithm name, so that we are sure that we only have this
     * class that provides the classes.
     */
    private void installStuff(){
        //Should be OK
        put("Signature.Findlay", FindlaySignature.class.getName());
        //Should cry
        put("KeyGenerator.Findlay", FindlayKey.class.getName());
        put("Cipher.Findlay", FindlayWrapPad.class.getName());
    }
}
