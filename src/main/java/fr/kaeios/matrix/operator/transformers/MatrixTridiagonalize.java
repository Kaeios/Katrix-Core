package fr.kaeios.matrix.operator.transformers;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;

import java.util.Arrays;

public class MatrixTridiagonalize implements UnaryOperator<TransformedMatrix, Matrix> {


    @Override
    public TransformedMatrix compute(Matrix operand) {

        final int size = operand.getRowCount();

        Matrix householder = new MatrixImpl(operand.getValues());
        Double[] diagonal = new Double[size];
        Double[] subDiagonal = new Double[size - 1];

        final Double[] z = new Double[size];
        for (int k = 0; k < size - 1; k++) {

            //zero-out a row and a column simultaneously
            Double[] hK = householder.getValues()[k];
            diagonal[k] = hK[k];
            double xNormSqr = 0;
            for (int j = k + 1; j < size; ++j) {
                double c = hK[j];
                xNormSqr += c * c;
            }
            double a = (hK[k + 1] > 0) ? -Math.sqrt(xNormSqr) : Math.sqrt(xNormSqr);
            subDiagonal[k] = a;
            if (a != 0.0) {
                // apply Householder transform from left and right simultaneously

                hK[k + 1] -= a;
                double beta = -1 / (a * hK[k + 1]);

                // compute a = beta A v, where v is the Householder vector
                // this loop is written in such a way
                //   1) only the upper triangular part of the matrix is accessed
                //   2) access is cache-friendly for a matrix stored in rows
                Arrays.fill(z, k + 1, size, 0.0D);
                for (int i = k + 1; i < size; ++i) {
                    Double[] hI = householder.getValues()[i];
                    Double hKI = hK[i];
                    Double zI = hI[i] * hKI;
                    for (int j = i + 1; j < size; ++j) {
                        Double hIJ = hI[j];
                        zI   += hIJ * hK[j];
                        z[j] += hIJ * hKI;
                    }
                    z[i] = beta * (z[i] + zI);
                }

                // compute gamma = beta vT z / 2
                double gamma = 0.0D;
                for (int i = k + 1; i < size; ++i) {
                    gamma += z[i] * hK[i];
                }
                gamma *= beta / 2;

                // compute z = z - gamma v
                for (int i = k + 1; i < size; ++i) {
                    z[i] -= gamma * hK[i];
                }

                // update matrix: A = A - v zT - z vT
                // only the upper triangular part of the matrix is updated
                for (int i = k + 1; i < size; ++i) {
                    Double[] hI = householder.getValues()[i];
                    for (int j = i; j < size; ++j) {
                        hI[j] -= hK[i] * z[j] + z[i] * hK[j];
                    }
                }
            }
        }

        diagonal[size - 1] = householder.getValues()[size - 1][size - 1];

        /*
        Compute T
         */
        Double[][] ta = new Double[size][size];
        for (int i = 0; i < size; ++i) {
            ta[i][i] = diagonal[i];
            if (i > 0) {
                ta[i][i - 1] = subDiagonal[i - 1];
            }
            if (i < diagonal.length - 1) {
                ta[i][i + 1] = subDiagonal[i];
            }
        }
        Matrix t = new MatrixImpl(ta);

        /*
        Compute Q
         */

        Double[][] qta = new Double[size][size];

        // build up first part of the matrix by applying Householder transforms
        for (int k = size - 1; k >= 1; --k) {
            Double[] hK = householder.getValues()[k - 1];
            qta[k][k] = 1.0D;
            if (hK[k] != 0.0) {
                final double inv = 1.0D / (subDiagonal[k - 1] * hK[k]);
                double beta = 1.0 / subDiagonal[k - 1];
                qta[k][k] = 1 + beta * hK[k];
                for (int i = k + 1; i < size; ++i) {
                    qta[k][i] = beta * hK[i];
                }
                for (int j = k + 1; j < size; ++j) {
                    beta = 0;
                    for (int i = k + 1; i < size; ++i) {
                        beta += qta[j][i] * hK[i];
                    }
                    beta *= inv;
                    qta[j][k] = beta * hK[k];
                    for (int i = k + 1; i < size; ++i) {
                        qta[j][i] += beta * hK[i];
                    }
                }
            }
        }
        qta[0][0] = 1.0D;
        Matrix Qt = new MatrixImpl(qta);

        return () -> new Matrix[]{Qt.apply(MatrixOperators.TRANSPOSE), t, Qt};
    }

}
