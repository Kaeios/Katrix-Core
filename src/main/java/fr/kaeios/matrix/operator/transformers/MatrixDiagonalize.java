package fr.kaeios.matrix.operator.transformers;

import fr.kaeios.api.Complex;
import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;
import fr.kaeios.matrix.suppliers.DefaultSuppliers;
import fr.kaeios.scalar.complex.ComplexImpl;
import fr.kaeios.scalar.complex.operators.ComplexOperators;

public class MatrixDiagonalize implements UnaryOperator<TransformedMatrix, Matrix> {

    private static final Double PRECISION = Double.longBitsToDouble((1023L - 53L) << 52);

    @Override
    public TransformedMatrix compute(Matrix operand) {

        boolean symmetric = operand.apply(MatrixOperators.CHECK_SYM);

        if(symmetric) {
            TransformedMatrix tridiagonal = operand.apply(MatrixOperators.TRIDIAGONALIZE);
            return findEigenVectors(tridiagonal.getTransformation()[0], tridiagonal.getTransformation()[1]);
        } else {
            TransformedMatrix schur = operand.apply(MatrixOperators.SCHUR);
            return findEigenVectorsFromSchur(schur.getTransformation()[1], schur.getTransformation()[0]);
        }

    }

    private TransformedMatrix findEigenVectors(Matrix householder, Matrix tridiagonal) {
        Matrix z = new MatrixImpl(householder.getValues());

        int size = householder.getRowCount();

        Double[] eigenValues = new Double[size];
        Double[] e = new Double[size];

        for (int i = 0; i < size - 1; i++) {
            eigenValues[i] = tridiagonal.getValues()[i][i];
            e[i] = tridiagonal.getValues()[i+1][i];
        }

        eigenValues[size - 1] = tridiagonal.getValues()[size - 1][size - 1];
        e[size - 1] = 0.0D;

        // Determine the largest main and secondary value in absolute term.
        double maxAbsoluteValue = 0.0D;
        for (int i = 0; i < size; i++) {
            if (Math.abs(eigenValues[i]) > maxAbsoluteValue) {
                maxAbsoluteValue = Math.abs(eigenValues[i]);
            }
            if (Math.abs(e[i]) > maxAbsoluteValue) {
                maxAbsoluteValue = Math.abs(e[i]);
            }
        }

        // Make null any main and secondary value too small to be significant
        if (maxAbsoluteValue != 0) {
            for (int i=0; i < size; i++) {
                if (Math.abs(eigenValues[i]) <= PRECISION * maxAbsoluteValue) {
                    eigenValues[i] = 0.0D;
                }
                if (Math.abs(e[i]) <= PRECISION * maxAbsoluteValue) {
                    e[i] = 0.0D;
                }
            }
        }

        for (int j = 0; j < size; j++) {
            int m;
            do {
                for (m = j; m < size - 1; m++) {
                    double delta = Math.abs(eigenValues[m]) +
                            Math.abs(eigenValues[m + 1]);
                    if (Math.abs(e[m]) + delta == delta) {
                        break;
                    }
                }
                if (m != j) {
                    double q = (eigenValues[j + 1] - eigenValues[j]) / (2 * e[j]);
                    double t = Math.sqrt(1 + q * q);
                    if (q < 0.0) {
                        q = eigenValues[m] - eigenValues[j] + e[j] / (q - t);
                    } else {
                        q = eigenValues[m] - eigenValues[j] + e[j] / (q + t);
                    }
                    double u = 0.0;
                    double s = 1.0;
                    double c = 1.0;
                    int i;
                    for (i = m - 1; i >= j; i--) {
                        double p = s * e[i];
                        double h = c * e[i];
                        if (Math.abs(p) >= Math.abs(q)) {
                            c = q / p;
                            t = Math.sqrt(c * c + 1.0);
                            e[i + 1] = p * t;
                            s = 1.0 / t;
                            c *= s;
                        } else {
                            s = p / q;
                            t = Math.sqrt(s * s + 1.0);
                            e[i + 1] = q * t;
                            c = 1.0 / t;
                            s *= c;
                        }
                        if (e[i + 1] == 0.0) {
                            eigenValues[i + 1] -= u;
                            e[m] = 0.0;
                            break;
                        }
                        q = eigenValues[i + 1] - u;
                        t = (eigenValues[i] - q) * s + 2.0 * c * h;
                        u = s * t;
                        eigenValues[i + 1] = q + u;
                        q = c * t - h;
                        for (int ia = 0; ia < size; ia++) {
                            p = z.getValues()[ia][i + 1];
                            z.getValues()[ia][i + 1] = s * z.getValues()[ia][i] + c * p;
                            z.getValues()[ia][i] = c * z.getValues()[ia][i] - s * p;
                        }
                    }
                    if (t == 0.0 && i >= j) {
                        continue;
                    }
                    eigenValues[j] -= u;
                    e[j] = q;
                    e[m] = 0.0;
                }
            } while (m != j);
        }

        //Sort the eigen values (and vectors) in increase order
        for (int i = 0; i < size; i++) {
            int k = i;
            double p = eigenValues[i];
            for (int j = i + 1; j < size; j++) {
                if (eigenValues[j] > p) {
                    k = j;
                    p = eigenValues[j];
                }
            }
            if (k != i) {
                eigenValues[k] = eigenValues[i];
                eigenValues[i] = p;
                for (int j = 0; j < size; j++) {
                    p = z.getValues()[j][i];
                    z.getValues()[j][i] = z.getValues()[j][k];
                    z.getValues()[j][k] = p;
                }
            }
        }

        Matrix diagonal = new MatrixImpl(size, size, DefaultSuppliers.NULL);

        // Make null any eigen value too small to be significant
        for (int i=0; i < size; i++) {
            if (Math.abs(eigenValues[i]) < PRECISION * maxAbsoluteValue) {
                eigenValues[i] = 0.0D;
            }
            diagonal.getValues()[i][i] = eigenValues[i];
        }

        Matrix eigenVectors = new MatrixImpl(z.getValues());

        return () -> new Matrix[]{eigenVectors, diagonal, eigenVectors.apply(MatrixOperators.INV)};
    }

