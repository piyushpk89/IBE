package nuim.cs.crypto.blitz.field;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.curve.CubicCurve;

public class FiniteFieldPrime extends FiniteField {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FiniteFieldPrime(BigInteger characteristic) {
        super(characteristic, BigInteger.ONE);
    }

    protected FiniteFieldPrime(BigInteger characteristic, BigInteger degree) {
        super(characteristic, degree);
    }

    private FiniteFieldPrime(FiniteFieldPrime f) {
        this(f.characteristic);
    }

    protected BigInteger add(BigInteger a, BigInteger b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return a.add(b);
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

    protected BigInteger addMod(BigInteger a, BigInteger b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return a.add(b).mod(characteristic);
    }

    @Override
    public Element addMod(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return new Element(addMod(a.toBigInteger(), b.toBigInteger()), this);
    }

    @Override
    public Object clone() {
        return new FiniteFieldPrime(this);
    }

    protected BigInteger div(BigInteger a, BigInteger b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return a.multiply(b.modInverse(characteristic));
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

    protected BigInteger divMod(BigInteger a, BigInteger b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return a.multiply(b.modInverse(characteristic)).mod(characteristic);
    }

    @Override
    public Element divMod(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return new Element(divMod(a.toBigInteger(), b.toBigInteger()), this);
    }

    protected BigInteger invMod(BigInteger a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        return a.modInverse(characteristic);
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
        BigInteger four = new BigInteger("4");
        BigInteger six = new BigInteger("6");

        BigInteger a1 = BigInteger.ZERO;
        BigInteger a2 = BigInteger.ZERO;
        BigInteger a3 = BigInteger.ZERO;

        BigInteger a4 = curve.c4().multiply(new BigInteger("-27"));
        a4 = a4.multiply(six.modPow(four, characteristic).modInverse(characteristic));
        a4 = a4.mod(characteristic);

        BigInteger a6 = curve.c6().multiply(new BigInteger("-54"));
        a6 = a6.multiply(six.modPow(six, characteristic).modInverse(characteristic));
        a6 = a6.mod(characteristic);

        return new CubicCurve(a1, a2, a3, a4, a6, (FiniteFieldPrime) clone());
    }

    protected BigInteger mod(BigInteger a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        return a.mod(characteristic);
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
        return a.multiply(b);
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

    protected BigInteger multMod(BigInteger a, BigInteger b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return a.multiply(b).mod(characteristic);
    }

    @Override
    public Element multMod(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return new Element(multMod(a.toBigInteger(), b.toBigInteger()), this);
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
        return new Element(neg(a.toBigInteger()), this);
    }

    protected BigInteger negMod(BigInteger a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        return a.negate().mod(characteristic);
    }

    @Override
    public Element negMod(Element a) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        return new Element(negMod(a.toBigInteger()), this);
    }

    protected BigInteger powMod(BigInteger a, BigInteger exp) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (exp == null) {
            throw new NullPointerException("exp cannot be null");
        }
        return a.modPow(exp, characteristic);
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

    protected BigInteger sub(BigInteger a, BigInteger b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return a.subtract(b);
    }

    @Override
    public Element sub(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return new Element(sub(a.toBigInteger(), b.toBigInteger()), this);
    }

    protected BigInteger subMod(BigInteger a, BigInteger b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return a.subtract(b).mod(characteristic);
    }

    @Override
    public Element subMod(Element a, Element b) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        return new Element(subMod(a.toBigInteger(), b.toBigInteger()), this);
    }
}