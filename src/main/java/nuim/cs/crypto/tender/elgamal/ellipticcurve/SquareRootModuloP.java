package nuim.cs.crypto.tender.elgamal.ellipticcurve;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

/**
 * Calculates the square root of a number modulo p (where p is a prime number).
 * This uses the algorithm as detailed in pages 48 - 49 of 2nd edition of Neal
 * Koblitz's A Course in Number Theory and Cryptography
 */
public class SquareRootModuloP {
    /**
     * The prime number which is the upper limit of the finite field
     */
    protected FiniteFieldPrime field;
    /**
     * The quadratic non-residue class containing the functionality required to
     * find the first quadratic non-residue.
     */
    protected QuadraticNonresidues quadraticNonresidues;

    /**
     * Class constructor
     * 
     * @param prime
     *            prime number which is the upper limit of the finite field
     */
    public SquareRootModuloP(FiniteFieldPrime field) {
        if (field == null) {
            throw new NullPointerException("field cannot be null");
        }
        this.field = (FiniteFieldPrime) field.clone();
        quadraticNonresidues = new QuadraticNonresidues(field);
    }

    /**
     * Calculates the square root of a number (modulo p).
     * 
     * @param number
     *            number to find the square root of modulo p
     * @return the square root of the number modulo p
     */
    public BigInteger calculate(BigInteger number) {
        BigInteger prime = field.getChar();

        // alpha = 0
        BigInteger alpha = BigInteger.ZERO;
        // s = prime - 1
        BigInteger s = prime.subtract(BigInteger.ONE);

        /**
         * In order to find p - 1 in the form 2^alpha . s, simply set s = p - 1
         * and alpha = 0 then divide s by 2 and add 1 to alpha until s is an odd
         * number
         */
        while (!s.testBit(0)) { // if the last bit is a 1 then odd number
            s = s.divide(Constant.TWO);
            alpha = alpha.add(BigInteger.ONE);
        }

        // b = n^s modulo p where n is a quadratic non-residue
        BigInteger n = quadraticNonresidues.getFirst();
        BigInteger b = n.modPow(s, prime);

        // r = number^((s+1)/2) mod p
        BigInteger r = number.modPow(s.add(BigInteger.ONE).divide(Constant.TWO),
                                     prime);
        BigInteger numberInverse = number.modInverse(prime);

        BigInteger j = BigInteger.ZERO;

        // find a suitable power b^j where 0 <= j < 2^alpha
        // for( k = 0; k <= (alpha - 2); k++ )
        BigInteger primeLessOne = prime.subtract(BigInteger.ONE);
        for (BigInteger k = BigInteger.ZERO; k.compareTo(alpha.subtract(Constant.TWO)) <= 0; k = k.add(BigInteger.ONE)) {
            // expr = | [  j0 + 2j1 + ... + 2^(k-1)jk-1  ]^2 | 2^(alpha - k - 2)
            //        | [b^                             r]   |
            //        |--------------------------------------|
            //        |    number to find square root of     |
            //
            // expr = |  top   | exponent
            //        |--------|
            //        | bottom |
            //
            BigInteger top = b.modPow(j, prime).multiply(r).modPow(Constant.TWO,
                                                                   prime);
            BigInteger exponent = Constant.TWO.pow(alpha.subtract(k).subtract(Constant.TWO).intValue());
            BigInteger expr = top.multiply(numberInverse).modPow(exponent,
                                                                 prime);

            // if expr = 1, jk = 0
            // else if expr = prime - 1, jk = 1
            if (expr.compareTo(BigInteger.ONE) == 0) {
                // do nothing as we're just adding zero here
                // because j = j + 2^k*jk, but jk = 0 => j = j + 0
            } else if (expr.compareTo(primeLessOne) == 0) {
                j = j.add(Constant.TWO.pow(k.intValue()));
            }
        }
        BigInteger bToPowerOfj = b.modPow(j, prime);
        BigInteger bjr = bToPowerOfj.multiply(r);
        boolean negative = bjr.compareTo(BigInteger.ZERO) < 0;
        BigInteger approxSquareRoot = bToPowerOfj.multiply(r).mod(prime);
        if (negative) {
            approxSquareRoot = approxSquareRoot.subtract(prime);
        }

        return approxSquareRoot;
    }
}