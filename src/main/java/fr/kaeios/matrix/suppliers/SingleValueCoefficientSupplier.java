package fr.kaeios.matrix.suppliers;

import fr.kaeios.api.matrix.CoefficientSupplier;

// TODO Refactor with LU
public class SingleValueCoefficientSupplier implements CoefficientSupplier {

    private final Double value;

    public SingleValueCoefficientSupplier(Double value) {
        this.value = value;
    }

    @Override
    public Double compute(Integer row, Integer column) {
        return value;
    }

}
