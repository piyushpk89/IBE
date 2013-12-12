package nuim.cs.crypto.tender.elgamal.ellipticcurve;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

/**
 * Tests the functionality of the <code>SquareRootModuloP</code> class
 */
public class SquareRootModuloPTest extends TestCase {
    /**
     * Tests the calculate method of SquareRootModuloP
     */
    public void testCalculate() {
        // the prime number
        BigInteger prime = new BigInteger("401");
        FiniteFieldPrime field = new FiniteFieldPrime(prime);
        // the value to find the square root of (modulo p)
        BigInteger value = new BigInteger("186");

        // the square root finder
        SquareRootModuloP squareRoot = new SquareRootModuloP(field);
        BigInteger result = squareRoot.calculate(value);

        // the expected result
        Element expected = field.element(new BigInteger("304"));

        // the tests
        assertTrue(expected.equals(result));

        prime = new BigInteger("2081");
        field = new FiniteFieldPrime(prime);
        value = new BigInteger("302");

        squareRoot = new SquareRootModuloP(field);
        result = squareRoot.calculate(value);

        expected = field.element(new BigInteger("1292"));

        assertTrue(expected.equals(result));
    }
}