package nuim.cs.crypto.tender.elgamal.ellipticcurve;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

/**
 * The Legendre Symbol is a way of identifying whether or not an integer is a
 * quadratic residue modulo p.
 * 
 * <pre>
 * If *a* is an integer and p is an odd prime (i.e. p > 2).
 * |a|   | 0, if p | a
 * --- = | 1, if a is a quadratic residue in Fp (have square roots)
 * |p|   | -1, if a is a quadratic nonresidue in Fp
 * </pre>
 */
public class Legendre {
    /**
     * <code>calculate</code> method will return this if legendre equal to zero
     */
    public static final int DIVISIBLE = 0;
    /**
     * <code>calculate</code> method will return this if legendre equal to minus
     * one
     */
    public static final int NONRESIDUE = -1;
    /**
     * <code>calculate</code> method will return this if legendre equal to one
     */
    public static final int RESIDUE = 1;

    /**
     * The field to be used in the legendre symbol
     */
    protected FiniteFieldPrime field;

    public Legendre(FiniteFieldPrime field) {
        if (field == null) {
            throw new NullPointerException("field cannot be null");
        }
        this.field = (FiniteFieldPrime) field.clone();
    }

    /**
     * The Legendre symbol can be calculated by using the following formula
     * 
     * |a| (p-1)/2 --- = a mod p |p|
     * 
     * @param value
     *            Value to check to see if it is a quadratic residue modulo p
     * @return whether or not an integer is a quadratic residue modulo p
     */
    public int calculate(BigInteger value) {
        // (a/p) = a^((p-1)/2) mod p where (a/p) is the legendre symbol
        BigInteger exponent = field.getChar().subtract(BigInteger.ONE);
        exponent = exponent.divide(Constant.TWO);

        BigInteger result = value.modPow(exponent, field.getChar());
        if (result.compareTo(BigInteger.ZERO) == 0) {
            return DIVISIBLE;
        } else if (result.compareTo(BigInteger.ONE) == 0) {
            return RESIDUE;
        } else {
            return NONRESIDUE;
        }
    }
}