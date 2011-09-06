package nuim.cs.crypto.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigSquareRoot {
    public static final int DEFAULT_MAX_ITERATIONS = 50;
    public static final int DEFAULT_SCALE = 10;
    private static final BigDecimal ONE = new BigDecimal(BigInteger.ONE);

    private static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal ZERO = new BigDecimal(BigInteger.ZERO);

    private int iterations;
    private int scale;

    public BigSquareRoot() {
        setScale(DEFAULT_SCALE);
        setIterations(DEFAULT_MAX_ITERATIONS);
    }

    public BigSquareRoot(int scale) {
        this();
        setScale(scale);
    }

    public BigSquareRoot(int scale, int iterations) {
        this(scale);
        setIterations(iterations);
    }

    public BigInteger ceiling(BigInteger n) {
        BigDecimal r = squareRoot(n);
        BigInteger unscaled = r.toBigInteger().multiply(new BigInteger("10").pow(r.scale()));

        if (unscaled.compareTo(r.unscaledValue()) == 0) {
            return r.toBigInteger();
        } else {
            return r.toBigInteger().add(BigInteger.ONE);
        }
    }

    public BigInteger floor(BigInteger n) {
        return squareRoot(n).toBigInteger();
    }

    public int getIterations() {
        return iterations;
    }

    public int getScale() {
        return scale;
    }

    public void setIterations(int iterations) {
        if (iterations < 0) {
            throw new IllegalArgumentException(
                                               "iterations cannot be less than zero");
        }
        this.iterations = iterations;
    }

    public void setScale(int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("scale cannot be less than zero");
        }
        this.scale = scale;
    }

    protected BigDecimal squareRoot(BigDecimal n) {
        if (n.compareTo(ZERO) <= 0) {
            throw new IllegalArgumentException("n must be a positive number");
        }
        BigDecimal guess = new BigDecimal(ONE.toString());
        BigDecimal lastGuess = new BigDecimal(ONE.toString());
        boolean more = true;
        for (int i = 0; i < iterations && more; i++) {
            lastGuess = new BigDecimal(guess.toString());
            guess = n.divide(lastGuess, scale, BigDecimal.ROUND_HALF_UP);
            guess = guess.add(lastGuess);
            guess = guess.divide(TWO, scale, BigDecimal.ROUND_HALF_UP);

            BigDecimal error = n.subtract(guess.multiply(guess));
            if (lastGuess.equals(guess)) {
                more = error.abs().compareTo(ONE) >= 0;
            }
        }

        return guess;
    }

    public BigDecimal squareRoot(BigInteger n) {
        return squareRoot(new BigDecimal(n));
    }

    //--------------------------
    // Get initial approximation
    //--------------------------

    /*
    private static BigDecimal getInitialApproximation (BigDecimal n) {
        BigInteger integerPart = n.toBigInteger ();
        int length = integerPart.toString ().length ();
        if ((length % 2) == 0) {
            length--;
        }
        length /= 2;
        BigDecimal guess = ONE.movePointRight (length);
        return guess;
    }

    //----------------
    // Get square root
    //----------------

    public BigDecimal get (BigInteger n) {
        return get (new BigDecimal (n));
    }

    public BigDecimal get( BigDecimal n ) {
        // Make sure n is a positive number
        if( n.compareTo( ZERO ) <= 0 ) {
            throw new IllegalArgumentException( "n must be a positive number" );
        }

        BigDecimal initialGuess = getInitialApproximation (n);
        // initial guess
        BigDecimal lastGuess = ZERO;
        BigDecimal guess = new BigDecimal( initialGuess.toString() );

        int iterations = 0;
        boolean more = true;
        while (more) {
            lastGuess = guess;
            guess = n.divide(guess, scale, BigDecimal.ROUND_HALF_UP);
            guess = guess.add(lastGuess);
            guess = guess.divide (TWO, scale, BigDecimal.ROUND_HALF_UP);
            // next guess
            error = n.subtract (guess.multiply (guess));
            if (++iterations >= maxIterations) {
                more = false;
            }
            else if (lastGuess.equals (guess)) {
                more = error.abs ().compareTo (ONE) >= 0;
            }
        }
        return guess;
    }

    /**
     * Returns a random BigInteger with the specified number of decimal digits.
     *
     * @param digits the number of decimal digits (to the base 10) for the
     * randomly generated integer.
     * @return the randomly generated BigInteger.
     */
    /*
    public static BigInteger getRandomBigInteger (int digits ) {
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        for( int i = 0; i < digits; i++ ) {
            sb.append( r.nextInt (10) );
        }
        return( new BigInteger( sb.toString () ) );
    }
     */
}