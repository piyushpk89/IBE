package nuim.cs.crypto.blitz.field.extension;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

public class Fp2 extends FiniteFieldPrime {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected int WINDOW_SIZE = 5;

    public Fp2(BigInteger characteristic) {
        super(characteristic, new BigInteger("2"));
    }

    private Fp2(Fp2 other) {
        this(other.characteristic);
    }

    @Override
    public Element add(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        Fp2Element s = null;
        Fp2Element t = null;
        if (a instanceof Fp2Element) {
            s = (Fp2Element) a;
        } else {
            s = new Fp2Element(a);
        }
        if (b instanceof Fp2Element) {
            t = (Fp2Element) b;
        } else {
            t = new Fp2Element(b);
        }
        BigInteger r = s.real().add(t.real());
        BigInteger i = s.imag().add(t.imag());
        return new Fp2Element(r, i, this);
    }

    @Override
    public Element addMod(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return mod(add(a, b));
    }

    @Override
    public Object clone() {
        return new Fp2(this);
    }

    public Fp2Element cubeRootOfUnity() {
        BigInteger r = getChar().subtract(BigInteger.ONE);
        r = r.divide(Constant.TWO);

        BigInteger exp = getChar().add(BigInteger.ONE);
        exp = exp.divide(Constant.FOUR);

        BigInteger i = Constant.THREE.modPow(exp, getChar());
        i = i.multiply(r).mod(getChar());

        return new Fp2Element(r, i, this);
    }

    @Override
    public Element div(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        Fp2Element s = null;
        Fp2Element t = null;
        if (a instanceof Fp2Element) {
            s = (Fp2Element) a;
        } else {
            s = new Fp2Element(a);
        }
        if (b instanceof Fp2Element) {
            t = (Fp2Element) b;
        } else {
            t = new Fp2Element(b);
        }

        BigInteger d = t.real().pow(2).add(t.imag().pow(2));
        d = d.modInverse(characteristic);

        BigInteger r = s.real().multiply(t.real());
        r = r.add(s.imag().multiply(t.imag()));
        r = r.multiply(d);

        BigInteger i = s.real().multiply(t.imag()).negate();
        i = i.add(s.imag().multiply(t.real()));
        i = i.multiply(d);

        return new Fp2Element(r, i, this);
    }

    @Override
    public Element divMod(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return mod(div(a, b));
    }

    @Override
    public Element element(BigInteger real) {
        if (real == null) {
            throw new NullPointerException("real cannot be null");
        }
        return element(real, BigInteger.ZERO);
    }

    public Fp2Element element(BigInteger real, BigInteger imag) {
        if (real == null) {
            throw new NullPointerException("real cannot be null");
        }
        if (imag == null) {
            throw new NullPointerException("imag cannot be null");
        }
        return new Fp2Element(real, imag, this);
    }

    protected int getWindowSizePower() {
        int exp = WINDOW_SIZE - 1;
        return (int) (Math.pow(2, exp) - 1);
    }

    @Override
    public Element invMod(Element a) {
        throw new IllegalArgumentException("method not yet implemented");
    }

    @Override
    public Element mod(Element a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (!(a instanceof Fp2Element)) {
            throw new IllegalArgumentException("a must be of type Fp2Element");
        }
        Fp2Element s = (Fp2Element) a;

        return element(s.real().mod(getChar()), s.imag().mod(getChar()));
    }

    @Override
    public Element mult(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        Fp2Element s = null;
        Fp2Element t = null;
        if (a instanceof Fp2Element) {
            s = (Fp2Element) a;
        } else {
            s = new Fp2Element(a);
        }
        if (b instanceof Fp2Element) {
            t = (Fp2Element) b;
        } else {
            t = new Fp2Element(b);
        }
        BigInteger r = s.real().multiply(t.real());
        r = r.subtract(s.imag().multiply(t.imag()));
        BigInteger i = s.real().multiply(t.imag());
        i = i.add(s.imag().multiply(t.real()));
        return new Fp2Element(r, i, this);
    }

    @Override
    public Element multMod(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return mod(mult(a, b));
    }

    @Override
    public Element neg(Element a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (!(a instanceof Fp2Element)) {
            throw new IllegalArgumentException("a must be of type Fp2Element");
        }
        Fp2Element s = (Fp2Element) a;

        return new Fp2Element(s.real().negate(), s.imag().negate(), this);
    }

    /**
     * Returns the exponentiation modulo the characteristic of the extension
     * field. This is a very inefficient implementation and could be improved
     * on.
     */
    @Override
    public Element powMod(Element a, BigInteger exp) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (exp == null) {
            throw new NullPointerException("exp cannot be null");
        }
        if (exp.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("exp cannot be less than zero");
        }
        Fp2Element t = null;
        if (a instanceof Fp2Element) {
            t = (Fp2Element) a;
        } else {
            t = new Fp2Element(a);
        }

        /* still doesn't quite work...
         * haven't noticed much different in speed either
        int b = 8;
        int B = (int) Math.pow( 2, b );
        Fp2Element window[] = new Fp2Element[B/2];
        window[0] = t;
        Fp2Element t2 = square( t );
        for( int i = 1; i < window.length; i++ ) {
            window[i] = (Fp2Element) multMod( window[i-1], t2 );
        }

        byte y[] = exp.toByteArray();
        Fp2Element z = element( BigInteger.ONE, BigInteger.ZERO );
        for( int i = 0; i < y.length; i++ ) {
            if( y[i] > 0 ) {
                int j = 0;
                for( ; y[i] % 2 != 1; j++ ) {
                    y[i] = (byte) ( y[i] >>> 1 );
                }
                int c = j;
                int d = y[i];
                Fp2Element r = (Fp2Element) window[d/2].clone();
                for( int k = 0; k < c; k++ ) {
                    r = square( r );
                }
                z = (Fp2Element) z.multMod( r );
            }
            if( i < y.length - 1 ) {
                for( int j = 0; j < b; j++ ) {
                    z = square( z );
                }
            }
        }

        return( z );
         */

        /* the right-left binary ladder exponentiation method
         * page 423 Crandall & Pomerance
         */
        Fp2Element result = element(BigInteger.ONE, BigInteger.ZERO);
        for (int m = exp.bitLength() - 1; m >= 0; m--) {
            if (exp.testBit(m)) {
                result = (Fp2Element) multMod(result, t);
            }
            if (m == 0) {
                break;
            }
            result = square(result);
        }
        return result;
    }

    protected Fp2Element square(Fp2Element a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        BigInteger r = a.real().pow(2).subtract(a.imag().pow(2));
        r = r.mod(getChar());

        BigInteger i = a.real().multiply(a.imag());
        i = i.shiftLeft(1).mod(getChar());

        return new Fp2Element(r, i, this);
    }

    @Override
    public Element sub(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        Fp2Element s = null;
        Fp2Element t = null;
        if (a instanceof Fp2Element) {
            s = (Fp2Element) a;
        } else {
            s = new Fp2Element(a);
        }
        if (b instanceof Fp2Element) {
            t = (Fp2Element) b;
        } else {
            t = new Fp2Element(b);
        }
        BigInteger r = s.real().subtract(t.real());
        BigInteger i = s.imag().subtract(t.imag());
        return new Fp2Element(r, i, this);
    }

    @Override
    public Element subMod(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return mod(sub(a, b));
    }
}