package nuim.cs.crypto.bilinear;

import java.io.Serializable;
import java.math.BigInteger;

import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

public interface BilinearMap extends Serializable {
    public PairingEllipticCurve getCurve();

    public Element getPair(AffinePoint P, AffinePoint Q);

    public BigInteger getQ();

    public AffinePoint mapToPoint(BigInteger x);
}