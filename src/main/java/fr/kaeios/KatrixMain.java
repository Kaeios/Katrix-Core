package fr.kaeios;

import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;
import fr.kaeios.matrix.operator.transformers.MatrixRowEchelonDecomposition;

public class KatrixMain {

    public static void main(String[] args) {

        long t1 = System.currentTimeMillis();

        Matrix B = new MatrixImpl(
                new Double[][]{
                        {3.0D, -0.1D, -0.2D},
                        {0.1D, 7.0D, -0.3D},
                        {0.3D, -0.2D, 10.0D},
                });

        printMatrix(B);

        TransformedMatrix ECH = B.apply(new MatrixRowEchelonDecomposition());

        printMatrix(
                ECH.getTransformation()[0]
        );

        printMatrix(
                ECH.getTransformation()[1]
        );

        printMatrix(
                ECH.getTransformation()[2]
        );

        printMatrix(
                ECH.getTransformation()[0]
                        .apply(ECH.getTransformation()[1], MatrixOperators.MUL)
                        .apply(ECH.getTransformation()[2], MatrixOperators.MUL)
        );

    }

    public static void printMatrix(Matrix m) {
        System.out.println("============");
        for (int row = 0; row < m.getRowsCount(); row++) {
            for (int col = 0; col < m.getColumnsCount(); col++) {
                System.out.printf("%+.4f\t", m.getValues()[row][col]);
            }
            System.out.print("\n");
        }
    }

}
