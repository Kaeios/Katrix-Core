package fr.kaeios.matrix;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.suppliers.DefaultSuppliers;

public class MatrixImpl implements Matrix {

    /**
     * Number of rows in Matrix
     */
    private final int rows;
    /**
     * Number of columns in Matrix
     */
    private final int columns;

    /**
     * Values of the matrix
     */
    private final Double[][] values;


    /**
     * Create an identity Matrix of size m * n
     * @param rows number of rows in Matrix
     * @param columns number of columns in Matrix
     */
    public MatrixImpl(int rows, int columns) {
        this(rows, columns, DefaultSuppliers.IDENTITY);
    }

    /**
     * Create a  null Matrix of size m * n
     * @param rows number of rows in Matrix
     * @param columns number of columns in Matrix
     */
    public MatrixImpl(int rows, int columns, CoefficientSupplier supplier) {
        this.rows = rows;
        this.columns = columns;

        this.values = new Double[rows][columns];

        // Fill with default values of the supplier
        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.columns; y++) {
                values[x][y] = supplier.compute(x, y);
            }
        }
    }

    /**
     * Create a matrix from 2D array
     * @param matrix matrix to convert
     */
    public MatrixImpl(Double[][] matrix) {
        this(matrix.length, matrix[0].length, (x, y) -> matrix[x][y] == null ? 0.0D : matrix[x][y]);
    }

    public Matrix dotApply(Matrix other, BinaryOperator<Double, Double, Double> operation) {
        return new MatrixImpl(rows, columns, (x, y) -> operation.compute(this.values[x][y], other.getValues()[x][y]));
    }

    @Override
    public Matrix dotApply(UnaryOperator<Double, Double> operator) {
        return new MatrixImpl(rows, columns, (x, y) -> operator.compute(this.values[x][y]));
    }

    @Override
    public <T> T apply(UnaryOperator<T, Matrix> operator) {
        return operator.compute(this);
    }

    @Override
    public <R, O> R apply(O other, BinaryOperator<R, Matrix, O> operator) {
        return operator.compute(this, other);
    }

    @Override
    public int getRowsCount() {
        return rows;
    }

    @Override
    public int getColumnsCount() {
        return columns;
    }

    @Override
    public Double[][] getValues() {
        return values;
    }

}
