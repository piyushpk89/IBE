package nuim.cs.crypto.primality;

import java.math.BigInteger;
import java.util.Random;

/**
 * Implementation of Gordon's algorithm for generating a strong prime. A prime
 * number <b>p</b> is said to be a <i>strong prime</i> if integers <b>r</b>,
 * <b>s</b> and <b>t</b> exist such that the following three conditions are
 * satisfied:
 * <ol>
 * <li><b>p - 1</b> has a large prime factor, denoted <b>r</b></li>
 * <li><b>p + 1</b> has a large prime factor, denoted <b>s</b></li>
 * <li><b>r - 1</b> has a large prime factor, denoted <b>t</b>
 * </ol>
 */
public class Gordon {
    public static final BigInteger RANDOM_INT = new BigInteger("27192");
    public static final BigInteger TWO = new BigInteger("2");

    protected BigInteger p;
    protected BigInteger r;
    protected BigInteger s;
    protected BigInteger t;

    public Gordon() {
        this(10, 16, new Random(), RANDOM_INT, RANDOM_INT);
    }

    public Gordon(int bitLength) {
        this(bitLength, 16, new Random(), RANDOM_INT, RANDOM_INT);
    }

    public Gordon(int bitLength, int certainty, Random rnd, BigInteger i,
                  BigInteger j) {
        if (bitLength <= 0) {
            throw new IllegalArgumentException(
                                               "bitLength must be greater than zero");
        }
        if (certainty <= 0) {
            throw new IllegalArgumentException(
                                               "certainty must be greater than zero");
        }
        if (rnd == null) {
            throw new NullPointerException("rnd cannot be null");
        }
        if (i == null) {
            throw new NullPointerException("i cannot be null");
        }
        if (i.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("i must be greater than zero");
        }
        if (j == null) {
            throw new NullPointerException("j cannot be null");
        }
        if (j.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("j must be greater than zero");
        }

        BigInteger s = new BigInteger(bitLength, certainty, rnd);
        BigInteger t = new BigInteger(bitLength, certainty, rnd);

        /**
         * Select an integer i0. Find the first prime in the sequence 2it + 1,
         * for i = i0, i0 + 1, i0 + 2,... Denote this prime by r = 2it + 1.
         * 2(i0)t + 1 = 2it + 1 2(i0 + 1)t + 1 = 2it + 2t + 1 2(i0 + 2)t + 1 =
         * 2it + 4t + 1 = 2it + 2t + 1 + 2t = 2(i0 + 1)t + 1 + 2t = previous
         * result + 2t
         */
        BigInteger r = TWO.multiply(i).multiply(t);
        r = r.add(BigInteger.ONE);

        while (!r.isProbablePrime(10)) {
            r = r.add(TWO.multiply(t));
        }

        // Compute p0 = 2( s^(r - 2) mod r )s - 1
        BigInteger p0 = s.modPow(r.subtract(TWO), r);
        p0 = p0.multiply(s.multiply(TWO));
        p0 = p0.subtract(BigInteger.ONE);

        /**
         * Select an integer j0. Find the first prime in the sequence p0 + 2jrs,
         * for j = j0, j0 + 1, j0 + 2,... Denote this prime by p = p0 + 2jrs. p0
         * + 2(j0)rs = p0 + 2jrs p0 + 2(j0 + 1)rs = p0 + 2jrs + 2rs p0 + 2(j0 +
         * 2)rs = p0 + 2jrs + 4rs = p0 + 2jrs + 2rs + 2rs = previous step + 2rs
         */
        BigInteger p = TWO.multiply(j).multiply(r).multiply(s);
        p = p.add(p0);

        while (!p.isProbablePrime(10)) {
            p = p.add(TWO.multiply(r).multiply(s));
        }

        set(p, r, s, t);
    }

    public BigInteger getP() {
        return new BigInteger(p.toString());
    }

    public BigInteger getR() {
        return new BigInteger(r.toString());
    }

    public BigInteger getS() {
        return new BigInteger(s.toString());
    }

    public BigInteger getT() {
        return new BigInteger(t.toString());
    }

    protected void set(BigInteger p, BigInteger r, BigInteger s, BigInteger t) {
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        if (!p.isProbablePrime(10)) {
            throw new IllegalArgumentException("p must be a prime");
        }
        if (r == null) {
            throw new NullPointerException("r cannot be null");
        }
        if (!r.isProbablePrime(10)) {
            throw new IllegalArgumentException("r must be a prime");
        }
        if (s == null) {
            throw new NullPointerException("s cannot be null");
        }
        if (!s.isProbablePrime(10)) {
            throw new IllegalArgumentException("s must be a prime");
        }
        if (t == null) {
            throw new NullPointerException("t cannot be null");
        }
        if (!t.isProbablePrime(10)) {
            throw new IllegalArgumentException("t must be a prime");
        }
        BigInteger rMinus1 = r.subtract(BigInteger.ONE);
        if (rMinus1.remainder(t).compareTo(BigInteger.ZERO) != 0) {
            throw new IllegalArgumentException("t must be a factor of r - 1");
        }
        this.t = new BigInteger(t.toString());

        BigInteger pPlus1 = p.add(BigInteger.ONE);
        if (pPlus1.remainder(s).compareTo(BigInteger.ZERO) != 0) {
            throw new IllegalArgumentException("s must be a factor of p + 1");
        }
        this.s = new BigInteger(s.toString());

        BigInteger pMinus1 = p.subtract(BigInteger.ONE);
        if (pMinus1.remainder(r).compareTo(BigInteger.ZERO) != 0) {
            throw new IllegalArgumentException("r must be a factor of p - 1");
        }
        this.r = new BigInteger(r.toString());
        this.p = new BigInteger(p.toString());
    }
}