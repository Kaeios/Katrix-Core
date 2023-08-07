# KatrixCore
A matrix computing library, designed with modularity and code readability in mind. Written in Java.

---

## Usage :

Creating matrix : 

```JAVA
// Using 2D arrays
Matrix matrixA = new MatrixImpl(
        new Double[][]{
        {-1.0D, 0.0D, 0.0D},
        {1.0D, -2.0D, 0.0D},
        {0.0D, 2.0D, 0.0D},
    }
);

// Using coefficient suppliers
int rowSize = 3;
int colSize = 3;

Matrix matrixB = new MatrixImpl(rowSize, colSize, DefaultSuppliers.IDENTITY);
```
Applying operations to matrix
 [(full list available here)](https://github.com/Kaeios/Katrix-Core/blob/master/src/main/java/fr/kaeios/matrix/operator/MatrixOperators.java)

```
Matrix matrixA = ...;
Matrix matrixB = ...;

// Binary operator (2 operands)
Matrix result = matrixA.apply(matrixB, MatrixOperators.ADD);

// Unary operator (1 operand)
Matrix inverse = matrixB.apply(MatrixOperators.INV)
```
Combining operations allows to solve complex problems (see package fr.kaeios.solver for examples).

Results of DifferentialSystem solver can be plotted using FunctionPlotter class, [example available here](https://github.com/Kaeios/Katrix-Core/blob/master/src/main/java/fr/kaeios/KatrixMain.java).
