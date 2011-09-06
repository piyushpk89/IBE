package nuim.cs.crypto.ibe;

import java.security.PublicKey;

public class IbePublicKey extends IbeKey implements PublicKey {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected byte identity[];

    /*
    public IbePublicKey( String identity ) {
        if( identity == null ) {
            throw new NullPointerException( "identity cannot be null" );
        }
        if( identity.length() <= 0 ) {
            throw new IllegalArgumentException(
                "identity must be a string with length greater than zero" );
        }
        this.identity = identity.getBytes();
    }
     */

    public IbePublicKey(byte identity[]) {
        if (identity == null) {
            throw new NullPointerException("identity cannot be null");
        }
        if (identity.length <= 0) {
            throw new IllegalArgumentException(
                                               "identity must be a string with length greater than zero");
        }
        this.identity = identity;
    }

    public byte[] getIdentity() {
        return identity;
    }

    /*
    public String toString() {
        return( new String( identity ) );
    }
     */
}