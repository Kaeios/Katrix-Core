package fr.kaeios;

import fr.kaeios.api.computation.Function;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;
import fr.kaeios.matrix.operator.unary.MatrixRank;
import fr.kaeios.plotter.FunctionPlotter;
import fr.kaeios.solver.DifferentialSystemSolver;

public class KatrixMain {

    public static void main(String[] args) {

        long t1 = System.currentTimeMillis();

        Matrix B = new MatrixImpl(
                new Double[][]{
                        {-1.0D, 0.0D, 0.0D},
                        {1.0D, -2.0D, 0.0D},
                        {0.0D, 2.0D, 0.0D},
                });

        System.out.println("============");

        printMatrix(B.apply(MatrixOperators.LU).getTransformation()[1]);

    }

    public static void printMatrix(Matrix m) {
        for (int row = 0; row < m.getRowsCount(); row++) {
            for (int col = 0; col < m.getColumnsCount(); col++) {
                System.out.printf("%+.4f\t", m.getValues()[row][col]);
            }
            System.out.print("\n");
        }
    }

}
