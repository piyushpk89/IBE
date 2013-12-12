package nuim.cs.crypto.md;

import junit.framework.TestCase;

public class XORMessageDigestTest extends TestCase {
    public void testConstructor() {
        try {
            new XORMessageDigest(0);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new XORMessageDigest(-1);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        XORMessageDigest md = new XORMessageDigest(1);
        assertTrue(md.digest.length == 1);

        md = new XORMessageDigest(3);
        assertTrue(md.digest.length == 1);

        md = new XORMessageDigest(4);
        assertTrue(md.digest.length == 1);

        md = new XORMessageDigest(8);
        assertTrue(md.digest.length == 1);

        md = new XORMessageDigest(9);
        assertTrue(md.digest.length == 2);
    }

    public void testEngineDigest() {
        XORMessageDigest md = new XORMessageDigest(4);
        assertTrue(md.digest()[0] == 0);

        byte byteArray[] = new byte[2];
        byteArray[0] = 17;
        byteArray[1] = 3;
        md.update(byteArray);
        assertTrue(md.digest().length == 1);

        assertTrue(md.digest()[0] == 18);
    }

    public void testEngineReset() {
        XORMessageDigest md = new XORMessageDigest(4);
        assertTrue(md.digest.length == 1);
        assertTrue(md.nBytes == 0);

        md.engineReset();
        assertTrue(md.digest.length == 1);
        assertTrue(md.nBytes == 0);

        md = new XORMessageDigest(9);
        assertTrue(md.digest.length == 2);
        assertTrue(md.nBytes == 0);

        md.reset();
        assertTrue(md.digest.length == 2);
        assertTrue(md.nBytes == 0);
    }

    public void testEngineUpdate() {
        XORMessageDigest md = new XORMessageDigest(4);
        assertTrue(md.digest[0] == 0);

        byte b = 17;
        md.engineUpdate(b);
        assertTrue(md.digest[0] == 17);

        b = 3;
        md.update(b);
        assertTrue(md.digest[0] == 18);

        md.reset();
        assertTrue(md.digest[0] == 0);

        byte byteArray[] = new byte[2];
        byteArray[0] = 17;
        byteArray[1] = 3;
        md.update(byteArray);
        assertTrue(md.digest[0] == 18);

        md.reset();
        md.update(byteArray, 1, 1);
        assertTrue(md.digest[0] == 3);

        md.reset();
        md.update(byteArray, 0, byteArray.length);
        assertTrue(md.digest[0] == 18);
    }
}