    private TransformedMatrix findEigenVectorsFromSchur(Matrix triangle, Matrix pass) {

        final int size = triangle.getRowCount();

        // compute matrix norm
        double norm = 0.0;
        for (int i = 0; i < size; i++) {
            for (int j = Math.max(i - 1, 0); j < size; j++) {
                norm += Math.abs(triangle.getValues()[i][j]);
            }
        }

        double[] realEigenvalues = new double[size];
        double[] imagEigenvalues = new double[size];

        for (int i = 0; i < realEigenvalues.length; i++) {
            if (i == (realEigenvalues.length - 1) ||
                    Math.abs(triangle.getValues()[i + 1][i]) <= norm * PRECISION) {
                realEigenvalues[i] = triangle.getValues()[i][i];
            } else {
                final double x = triangle.getValues()[i + 1][i + 1];
                final double p = 0.5 * (triangle.getValues()[i][i] - x);
                final double z = Math.sqrt(Math.abs(p * p + triangle.getValues()[i + 1][i] * triangle.getValues()[i][i + 1]));
                realEigenvalues[i] = x + p;
                imagEigenvalues[i] = z;
                realEigenvalues[i + 1] = x + p;
                imagEigenvalues[i + 1] = -z;
                i++;
            }
        }

        // TODO Zero Norm

        // Backsubstitute to find vectors of upper triangular form

        double r = 0.0;
        double s = 0.0;
        double z = 0.0;

        for (int idx = size - 1; idx >= 0; idx--) {
            Double p = realEigenvalues[idx];
            double q = imagEigenvalues[idx];

            if (Math.abs(q) < 0.01D) {
                // Real vector
                int l = idx;
                triangle.getValues()[idx][idx] = 1.0;
                for (int i = idx - 1; i >= 0; i--) {
                    double w = triangle.getValues()[i][i] - p;
                    r = 0.0;
                    for (int j = l; j <= idx; j++) {
                        r += triangle.getValues()[i][j] * triangle.getValues()[j][idx];
                    }
                    if (compareTo(imagEigenvalues[i], 0.0, PRECISION) < 0) {
                        z = w;
                        s = r;
                    } else {
                        l = i;
                        if (Math.abs(imagEigenvalues[i]) < 0.01D) {
                            if (w != 0.0) {
                                triangle.getValues()[i][idx] = -r / w;
                            } else {
                                triangle.getValues()[i][idx] = -r / (PRECISION * norm);
                            }
                        } else {
                            // Solve real equations
                            double x = triangle.getValues()[i][i + 1];
                            double y = triangle.getValues()[i + 1][i];
                            q = (realEigenvalues[i] - p) * (realEigenvalues[i] - p) +
                                    imagEigenvalues[i] * imagEigenvalues[i];
                            double t = (x * s - z * r) / q;
                            triangle.getValues()[i][idx] = t;
                            if (Math.abs(x) > Math.abs(z)) {
                                triangle.getValues()[i + 1][idx] = (-r - w * t) / x;
                            } else {
                                triangle.getValues()[i + 1][idx] = (-s - y * t) / z;
                            }
                        }

                        // Overflow control
                        double t = Math.abs(triangle.getValues()[i][idx]);
                        if ((PRECISION * t) * t > 1) {
                            for (int j = i; j <= idx; j++) {
                                triangle.getValues()[j][idx] /= t;
                            }
                        }
                    }
                }
            } else if (q < 0.0) {
                // Complex vector
                int l = idx - 1;

                // Last vector component imaginary so matrix is triangular
                if (Math.abs(triangle.getValues()[idx][idx - 1]) > Math.abs(triangle.getValues()[idx - 1][idx])) {
                    triangle.getValues()[idx - 1][idx - 1] = q / triangle.getValues()[idx][idx - 1];
                    triangle.getValues()[idx - 1][idx] = -(triangle.getValues()[idx][idx] - p) / triangle.getValues()[idx][idx - 1];
                } else {
                    Complex x = new ComplexImpl(0.0, -triangle.getValues()[idx - 1][idx]);
                    Complex y = new ComplexImpl(triangle.getValues()[idx - 1][idx - 1] - p, q);

                    final Complex result = x.apply(y, ComplexOperators.DIV);

                    triangle.getValues()[idx - 1][idx - 1] = result.getReal();
                    triangle.getValues()[idx - 1][idx] = result.getImaginary();
                }

                triangle.getValues()[idx][idx - 1] = 0.0;
                triangle.getValues()[idx][idx] = 1.0;

                for (int i = idx - 2; i >= 0; i--) {
                    double ra = 0.0;
                    double sa = 0.0;
                    for (int j = l; j <= idx; j++) {
                        ra += triangle.getValues()[i][j] * triangle.getValues()[j][idx - 1];
                        sa += triangle.getValues()[i][j] * triangle.getValues()[j][idx];
                    }
                    double w = triangle.getValues()[i][i] - p;

                    if (compareTo(imagEigenvalues[i], 0.0, PRECISION) < 0) {
                        z = w;
                        r = ra;
                        s = sa;
                    } else {
                        l = i;
                        if (Math.abs(imagEigenvalues[i]) < 0.01) {
                            Complex x = new ComplexImpl(-ra, sa);
                            Complex y = new ComplexImpl(w, q);

                            final Complex c = x.apply(y, ComplexOperators.DIV);

                            triangle.getValues()[i][idx - 1] = c.getReal();
                            triangle.getValues()[i][idx] = c.getImaginary();
                        } else {
                            // Solve complex equations
                            double x = triangle.getValues()[i][i + 1];
                            double y = triangle.getValues()[i + 1][i];
                            double vr = (realEigenvalues[i] - p) * (realEigenvalues[i] - p) +
                                    imagEigenvalues[i] * imagEigenvalues[i] - q * q;
                            final double vi = (realEigenvalues[i] - p) * 2.0 * q;
                            if (Math.abs(vr) < 0.01 && Math.abs(vi) < 0.01) {
                                vr = PRECISION * norm *
                                        (Math.abs(w) + Math.abs(q) + Math.abs(x) +
                                                Math.abs(y) + Math.abs(z));
                            }

                            Complex x2 = new ComplexImpl(x * r - z * ra + q * sa, x * s - z * sa - q * ra);
                            Complex y2 = new ComplexImpl(vr, vi);

                            final Complex c = x2.apply(y2, ComplexOperators.DIV);

                            triangle.getValues()[i][idx - 1] = c.getReal();
                            triangle.getValues()[i][idx] = c.getImaginary();

                            if (Math.abs(x) > (Math.abs(z) + Math.abs(q))) {
                                triangle.getValues()[i + 1][idx - 1] = (-ra - w * triangle.getValues()[i][idx - 1] +
                                        q * triangle.getValues()[i][idx]) / x;
                                triangle.getValues()[i + 1][idx] = (-sa - w * triangle.getValues()[i][idx] -
                                        q * triangle.getValues()[i][idx - 1]) / x;
                            } else {
                                Complex x3 = new ComplexImpl(-r - y * triangle.getValues()[i][idx - 1], -s - y * triangle.getValues()[i][idx]);
                                Complex y3 = new ComplexImpl(z, q);

                                final Complex c2 = x3.apply(y3, ComplexOperators.DIV);

                                triangle.getValues()[i + 1][idx - 1] = c2.getReal();
                                triangle.getValues()[i + 1][idx] = c2.getImaginary();
                            }
                        }

                        // Overflow control
                        double t = Math.max(Math.abs(triangle.getValues()[i][idx - 1]),
                                Math.abs(triangle.getValues()[i][idx]));
                        if ((PRECISION * t) * t > 1) {
                            for (int j = i; j <= idx; j++) {
                                triangle.getValues()[j][idx - 1] /= t;
                                triangle.getValues()[j][idx] /= t;
                            }
                        }
                    }
                }
            }
        }

        // Back transformation to get eigenvectors of original matrix
        for (int j = size - 1; j >= 0; j--) {
            for (int i = 0; i <= size - 1; i++) {
                z = 0.0;
                for (int k = 0; k <= Math.min(j, size - 1); k++) {
                    z += pass.getValues()[i][k] * triangle.getValues()[k][j];
                }
                pass.getValues()[i][j] = z;
            }
        }

        Matrix diag = new MatrixImpl(size, size);
        for (int i = 0; i < realEigenvalues.length; i++) {
            diag.getValues()[i][i] = realEigenvalues[i];
        }

        return () -> new Matrix[]{ pass, diag, pass.apply(MatrixOperators.INV) };
    }

    private static int compareTo(double x, double y, double eps) {
        if (Math.abs(x - y) <= eps) {
            return 0;
        } else if (x < y) {
            return -1;
        } else if (x > y) {
            return 1;
        }
        // NaN input.
        return Double.compare(x, y);
    }

}
