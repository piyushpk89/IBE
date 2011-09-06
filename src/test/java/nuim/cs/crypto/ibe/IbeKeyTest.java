package nuim.cs.crypto.ibe;

import junit.framework.TestCase;

public class IbeKeyTest extends TestCase {
    public void testGetAlgorithm() {
        IbeKey key = new IbeKey();
        assertTrue(key.getAlgorithm() != null
                   && key.getAlgorithm().length() > 0);
    }

    public void testGetEncoded() {
        IbeKey key = new IbeKey();
        assertTrue(key.getEncoded() == null);
    }

    public void testGetFormat() {
        IbeKey key = new IbeKey();
        assertTrue(key.getFormat() == null);
    }
}