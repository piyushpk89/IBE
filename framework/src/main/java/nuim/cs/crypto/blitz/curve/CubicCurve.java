package nuim.cs.crypto.blitz.curve;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.field.Field;
import nuim.cs.crypto.blitz.field.FiniteField;

/**
 * Cubic curve representation in Weierstrass form.
 * 
 * A cubic in the form below is said to be in long Weierstrass normal form.
 * <p align="center">
 * y<sup>2</sup> + a<sub>1</sub>xy + a<sub>3</sub>y = x<sup>3</sup> +
 * a<sub>2</sub>x<sup>2</sup> + a<sub>4</sub>x + a<sub>6</sub>
 * </p>
 */
public class CubicCurve {
    public static CubicCurve isomorphism(CubicCurve curve) {
        if (curve == null) {
            throw new NullPointerException("curve cannot be null");
        }
        if (curve.field instanceof FiniteField) {
            FiniteField f = (FiniteField) curve.field;
            return f.isomorphism(curve);
        } else {
            return (CubicCurve) curve.clone();
        }
    }

    protected BigInteger a1;
    protected BigInteger a2;
    protected BigInteger a3;
    protected BigInteger a4;
    protected BigInteger a6;

    protected Field field;

    public CubicCurve(BigInteger a1, BigInteger a2, BigInteger a3,
                      BigInteger a4, BigInteger a6, Field field) {
        if (a1 == null) {
            throw new NullPointerException("a1 cannot be null");
        }
        if (a2 == null) {
            throw new NullPointerException("a2 cannot be null");
        }
        if (a3 == null) {
            throw new NullPointerException("a3 cannot be null");
        }
        if (a4 == null) {
            throw new NullPointerException("a4 cannot be null");
        }
        if (a6 == null) {
            throw new NullPointerException("a6 cannot be null");
        }
        if (field == null) {
            throw new NullPointerException("field cannot be null");
        }
        this.a1 = new BigInteger(a1.toString());
        this.a2 = new BigInteger(a2.toString());
        this.a3 = new BigInteger(a3.toString());
        this.a4 = new BigInteger(a4.toString());
        this.a6 = new BigInteger(a6.toString());
        this.field = (Field) field.clone();
    }

    public CubicCurve(CubicCurve curve) {
        this(curve.a1, curve.a2, curve.a3, curve.a4, curve.a6, curve.field);
    }

    public BigInteger a1() {
        return new BigInteger(a1.toString());
    }

    public BigInteger a2() {
        return new BigInteger(a2.toString());
    }

    public BigInteger a3() {
        return new BigInteger(a3.toString());
    }

    public BigInteger a4() {
        return new BigInteger(a4.toString());
    }

    public BigInteger a6() {
        return new BigInteger(a6.toString());
    }

    public BigInteger b2() {
        BigInteger four = new BigInteger("4");
        BigInteger b2 = a1.pow(2).add(four.multiply(a2));
        if (field instanceof FiniteField) {
            return b2.mod(field.getChar());
        } else {
            return b2;
        }
    }

    public BigInteger b4() {
        BigInteger two = new BigInteger("2");
        BigInteger b4 = a1.multiply(a3).add(two.multiply(a4));
        if (field instanceof FiniteField) {
            return b4.mod(field.getChar());
        } else {
            return b4;
        }
    }

    public BigInteger b6() {
        BigInteger four = new BigInteger("4");
        BigInteger b6 = a3.pow(2).add(four.multiply(a6));
        if (field instanceof FiniteField) {
            return b6.mod(field.getChar());
        } else {
            return b6;
        }
    }

    public BigInteger b8() {
        BigInteger four = new BigInteger("4");
        BigInteger b8 = a1.pow(2).multiply(a6).subtract(a1.multiply(a3).multiply(a4)).add(four.multiply(a2).multiply(a6)).add(a2.multiply(a3.pow(2))).subtract(a4.pow(2));
        if (field instanceof FiniteField) {
            return b8.mod(field.getChar());
        } else {
            return b8;
        }
    }

    public BigInteger c4() {
        BigInteger twoFour = new BigInteger("24");
        BigInteger c4 = b2().pow(2).subtract(twoFour.multiply(b4()));
        if (field instanceof FiniteField) {
            return c4.mod(field.getChar());
        } else {
            return c4;
        }
    }

    public BigInteger c6() {
        BigInteger threeSix = new BigInteger("36");
        BigInteger twoOneSix = new BigInteger("216");
        BigInteger b2 = b2();
        BigInteger c6 = b2.pow(3).negate().add(threeSix.multiply(b2).multiply(b4())).subtract(twoOneSix.multiply(b6()));
        if (field instanceof FiniteField) {
            return c6.mod(field.getChar());
        } else {
            return c6;
        }
    }

    @Override
    public Object clone() {
        return new CubicCurve(this);
    }

    public BigInteger discriminant() {
        BigInteger eight = new BigInteger("8");
        BigInteger nine = new BigInteger("9");
        BigInteger twentySeven = new BigInteger("27");

        BigInteger b2 = b2();
        BigInteger b4 = b4();
        BigInteger b6 = b6();
        BigInteger b8 = b8();

        BigInteger d = b2.pow(2).multiply(b8).negate().subtract(eight.multiply(b4.pow(3))).subtract(twentySeven.multiply(b6.pow(2))).add(nine.multiply(b2).multiply(b4).multiply(b6));
        if (field instanceof FiniteField) {
            return d.mod(field.getChar());
        } else {
            return d;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null");
        }
        if (obj instanceof CubicCurve) {
            CubicCurve curve = (CubicCurve) obj;
            return a1.compareTo(curve.a1) == 0 && a2.compareTo(curve.a2) == 0
                   && a3.compareTo(curve.a3) == 0
                   && a4.compareTo(curve.a4) == 0
                   && a6.compareTo(curve.a6) == 0 && field.equals(curve.field);
        } else {
            return false;
        }
    }

    public Field getField() {
        return (Field) field.clone();
    }

    public BigInteger j() {
        BigInteger c = c4().pow(3);
        BigInteger d = discriminant();
        if (field instanceof FiniteField) {
            BigInteger dInv = d.modInverse(field.getChar());
            return c.multiply(dInv).mod(field.getChar());
        } else {
            return c.divide(d);
        }
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("y2 + ");
        buff.append(a1 + "xy + ");
        buff.append(a3 + "y = ");
        buff.append("x3 + ");
        buff.append(a2 + "x2 + ");
        buff.append(a4 + "x + ");
        buff.append(a6);

        return new String(buff);
    }
}