package nuim.cs.crypto.tender.elgamal.ellipticcurve;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

/**
 * Tests the functionality of the <code>Legendre</code> class
 */
public class LegendreTest extends TestCase {
    /**
     * Tests the calculate method in Legendre
     */
    public void testDivisible() {
        // a quadratic residue
        BigInteger quadraticResidue = new BigInteger("4");
        // initialise the legendre symbol test
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("8"));
        Legendre legendre = new Legendre(field);
        int result = legendre.calculate(quadraticResidue);

        // check the result
        assertTrue(result == Legendre.DIVISIBLE);
    }

    /**
     * Tests the calculate method in Legendre
     */
    public void testNonResidue() {
        // a quadratic nonresidue
        BigInteger quadraticResidue = new BigInteger("7411");
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("9283"));
        Legendre legendre = new Legendre(field);
        int result = legendre.calculate(quadraticResidue);
        assertTrue(result == Legendre.NONRESIDUE);
    }

    /**
     * Tests the calculate method in Legendre
     */
    public void testResidue() {
        // a quadratic residue
        BigInteger quadraticResidue = new BigInteger("19");
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("101"));
        Legendre legendre = new Legendre(field);
        int result = legendre.calculate(quadraticResidue);
        assertTrue(result == Legendre.RESIDUE);
    }
}