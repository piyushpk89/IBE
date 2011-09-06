package nuim.cs.crypto.blitz.point;

import java.io.Serializable;

import nuim.cs.crypto.blitz.field.Element;

public class AffinePoint implements Comparable<Object>, Point, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected Element x;
    protected Element y;

    protected AffinePoint(AffinePoint other) {
        this(other.x, other.y);
    }

    public AffinePoint(Element x, Element y) {
        if (x == null) {
            throw new NullPointerException("x cannot be null");
        }
        if (y == null) {
            throw new NullPointerException("y cannot be null");
        }
        this.x = (Element) x.clone();
        this.y = (Element) y.clone();
    }

    @Override
    public Object clone() {
        return new AffinePoint(this);
    }

    @Override
    public int compareTo(Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null");
        }
        if (obj instanceof AffinePoint) {
            AffinePoint p = (AffinePoint) obj;
            int comp = x().compareTo(p.x());
            if (comp == 0) {
                return y().compareTo(p.y());
            } else {
                return comp;
            }
        } else {
            throw new IllegalArgumentException(
                                               "obj must be instance of AffinePoint");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null");
        }
        if (obj instanceof AffinePoint) {
            AffinePoint p = (AffinePoint) obj;
            return x().equals(p.x()) && y().equals(p.y());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return new String("(" + x + "," + y + ")");
    }

    @Override
    public Element x() {
        return (Element) x.clone();
    }

    @Override
    public Element y() {
        return (Element) y.clone();
    }
}