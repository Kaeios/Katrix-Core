package fr.kaeios;

import fr.kaeios.api.computation.Function;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.TransformedMatrix;
import fr.kaeios.matrix.MatrixImpl;
import fr.kaeios.matrix.operator.MatrixOperators;
import fr.kaeios.plotter.FunctionPlotter;
import fr.kaeios.solver.DifferentialSystemSolver;

public class KatrixMain {

    public static void main(String[] args) {

        long t1 = System.currentTimeMillis();

        Matrix A = new MatrixImpl(
                new Double[][]{
                        {-1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D},
                        {1.0D, -2.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D},
                        {0.0D, 2.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D},
                        {0.0D, 0.0D, 1.0D, -0.5D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D},
                        {0.0D, 0.0D, 0.0D, 0.5D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D},
                        {0.0D, 0.0D, 0.0D, 0.0D, 1.0D, -0.05D, 0.0D, 0.0D, 0.0D, 0.0D},
                        {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.05D, -1.0D, 0.0D, 0.0D, 0.0D},
                        {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.5D, 0.0D, -1.0D, 0.0D},
                        {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.5D, -0.5D, 0.0D, 0.0D},
                        {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.0D},
                });

        Matrix I = new MatrixImpl(
                new Double[][]{
                        {1.0D},
                        {0.0D},
                        {0.0D},
                        {0.0D},
                        {0.0D},
                        {0.0D},
                        {0.0D},
                });

        Function[] functions = new DifferentialSystemSolver(A, I).solve();

        FunctionPlotter plot = new FunctionPlotter(functions, 0, 8, 1500, 1000);
        plot.compute();

        long t2 = System.currentTimeMillis();

        System.out.println(t2 - t1);

        Matrix B = new MatrixImpl(
                new Double[][]{
                        {1.0D, 2.0D, 0.0D, -3.0D},
                        {2.0D, 2.0D, 2.0D, -2.0D},
                        {-1.0D, -1.0D, -1.0D, 1.0D},
                        {3.0D, 2.0D, 4.0D, -1.0D},
                });

        TransformedMatrix QR = B
                .apply(MatrixOperators.TRANSPOSE)
                .apply(MatrixOperators.QR);


        Matrix Q = QR.getTransformation()[0];

        Matrix V1 = new MatrixImpl(4, 1,  (x, y) -> Q.getValues()[x][2]);
        Matrix V2 = new MatrixImpl(4, 1,  (x, y) -> Q.getValues()[x][3]);

        Matrix res = B.apply(V1.apply(V2.apply(10.0D, MatrixOperators.SCALAR_MUL), MatrixOperators.ADD), MatrixOperators.MUL);

        Matrix R = QR.getTransformation()[1];

        printMatrix(res);

    }

    static void printMatrix(Matrix m) {
        for (int row = 0; row < m.getRowCount(); row++) {
            for (int col = 0; col < m.getColumnsCount(); col++) {
                System.out.printf("%+.4f\t", m.getValues()[row][col]);
            }
            System.out.print("\n");
        }
    }

}
