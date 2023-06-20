package fr.kaeios.matrix.suppliers;

import fr.kaeios.api.matrix.CoefficientSupplier;

public class IdentityCoefficientSupplier implements CoefficientSupplier {

    @Override
    public Double compute(Integer row, Integer column) {
        return row.equals(column) ? 1.0D : 0.0D;
    }

}
