package nuim.cs.crypto.ibe;

import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import nuim.cs.crypto.blitz.point.AffinePoint;

public class IbeKeyPairGenerator extends KeyPairGeneratorSpi {
    private IbeKeyParameters params;

    /**
     * Generates a key pair. Unless an initialization method is called using a
     * KeyPairGenerator interface, algorithm-specific defaults will be used.
     * This will generate a new key pair every time it is called.
     * 
     * @return the newly generated <tt>KeyPair</tt>
     */
    @Override
    public KeyPair generateKeyPair() {
        byte id[] = params.getPublicKey();
        /*
        MessageDigest hash = params.getHash();
        hash.reset();
        byte identity[] = hash.digest( id.getBytes() );
        BigInteger masterKey = params.getMasterKey();
        BilinearMap map = params.getMap();
        AffinePoint Qid = map.mapToPoint( new BigInteger( 1, identity ) );
        AffinePoint Did = map.getCurve().multiply( masterKey, Qid );
         */
        AffinePoint Did = params.getPrivateKey();

        IbePublicKey publicKey = new IbePublicKey(id);
        IbePrivateKey privateKey = new IbePrivateKey(Did);
        return new KeyPair(publicKey, privateKey);
    }

    public void initialize(AlgorithmParameterSpec params) {
        initialize(params, null);
    }

    @Override
    public void initialize(AlgorithmParameterSpec params, SecureRandom random) {
        if (params == null) {
            throw new NullPointerException("params cannot be null");
        }
        if (!(params instanceof IbeKeyParameters)) {
            throw new IllegalArgumentException(
                                               "params must be an instance of IbeKeyParameters");
        }
        this.params = (IbeKeyParameters) params;
    }

    /**
     * Initializes the key pair generator for a certain keysize, using the
     * default parameter set.
     * 
     * @param keysize
     *            the keysize. This is an algorithm-specific metric, such as
     *            modulus length, specified in number of bits.
     * @param random
     *            the source of randomness for this generator.
     * @exception InvalidParameterException
     *                if the <code>keysize</code> is not supported by this
     *                KeyPairGeneratorSpi object.
     */
    @Override
    public void initialize(int keysize, SecureRandom random) {
        throw new RuntimeException("method not implemented");
    }
}