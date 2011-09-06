package nuim.cs.crypto.ibe;

import java.security.Provider;

/**
 * This class provides a key pair generator and an El-Gamal based cipher for
 * Identity Based Encryption (IBE).
 */
public class IbeProvider extends Provider {
    /** the name of the cipher and key pair generators for ibe */
    public static final String IBE = new String("ibe");
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of the Crypto Group's provider for IBE.
     */
    public IbeProvider() {
        super("NUIMCSCGIBE", 1.0, "NUIM CS CG IBE Crypto Provider");

        // clears the superclass provider - don't want any surprises
        clear();
        // add in our own implementations
        put("KeyPairGenerator." + IBE, IbeKeyPairGenerator.class.getName());
        put("Cipher." + IBE, IbeCipher.class.getName());
    }
}