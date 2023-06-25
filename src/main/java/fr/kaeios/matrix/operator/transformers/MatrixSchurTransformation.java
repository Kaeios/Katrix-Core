package fr.kaeios.matrix.operator.transformers;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.matrix.operator.MatrixOperators;

public class MatrixSchurTransformation implements UnaryOperator<TransformedMatrix, Matrix> {

    private static final Double PRECISION = Double.longBitsToDouble((1023L - 53L) << 52);

    @Override
    public TransformedMatrix compute(Matrix operand) {

        TransformedMatrix hessenberg = operand.apply(MatrixOperators.HESSENBERG);

        Matrix triangle = hessenberg.getTransformation()[1];
        Matrix pass = hessenberg.getTransformation()[0];

        int size = triangle.getRowsCount();

        // shift information
        ShiftInfo shift = new ShiftInfo();

        // Outer loop over eigenvalue index
        int iteration = 0;
        int iu = size - 1;
        while (iu >= 0) {

            // Look for single small sub-diagonal element
            int il = findSmallSubDiagonalElement(triangle, iu);

            // Check for convergence
            if (il == iu) {
                // One root found
                triangle.getValues()[iu][iu] += shift.exShift;
                iu--;
            } else if (il == iu - 1) {
                // Two roots found
                double p = (triangle.getValues()[iu - 1][iu - 1] - triangle.getValues()[iu][iu]) / 2.0;
                double q = p * p + triangle.getValues()[iu][iu - 1] * triangle.getValues()[iu - 1][iu];
                triangle.getValues()[iu][iu] += shift.exShift;
                triangle.getValues()[iu - 1][iu - 1] += shift.exShift;

                if (q >= 0) {
                    double z = Math.sqrt(Math.abs(q));
                    if (p >= 0) {
                        z = p + z;
                    } else {
                        z = p - z;
                    }
                    double x = triangle.getValues()[iu][iu - 1];
                    double s = Math.abs(x) + Math.abs(z);
                    p = x / s;
                    q = z / s;
                    double r = Math.sqrt(p * p + q * q);
                    p /= r;
                    q /= r;

                    // Row modification
                    for (int j = iu - 1; j < size; j++) {
                        z = triangle.getValues()[iu - 1][j];
                        triangle.getValues()[iu - 1][j] = q * z + p * triangle.getValues()[iu][j];
                        triangle.getValues()[iu][j] = q * triangle.getValues()[iu][j] - p * z;
                    }

                    // Column modification
                    for (int i = 0; i <= iu; i++) {
                        z = triangle.getValues()[i][iu - 1];
                        triangle.getValues()[i][iu - 1] = q * z + p * triangle.getValues()[i][iu];
                        triangle.getValues()[i][iu] = q * triangle.getValues()[i][iu] - p * z;
                    }

                    // Accumulate transformations
                    for (int i = 0; i <= size - 1; i++) {
                        z = pass.getValues()[i][iu - 1];
                        pass.getValues()[i][iu - 1] = q * z + p * pass.getValues()[i][iu];
                        pass.getValues()[i][iu] = q * pass.getValues()[i][iu] - p * z;
                    }
                }
                iu -= 2;
            } else {
                // No convergence yet
                computeShift(triangle, il, iu, iteration, shift);

                // TODO Limit iterations

                // the initial houseHolder vector for the QR step
                double[] hVec = new double[3];

                int im = initQRStep(triangle, il, iu, shift, hVec);
                performDoubleQRStep(triangle, pass, il, im, iu, shift, hVec);
            }
        }

        return () -> new Matrix[]{pass, triangle, pass.apply(MatrixOperators.TRANSPOSE)};
    }

    private int initQRStep(Matrix matrix, int il, int iu, ShiftInfo shift, double[] hVec) {
        // Look for two consecutive small sub-diagonal elements
        int im = iu - 2;
        while (im >= il) {
            double z = matrix.getValues()[im][im];
            double r = shift.x - z;
            double s = shift.y - z;
            hVec[0] = (r * s - shift.w) / matrix.getValues()[im + 1][im] + matrix.getValues()[im][im + 1];
            hVec[1] = matrix.getValues()[im + 1][im + 1] - z - r - s;
            hVec[2] = matrix.getValues()[im + 2][im + 1];

            if (im == il) {
                break;
            }

            double lhs = Math.abs(matrix.getValues()[im][im - 1]) * (Math.abs(hVec[1]) + Math.abs(hVec[2]));
            double rhs = Math.abs(hVec[0]) * (Math.abs(matrix.getValues()[im - 1][im - 1]) +
                    Math.abs(z) +
                    Math.abs(matrix.getValues()[im + 1][im + 1]));

            if (lhs < PRECISION * rhs) {
                break;
            }
            im--;
        }

        return im;
    }

