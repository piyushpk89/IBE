package nuim.cs.crypto.bilinear;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class ModifiedTatePairing extends TatePairing {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ModifiedTatePairing() {
        super();
    }

    public ModifiedTatePairing(int bitLength) {
        super(bitLength);
    }

    @Override
    public Element getPair(AffinePoint P, AffinePoint Q) {
        if (P == null) {
            throw new NullPointerException("P cannot be null");
        }
        if (Q == null) {
            throw new NullPointerException("Q cannot be null");
        }
        // morph the Q point
        Q = morphPoint(Q);

        boolean problem = false;
        do {
            problem = false;
            try {
                AffinePoint R = morphPoint(randomPoint());
                // if Q == R then Qhat would be equal to oo (infinity)
                if (Q.equals(R)) {
                    problem = true;
                } else {
                    Element result = getPair(P, Q, R);
                    // calculate the exponent result^(p^2 - 1)/q
                    BigInteger exp = curve.getField().getChar().pow(2);
                    exp = exp.subtract(BigInteger.ONE);
                    exp = exp.divide(q);

                    return result.powMod(exp);
                }
            } catch (ArithmeticException ae) {
                problem = true;
            }
        } while (problem);
        return null;
    }
}