package nuim.cs.crypto.blitz.field;

import java.io.Serializable;
import java.math.BigInteger;

@SuppressWarnings("rawtypes")
public class Element implements Comparable, Serializable {
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
        return new Element(pAdic, field);
    }

    protected FiniteField field;

    protected BigInteger value;

    public Element(BigInteger value, Element like) {
        this(like.clone(value));
    }

    /*
    protected Element( byte value[], FiniteField field ) {
        if( value == null ) {
            throw new NullPointerException( "value cannot be null" );
        }
        this.value = new BigInteger( value );
        this.field = field;
    }
     */

    protected Element(BigInteger value, FiniteField field) {
        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }
        if (field == null) {
            throw new NullPointerException("field cannot be null");
        }
        this.value = new BigInteger(value.toString());
        this.field = field;
    }

    protected Element(Element other) {
        this(other.value, other.field);
    }

    public Element add(BigInteger other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        return add(clone(other));
    }

    public Element add(Element other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        if (getClass().isAssignableFrom(other.getClass())) {
            return other.field.add(this, other);
        } else {
            return field.add(this, other);
        }
    }

    public Element addMod(BigInteger other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        return addMod(clone(other));
    }

    public Element addMod(Element other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        if (getClass().isAssignableFrom(other.getClass())) {
            return other.field.addMod(this, other);
        } else {
            return field.addMod(this, other);
        }
    }

    @Override
    public Object clone() {
        return new Element(this);
    }

    protected Element clone(BigInteger value) {
        return new Element(value, field);
    }

    @Override
    public int compareTo(Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null");
        }
        if (obj instanceof BigInteger) {
            BigInteger i = (BigInteger) obj;
            return value.compareTo(i);
        } else if (obj instanceof Element) {
            Element e = (Element) obj;
            return value.compareTo(e.value);
        } else {
            throw new IllegalArgumentException(
                                               "obj must be of type BigInteger or Element");
        }
    }

    public Element div(BigInteger other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        return div(clone(other));
    }

    public Element div(Element other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        if (getClass().isAssignableFrom(other.getClass())) {
            return other.field.div(this, other);
        } else {
            return field.div(this, other);
        }
    }

    public Element divMod(BigInteger other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        return divMod(clone(other));
    }

    public Element divMod(Element other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        if (getClass().isAssignableFrom(other.getClass())) {
            return other.field.divMod(this, other);
        } else {
            return field.divMod(this, other);
        }
    }

    protected boolean equals(BigInteger obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null");
        }
        return value.compareTo(obj) == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null");
        }
        if (obj instanceof BigInteger) {
            return equals((BigInteger) obj);
        } else if (obj instanceof Element) {
            Element e = (Element) obj;
            return value.compareTo(e.value) == 0 && field.equals(e.field);
        } else {
            return false;
        }
    }

    public Element invMod() {
        return field.invMod(this);
    }

    public Element mod() {
        return field.mod(this);
    }

    public Element mult(BigInteger other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        return mult(clone(other));
    }

    public Element mult(Element other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        if (getClass().isAssignableFrom(other.getClass())) {
            return other.field.mult(this, other);
        } else {
            return field.mult(this, other);
        }
    }

    public Element multMod(BigInteger other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        return multMod(clone(other));
    }

    public Element multMod(Element other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        if (getClass().isAssignableFrom(other.getClass())) {
            return other.field.multMod(this, other);
        } else {
            return field.multMod(this, other);
        }
    }

    public Element neg() {
        return field.neg(this);
    }

    public Element negMod() {
        return field.negMod(this);
    }

    public BigInteger pAdic() {
        return value;
    }

    public Element powMod(BigInteger exp) {
        if (exp == null) {
            throw new NullPointerException("exp cannot be null");
        }
        return field.powMod(this, exp);
    }

    public Element sub(BigInteger other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        return sub(clone(other));
    }

    public Element sub(Element other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        if (getClass().isAssignableFrom(other.getClass())) {
            return other.field.sub(this, other);
        } else {
            return field.sub(this, other);
        }
    }

    public Element subMod(BigInteger other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        return subMod(clone(other));
    }

    public Element subMod(Element other) {
        if (other == null) {
            throw new NullPointerException("other cannot be null");
        }
        if (getClass().isAssignableFrom(other.getClass())) {
            return other.field.subMod(this, other);
        } else {
            return field.subMod(this, other);
        }
    }

    public BigInteger toBigInteger() {
        return new BigInteger(value.toString());
    }

    public byte[] toByteArray() {
        return value.toByteArray();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}