    private void performDoubleQRStep(Matrix triangle, Matrix pass, int il, int im, int iu, ShiftInfo shift, double[] hVec) {

        int size = triangle.getRowsCount();
        double p = hVec[0];
        double q = hVec[1];
        double r = hVec[2];

        for (int k = im; k <= iu - 1; k++) {
            boolean notlast = k != (iu - 1);
            if (k != im) {
                p = triangle.getValues()[k][k - 1];
                q = triangle.getValues()[k + 1][k - 1];
                r = notlast ? triangle.getValues()[k + 2][k - 1] : 0.0;
                shift.x = Math.abs(p) + Math.abs(q) + Math.abs(r);

                if (Math.abs(shift.x) < PRECISION) {
                    continue;
                }

                p /= shift.x;
                q /= shift.x;
                r /= shift.x;
            }
            double s = Math.sqrt(p * p + q * q + r * r);
            if (p < 0.0) {
                s = -s;
            }
            if (s != 0.0) {
                if (k != im) {
                    triangle.getValues()[k][k - 1] = -s * shift.x;
                } else if (il != im) {
                    triangle.getValues()[k][k - 1] = -triangle.getValues()[k][k - 1];
                }
                p += s;
                shift.x = p / s;
                shift.y = q / s;
                double z = r / s;
                q /= p;
                r /= p;

                // Row modification
                for (int j = k; j < size; j++) {
                    p = triangle.getValues()[k][j] + q * triangle.getValues()[k + 1][j];
                    if (notlast) {
                        p += r * triangle.getValues()[k + 2][j];
                        triangle.getValues()[k + 2][j] -= p * z;
                    }
                    triangle.getValues()[k][j] -= p * shift.x;
                    triangle.getValues()[k + 1][j] -= p * shift.y;
                }

                // Column modification
                for (int i = 0; i <= Math.min(iu, k + 3); i++) {
                    p = shift.x * triangle.getValues()[i][k] + shift.y * triangle.getValues()[i][k + 1];
                    if (notlast) {
                        p += z * triangle.getValues()[i][k + 2];
                        triangle.getValues()[i][k + 2] -= p * r;
                    }
                    triangle.getValues()[i][k] -= p;
                    triangle.getValues()[i][k + 1] -= p * q;
                }

                // Accumulate transformations
                int high = size - 1;
                for (int i = 0; i <= high; i++) {
                    p = shift.x * pass.getValues()[i][k] + shift.y * pass.getValues()[i][k + 1];
                    if (notlast) {
                        p += z * pass.getValues()[i][k + 2];
                        pass.getValues()[i][k + 2] -= p * r;
                    }
                    pass.getValues()[i][k] -= p;
                    pass.getValues()[i][k + 1] -= p * q;
                }
            }  // (s != 0)
        }  // k loop

        // clean up pollution due to round-off errors
        for (int i = im + 2; i <= iu; i++) {
            triangle.getValues()[i][i-2] = 0.0;
            if (i > im + 2) {
                triangle.getValues()[i][i-3] = 0.0;
            }
        }
    }

    private void computeShift(Matrix matrix, int l, int idx, int iteration, ShiftInfo shift) {
        // Form shift
        shift.x = matrix.getValues()[idx][idx];
        shift.y = shift.w = 0.0;
        if (l < idx) {
            shift.y = matrix.getValues()[idx - 1][idx - 1];
            shift.w = matrix.getValues()[idx][idx - 1] * matrix.getValues()[idx - 1][idx];
        }

        // Wilkinson's original ad hoc shift
        if (iteration == 10) {
            shift.exShift += shift.x;
            for (int i = 0; i <= idx; i++) {
                matrix.getValues()[i][i] -= shift.x;
            }
            double s = Math.abs(matrix.getValues()[idx][idx - 1]) + Math.abs(matrix.getValues()[idx - 1][idx - 2]);
            shift.x = 0.75 * s;
            shift.y = 0.75 * s;
            shift.w = -0.4375 * s * s;
        }

        // MATLAB's new ad hoc shift
        if (iteration == 30) {
            double s = (shift.y - shift.x) / 2.0;
            s = s * s + shift.w;
            if (s > 0.0) {
                s = Math.sqrt(s);
                if (shift.y < shift.x) {
                    s = -s;
                }
                s = shift.x - shift.w / ((shift.y - shift.x) / 2.0 + s);
                for (int i = 0; i <= idx; i++) {
                    matrix.getValues()[i][i] -= s;
                }
                shift.exShift += s;
                shift.x = shift.y = shift.w = 0.964;
            }
        }
    }

    /**
     * Find the first small sub-diagonal element and returns its index.
     *
     * @param startIdx the starting index for the search
     * @return the index of the first small sub-diagonal element
     */
    private int findSmallSubDiagonalElement(Matrix matrix, int startIdx) {
        double norm = matrix.apply(MatrixOperators.L1_NORM);

        int l = startIdx;
        while (l > 0) {
            double s = Math.abs(matrix.getValues()[l - 1][l - 1]) + Math.abs(matrix.getValues()[l][l]);
            if (s == 0.0) {
                s = norm;
            }
            if (Math.abs(matrix.getValues()[l][l - 1]) < PRECISION * s) {
                break;
            }
            l--;
        }
        return l;
    }

    /**
     * Internal data structure holding the current shift information.
     * Contains variable names as present in the original JAMA code.
     */
    private static class ShiftInfo {
        // CHECKSTYLE: stop all

        /** x shift info */
        double x;
        /** y shift info */
        double y;
        /** w shift info */
        double w;
        /** Indicates an exceptional shift. */
        double exShift;

        // CHECKSTYLE: resume all
    }

}
