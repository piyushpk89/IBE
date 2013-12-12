package nuim.cs.crypto.tender.elgamal.ellipticcurve;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

/**
 * Tests the functionality of the <code>QuadraticNonresidues</code> class
 */
public class QuadraticNonresiduesTest extends TestCase {
    public void testFind() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("401"));
        QuadraticNonresidues qn = new QuadraticNonresidues(field);
        BigInteger result = qn.getFirst();

        assertTrue(result.compareTo(new BigInteger("3")) == 0);
    }

    /**
     * Tests the find method
     */
    public void testFindPrimeIsFive() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("5"));
        QuadraticNonresidues qn = new QuadraticNonresidues(field);
        BigInteger result = qn.getFirst();

        assertTrue(result.compareTo(new BigInteger("2")) == 0);
    }

    /**
     * Tests the find method
     */
    public void testFindPrimeIsNineteen() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("19"));
        QuadraticNonresidues qn = new QuadraticNonresidues(field);
        BigInteger result = qn.getFirst();

        assertTrue(result.compareTo(new BigInteger("2")) == 0);
    }

    /**
     * Tests the find method
     */
    public void testFindPrimeIsSeven() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("7"));
        QuadraticNonresidues qn = new QuadraticNonresidues(field);
        BigInteger result = qn.getFirst();

        assertTrue(result.compareTo(new BigInteger("3")) == 0);
    }

    /**
     * Tests the find method
     */
    public void testFindPrimeIsSeventeen() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("17"));
        QuadraticNonresidues qn = new QuadraticNonresidues(field);
        BigInteger result = qn.getFirst();

        assertTrue(result.compareTo(new BigInteger("3")) == 0);
    }

    /**
     * Tests the find method
     */
    public void testFindPrimeIsThirteen() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("13"));
        QuadraticNonresidues qn = new QuadraticNonresidues(field);
        BigInteger result = qn.getFirst();

        assertTrue(result.compareTo(new BigInteger("2")) == 0);
    }

    /**
     * Tests the find method
     */
    public void testFindPrimeIsThree() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("3"));
        QuadraticNonresidues qn = new QuadraticNonresidues(field);
        BigInteger result = qn.getFirst();

        assertTrue(result.compareTo(new BigInteger("2")) == 0);
    }
}