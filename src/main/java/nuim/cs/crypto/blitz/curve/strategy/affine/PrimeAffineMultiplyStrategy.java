package nuim.cs.crypto.blitz.curve.strategy.affine;

import java.math.BigInteger;
import java.util.Vector;

import nuim.cs.crypto.blitz.curve.EllipticCurve;
import nuim.cs.crypto.blitz.curve.strategy.MultiplyStrategy;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class PrimeAffineMultiplyStrategy extends MultiplyStrategy {
    public PrimeAffineMultiplyStrategy(EllipticCurve curve) {
        super(curve);
    }

    @Override
    public AffinePoint multiplyP(BigInteger scalar, AffinePoint p) {
        AffinePoint result = (AffinePoint) p.clone();

        if (scalar.compareTo(BigInteger.ONE) == 0) {
            // result is already p
        } else if (scalar.compareTo(new BigInteger("2")) == 0) {
            result = curve.doublePoint(p);
        } else {
            BigInteger k = new BigInteger(scalar.toString());

            Vector<Boolean> operationVector = new Vector<Boolean>();
            // while k > 1
            while (k.compareTo(BigInteger.ONE) == 1) {
                // if k's first bit is on, k is odd, else k is even
                if (k.testBit(0)) {
                    operationVector.addElement(new Boolean(false));
                    k = k.subtract(BigInteger.ONE);
                } else {
                    operationVector.addElement(new Boolean(true));
                    k = k.divide(new BigInteger("2"));
                }
            }

            for (int i = operationVector.size() - 1; i >= 0; i--) {
                Boolean operation = operationVector.elementAt(i);
                if (operation.booleanValue()) {
                    result = curve.doublePoint(result);
                } else {
                    result = curve.add(p, result);
                }
            }
        }
        return result;
    }
}