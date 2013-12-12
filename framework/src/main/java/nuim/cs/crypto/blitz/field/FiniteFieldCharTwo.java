package nuim.cs.crypto.blitz.field;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.curve.CubicCurve;
import nuim.cs.crypto.util.BitUtility;

public class FiniteFieldCharTwo extends FiniteField {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** byte square look-up table */
    protected static byte square_lut[][] = new byte[256][2];

    public static final BigInteger TWO = new BigInteger("2");

    static {
        /** initialise the byte square look-up table */
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            square_lut[i - Byte.MIN_VALUE] = BitUtility.square(i);
        }
    }

    /** this stores the irreducible binary polynomial which is used to mod */
    protected BigInteger modulus;

    public FiniteFieldCharTwo(BigInteger degree) {
        super(TWO, degree);
    }

    /**
     * There is a calculation to get the irreducible binary polynomial acting as
     * the modulus. It is mentioned in Rosing's book and needs to be implemented
     * here!!!
     */
    public FiniteFieldCharTwo(BigInteger degree, BigInteger modulus) {
        super(TWO, degree);
        if (modulus == null) {
            throw new NullPointerException("modulus cannot be null");
        }
        if (modulus.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException(
                                               "modulus must be greater than zero");
        }
        this.modulus = new BigInteger(modulus.toString());
    }

    public FiniteFieldCharTwo(FiniteFieldCharTwo f) {
        this(f.degree);
        if (f.modulus != null) {
            modulus = new BigInteger(f.modulus.toString());
        }
    }

    protected BigInteger add(BigInteger a, BigInteger b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return a.xor(b);
    }

    @Override
    public Element add(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return new Element(add(a.toBigInteger(), b.toBigInteger()), this);
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
        return new FiniteFieldCharTwo(this);
    }

    protected BigInteger div(BigInteger a, BigInteger b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return mult(a, invMod(b));
    }

    @Override
    public Element div(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return new Element(div(a.toBigInteger(), b.toBigInteger()), this);
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

    protected BigInteger invMod(BigInteger a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        BigInteger b = BigInteger.ONE;
        BigInteger c = BigInteger.ZERO;
        BigInteger u = mod(a);
        BigInteger v = new BigInteger(modulus.toString());

        BigInteger buf;
        while (u.bitLength() > 1) {
            int j = u.bitLength() - v.bitLength();
            if (j < 0) {
                buf = u;
                u = v;
                v = buf;
                buf = c;
                c = b;
                b = buf;
                j = -j;
            }
            u = u.xor(v.shiftLeft(j));
            b = b.xor(c.shiftLeft(j));
        }
        return b;
    }

    @Override
    public Element invMod(Element a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        return new Element(invMod(a.toBigInteger()), this);
    }

    @Override
    public CubicCurve isomorphism(CubicCurve curve) {
        if (curve == null) {
            throw new NullPointerException("curve cannot be null");
        }
        BigInteger a1 = curve.a1();
        BigInteger a2 = curve.a2().mod(TWO);
        BigInteger a3 = curve.a3().mod(TWO);
        BigInteger a4 = curve.a4().mod(TWO);
        BigInteger a6 = curve.a6().mod(TWO);
        if (a1.compareTo(BigInteger.ZERO) == 0) {
            BigInteger x = a2.modPow(TWO, TWO);
            x = x.add(a4).mod(TWO);

            BigInteger c = a6.add(a2.multiply(a4)).mod(TWO);

            return new CubicCurve(BigInteger.ZERO, BigInteger.ZERO, a3, x, c,
                                  (FiniteFieldCharTwo) clone());
        } else {
            BigInteger x2 = a2.add(a3).mod(TWO);

            BigInteger c = a6.subtract(a2.multiply(a3.modPow(TWO, TWO)).mod(TWO));
            c = c.subtract(a3.modPow(new BigInteger("3"), TWO));
            c = c.add(a4.modPow(new BigInteger("3"), TWO));
            c = c.add(a3.modPow(new BigInteger("4"), TWO));
            c = c.add(a3.multiply(a4)).mod(TWO);

            return new CubicCurve(a1, x2, BigInteger.ZERO, BigInteger.ZERO, c,
                                  (FiniteFieldCharTwo) clone());
        }
    }

    protected BigInteger mod(BigInteger a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        // a mod p = 0 if a = 0
        if (a.compareTo(modulus) == 0) {
            return BigInteger.ZERO;
        }
        BigInteger r = new BigInteger(a.toString());
        while (r.bitLength() >= modulus.bitLength()) {
            r = r.xor(modulus.shiftLeft(r.bitLength() - modulus.bitLength()));
        }
        return r;
    }

    @Override
    public Element mod(Element a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        return new Element(mod(a.toBigInteger()), this);
    }

    protected BigInteger mult(BigInteger a, BigInteger b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        BigInteger c = BigInteger.ZERO;
        for (int j = 0; j < a.bitLength(); j++) {
            if (a.testBit(j)) {
                c = c.xor(b);
            }
            b = b.shiftLeft(1);
        }
        return c;
    }

    @Override
    public Element mult(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return new Element(mult(a.toBigInteger(), b.toBigInteger()), this);
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

    protected BigInteger neg(BigInteger a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        return a.negate();
    }

    @Override
    public Element neg(Element a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        throw new IllegalArgumentException("not yet implemented");
        //return( new Element( neg( a.toBigInteger() ), this ) );
    }

    @Override
    public Element negMod(Element a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        throw new IllegalArgumentException("not yet implemented");
        //return( mod( neg( a ) ) );
    }

    protected BigInteger powMod(BigInteger a, BigInteger exp) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (exp == null) {
            throw new NullPointerException("exp cannot be null");
        }
        if (exp.compareTo(new BigInteger("2")) == 0) {
            return square(a);
        }
        return null;
    }

    @Override
    public Element powMod(Element a, BigInteger exp) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (exp == null) {
            throw new NullPointerException("exp cannot be null");
        }
        return new Element(powMod(a.toBigInteger(), exp), this);
    }

    protected BigInteger square(BigInteger a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (a.compareTo(BigInteger.ZERO) == 0) {
            return BigInteger.ZERO;
        } else {
            byte b[] = a.toByteArray();
            byte c[] = new byte[b.length * 2];
            for (int i = 0; i < b.length; i++) {
                byte s[] = square_lut[b[i] - Byte.MIN_VALUE];
                c[2 * i] = s[0];
                c[2 * i + 1] = s[1];
            }
            return mod(new BigInteger(c));
        }
    }

    @Override
    public Element sub(Element a, Element b) {
        return add(a, b);
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