package nuim.cs.crypto.tender.elgamal.ellipticcurve;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

/**
 * Uses the Legendre symbol to find the first quadratic nonresidue in a finite
 * field (assumes that the limit of the finite field is a prime number). To find
 * the first quadratic non-residue the formula is simple, start at 1 and work up
 * to prime until the first quadratic non-residue is found using the Legendre
 * symbol
 */
public class QuadraticNonresidues {
    protected BigInteger nonresidue;

    /**
     * Class constructor
     * 
     * @param prime
     *            Prime number that is the limit of the finite field to find the
     *            quadratic non-residue in
     */
    public QuadraticNonresidues(FiniteFieldPrime field) {
        if (field == null) {
            throw new NullPointerException("field cannot be null");
        }
        Legendre legendre = new Legendre(field);

        nonresidue = BigInteger.ONE;
        while (legendre.calculate(nonresidue) != Legendre.NONRESIDUE
               && nonresidue.compareTo(field.getChar()) < 0) {
            nonresidue = nonresidue.add(BigInteger.ONE);
        }
    }

    public BigInteger getFirst() {
        return nonresidue;
    }
}