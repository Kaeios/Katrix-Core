package fr.kaeios.matrix.operator.transformers;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;

public class MatrixQRDecomposition implements UnaryOperator<TransformedMatrix, Matrix> {

    @Override
    public TransformedMatrix compute(Matrix operand) {

        int m = operand.getRowCount();
        int n = operand.getColumnsCount();

        Matrix qrt = operand.apply(MatrixOperators.TRANSPOSE);
        double[] rDiag = new double[Math.min(m, n)];

        for (int minor = 0; minor < Math.min(m, n); minor++) {
            Double[] qrtMinor = qrt.getValues()[minor];

            /*
             * Let x be the first column of the minor, and a^2 = |x|^2.
             * x will be in the positions qr[minor][minor] through qr[m][minor].
             * The first column of the transformed minor will be (a,0,0,..)'
             * The sign of a is chosen to be opposite to the sign of the first
             * component of x. Let's find a:
             */
            double xNormSqr = 0;
            for (int row = minor; row < qrtMinor.length; row++) {
                final double c = qrtMinor[row];
                xNormSqr += c * c;
            }
            final double a = (qrtMinor[minor] > 0) ? -Math.sqrt(xNormSqr) : Math.sqrt(xNormSqr);
            rDiag[minor] = a;

            if (a != 0.0) {

                /*
                 * Calculate the normalized reflection vector v and transform
                 * the first column. We know the norm of v beforehand: v = x-ae
                 * so |v|^2 = <x-ae,x-ae> = <x,x>-2a<x,e>+a^2<e,e> =
                 * a^2+a^2-2a<x,e> = 2a*(a - <x,e>).
                 * Here <x, e> is now qr[minor][minor].
                 * v = x-ae is stored in the column at qr:
                 */
                qrtMinor[minor] -= a; // now |v|^2 = -2a*(qr[minor][minor])

                /*
                 * Transform the rest of the columns of the minor:
                 * They will be transformed by the matrix H = I-2vv'/|v|^2.
                 * If x is a column vector of the minor, then
                 * Hx = (I-2vv'/|v|^2)x = x-2vv'x/|v|^2 = x - 2<x,v>/|v|^2 v.
                 * Therefore the transformation is easily calculated by
                 * subtracting the column vector (2<x,v>/|v|^2)v from x.
                 *
                 * Let 2<x,v>/|v|^2 = alpha. From above we have
                 * |v|^2 = -2a*(qr[minor][minor]), so
                 * alpha = -<x,v>/(a*qr[minor][minor])
                 */
                for (int col = minor+1; col < qrt.getRowCount(); col++) {
                    Double[] qrtCol = qrt.getValues()[col];
                    double alpha = 0;
                    for (int row = minor; row < qrtCol.length; row++) {
                        alpha -= qrtCol[row] * qrtMinor[row];
                    }
                    alpha /= a * qrtMinor[minor];

                    // Subtract the column vector alpha*v from x.
                    for (int row = minor; row < qrtCol.length; row++) {
                        qrtCol[row] -= alpha * qrtMinor[row];
                    }
                }
            }
        }

        /*
        Compute R
         */
        // R is supposed to be m x n
        n = qrt.getRowCount();
        m = qrt.getColumnsCount();
        Double[][] ra = new Double[m][n];
        // copy the diagonal from rDiag and the upper triangle of qr
        for (int row = Math.min(m, n) - 1; row >= 0; row--) {
            ra[row][row] = rDiag[row];
            for (int col = row + 1; col < n; col++) {
                ra[row][col] = qrt.getValues()[col][row];
            }
        }
        Matrix R = new MatrixImpl(ra);

        /*
        Compute Q
        */

        // QT is supposed to be m x m
        Double[][] qta = new Double[m][m];
        for (int i = 0; i < qta.length; i++) {
            for (int j = 0; j < qta[0].length; j++) {
                qta[i][j] = 0.0D;
            }
        }

        /*
         * Q = Q1 Q2 ... Q_m, so Q is formed by first constructing Q_m and then
         * applying the Householder transformations Q_(m-1),Q_(m-2),...,Q1 in
         * succession to the result
         */
        for (int minor = m - 1; minor >= Math.min(m, n); minor--) {
            qta[minor][minor] = 1.0d;
        }

        for (int minor = Math.min(m, n)-1; minor >= 0; minor--){
            Double[] qrtMinor = qrt.getValues()[minor];
            qta[minor][minor] = 1.0d;
            if (qrtMinor[minor] != 0.0) {
                for (int col = minor; col < m; col++) {
                    double alpha = 0;
                    for (int row = minor; row < m; row++) {
                        alpha -= qta[col][row] * qrtMinor[row];
                    }
                    alpha /= rDiag[minor] * qrtMinor[minor];

                    for (int row = minor; row < m; row++) {
                        qta[col][row] += -alpha * qrtMinor[row];
                    }
                }
            }
        }

        Matrix Q = new MatrixImpl(qta).apply(MatrixOperators.TRANSPOSE);


        return () -> new Matrix[] {Q, R};
    }

}
