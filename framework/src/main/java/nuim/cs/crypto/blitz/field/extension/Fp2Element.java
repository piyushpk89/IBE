package nuim.cs.crypto.blitz.field.extension;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteField;

public class Fp2Element extends Element {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static Element element(BigInteger pAdic, FiniteField field) {
        if (pAdic == null) {
            throw new NullPointerException("pAdic cannot be null");
        }
        if (field == null) {
            throw new NullPointerException("field cannot be null");
        }
        BigInteger r = pAdic.mod(field.getChar());
        BigInteger i = pAdic.subtract(r).divide(field.getChar());
        return new Fp2Element(r, i, field);
    }

    protected BigInteger imaginary;

    protected Fp2Element(BigInteger real, BigInteger imaginary,
                         FiniteField field) {
        super(real, field);
        if (imaginary == null) {
            throw new NullPointerException("imaginary cannot be null");
        }
        this.imaginary = new BigInteger(imaginary.toString());
    }

    protected Fp2Element(Element element) {
        super(element);
        if (element instanceof Fp2Element) {
            Fp2Element fp2 = (Fp2Element) element;
            imaginary = fp2.imag();
        } else {
            imaginary = BigInteger.ZERO;
        }
    }

    @Override
    public Object clone() {
        return new Fp2Element(value, imaginary, field);
    }

    @Override
    public Element clone(BigInteger value) {
        return new Fp2Element(value, BigInteger.ZERO, field);
    }

    @Override
    public int compareTo(Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null");
        }
        if (obj instanceof BigInteger) {
            BigInteger i = (BigInteger) obj;
            if (imag().compareTo(BigInteger.ZERO) == 0) {
                return real().compareTo(i);
            } else {
                return imag().compareTo(BigInteger.ZERO);
            }
        } else if (obj instanceof Fp2Element) {
            Fp2Element e = (Fp2Element) obj;
            int comp = real().compareTo(e.real());
            if (comp == 0) {
                return imag().compareTo(e.imag());
            } else {
                return comp;
            }
        } else if (obj instanceof Element) {
            Fp2Element e = new Fp2Element((Element) obj);
            return compareTo(e);
        } else {
            throw new IllegalArgumentException(
                                               "obj must be of type BigInteger or Element or Fp2Element");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null");
        }
        if (obj instanceof BigInteger) {
            // a = a + 0i
            return super.equals((BigInteger) obj)
                   && imaginary.compareTo(BigInteger.ZERO) == 0;
        } else if (obj instanceof Element) {
            Fp2Element e = new Fp2Element((Element) obj);
            return super.equals(e) && imaginary.compareTo(e.imaginary) == 0;
        } else {
            return false;
        }
    }

    public BigInteger imag() {
        return new BigInteger(imaginary.toString());
    }

    @Override
    public BigInteger pAdic() {
        return real().add(imag().multiply(field.getChar()));
    }

    public BigInteger real() {
        return toBigInteger();
    }

    @Override
    public String toString() {
        return super.toString() + " + " + imaginary.toString() + "i";
    }
}