package fr.kaeios.matrix.operator.unary;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.suppliers.DefaultSuppliers;

public class MatrixInverse implements UnaryOperator<Matrix, Matrix> {

    @Override
    public Matrix compute(Matrix matrix) {
        Matrix operand = new MatrixImpl(matrix.getValues());
        int size = operand.getRowCount();

        Matrix x = new MatrixImpl(size, size, DefaultSuppliers.NULL);
        Matrix b  = new MatrixImpl(size, size);

        int[] index = new int[size];

        // Transform the matrix into an upper triangle
        gaussian(operand, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i = 0; i < (size - 1); i++)
            for (int j = (i + 1); j < size; j++)
                for (int k=0; k < size; k++)
                    b.getValues()[index[j]][k] -= operand.getValues()[index[j]][i] * b.getValues()[index[i]][k];

        // Perform backward substitutions
        for (int i = 0; i < size; ++i) {

            x.getValues()[size-1][i] = b.getValues()[index[size - 1]][i]/operand.getValues()[index[size - 1]][size - 1];

            for (int j = size - 2; j >= 0; j--) {

                x.getValues()[j][i] = b.getValues()[index[j]][i];

                for (int k=j+1; k<size; ++k)
                    x.getValues()[j][i] -= operand.getValues()[index[j]][k]*x.getValues()[k][i];

                x.getValues()[j][i] /= operand.getValues()[index[j]][j];
            }
        }

        return x;
    }

    // Method to carry out the partial-pivoting Gaussian
    // elimination.  Here index[] stores pivoting order.
    public static void gaussian(Matrix operand, int[] index) {
        int n = index.length;
        double[] factors = new double[n];

        // Initialize the index
        for (int i=0; i < n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i) {

            double maxValue = 0;

            for (int j=0; j<n; ++j) {
                double value = Math.abs(operand.getValues()[i][j]);
                if (value > maxValue) maxValue = value;
            }
            factors[i] = maxValue;
        }

        // Search the pivoting element from each column
        int k = 0;

        for (int j=0; j<n-1; ++j) {
            double pi1 = 0;
            for (int i=j; i<n; ++i) {
                double pi0 = Math.abs(operand.getValues()[index[i]][j]);
                pi0 /= factors[index[i]];

                if (pi0 <= pi1) continue;

                pi1 = pi0;
                k = i;
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i) {

                double pj = operand.getValues()[index[i]][j]/operand.getValues()[index[j]][j];

                // Record pivoting ratios below the diagonal
                operand.getValues()[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l=j+1; l<n; ++l)
                    operand.getValues()[index[i]][l] -= pj*operand.getValues()[index[j]][l];
            }
        }
    }
}
