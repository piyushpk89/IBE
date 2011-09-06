package nuim.cs.crypto.blitz.field;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.curve.CubicCurve;

public abstract class FiniteField extends Field {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected BigInteger degree;

    public FiniteField(BigInteger characteristic, BigInteger degree) {
        super(characteristic);
        if (degree == null) {
            throw new NullPointerException("degree cannot be null");
        }
        if (degree.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException(
                                               "degree must be greater than zero");
        }
        if (characteristic.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException(
                                               "characteristic of finite field must be greater than zero");
        }
        this.degree = new BigInteger(degree.toString());
    }

    public FiniteField(FiniteField f) {
        this(f.characteristic, f.degree);
    }

    public abstract Element add(Element a, Element b);

    public abstract Element addMod(Element a, Element b);

    public abstract Element div(Element a, Element b);

    public abstract Element divMod(Element a, Element b);

    public Element element(BigInteger x) {
        if (x == null) {
            throw new NullPointerException("x cannot be null");
        }
        return new Element(x, this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null");
        }
        if (obj instanceof FiniteField) {
            FiniteField f = (FiniteField) obj;
            return super.equals(f) && getDegree().compareTo(f.getDegree()) == 0;
        } else {
            return false;
        }
    }

    public BigInteger getDegree() {
        return new BigInteger(degree.toString());
    }

    public abstract Element invMod(Element a);

    public abstract CubicCurve isomorphism(CubicCurve curve);

    public abstract Element mod(Element a);

    public abstract Element mult(Element a, Element b);

    public abstract Element multMod(Element a, Element b);

    public abstract Element neg(Element a);

    public abstract Element negMod(Element a);

    public abstract Element powMod(Element a, BigInteger exp);

    public abstract Element sub(Element a, Element b);

    public abstract Element subMod(Element a, Element b);
}