package fr.kaeios.matrix.operator.transformers;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;

public class MatrixHessenbergTransform implements UnaryOperator<TransformedMatrix, Matrix> {

    @Override
    public TransformedMatrix compute(Matrix operand) {

        int size = operand.getRowsCount();
        Matrix householder = new MatrixImpl(operand.getValues());
        Double[] ort = new Double[size];

        final int high = size - 1;

        for (int m = 1; m <= high - 1; m++) {
            // Scale column.
            double scale = 0;
            for (int i = m; i <= high; i++) {
                scale += Math.abs(householder.getValues()[i][m - 1]);
            }

            if (Math.abs(scale) > 0.01f) {
                // Compute Householder transformation.
                double h = 0;
                for (int i = high; i >= m; i--) {
                    ort[i] = householder.getValues()[i][m - 1] / scale;
                    h += ort[i] * ort[i];
                }
                final double g = (ort[m] > 0) ? -Math.sqrt(h) : Math.sqrt(h);

                h -= ort[m] * g;
                ort[m] -= g;

                // Apply Householder similarity transformation
                // H = (I - u*u' / h) * H * (I - u*u' / h)

                for (int j = m; j < size; j++) {
                    double f = 0.0D;
                    for (int i = high; i >= m; i--) {
                        f += ort[i] * householder.getValues()[i][j];
                    }
                    f /= h;
                    for (int i = m; i <= high; i++) {
                        householder.getValues()[i][j] -= f * ort[i];
                    }
                }

                for (int i = 0; i <= high; i++) {
                    double f = 0.0D;
                    for (int j = high; j >= m; j--) {
                        f += ort[j] * householder.getValues()[i][j];
                    }
                    f /= h;
                    for (int j = m; j <= high; j++) {
                        householder.getValues()[i][j] -= f * ort[j];
                    }
                }

                ort[m] = scale * ort[m];
                householder.getValues()[m][m - 1] = scale * g;
            }
        }

        /*
        Compute H
         */
        Double[][] h = new Double[size][size];
        for (int i = 0; i < size; ++i) {
            if (i > 0) {
                // copy the entry of the lower sub-diagonal
                h[i][i - 1] = householder.getValues()[i][i - 1];
            }

            // copy upper triangular part of the matrix
            for (int j = i; j < size; ++j) {
                h[i][j] = householder.getValues()[i][j];
            }
        }

        Matrix H = new MatrixImpl(h);

        /*
        Compute P
         */
        Double[][] pa = new Double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                pa[i][j] = (i == j) ? 1.0D : 0.0D;
            }
        }

        for (int m = high - 1; m >= 1; m--) {
            if (householder.getValues()[m][m - 1] != 0.0D) {
                for (int i = m + 1; i <= high; i++) {
                    ort[i] = householder.getValues()[i][m - 1];
                }

                for (int j = m; j <= high; j++) {
                    double g = 0.0D;

                    for (int i = m; i <= high; i++) {
                        g += ort[i] * pa[i][j];
                    }

                    // Double division avoids possible underflow
                    g = (g / ort[m]) / householder.getValues()[m][m - 1];

                    for (int i = m; i <= high; i++) {
                        pa[i][j] += g * ort[i];
                    }
                }
            }
        }

        Matrix P = new MatrixImpl(pa);

        return () -> new Matrix[]{P, H, P.apply(MatrixOperators.TRANSPOSE)};
    }

}
