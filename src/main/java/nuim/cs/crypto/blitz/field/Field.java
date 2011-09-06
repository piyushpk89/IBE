package nuim.cs.crypto.blitz.field;

import java.io.Serializable;
import java.math.BigInteger;

public class Field implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected BigInteger characteristic;

    public Field(BigInteger characteristic) {
        if (characteristic == null) {
            throw new NullPointerException("characteristic cannot be null");
        }
        this.characteristic = new BigInteger(characteristic.toString());
    }

    public Field(Field f) {
        this(f.characteristic);
    }

    @Override
    public Object clone() {
        return new Field(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null");
        }
        if (obj instanceof Field) {
            Field f = (Field) obj;
            return getChar().compareTo(f.getChar()) == 0;
        } else {
            return false;
        }
    }

    public BigInteger getChar() {
        return new BigInteger(characteristic.toString());
    }
}