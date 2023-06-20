package fr.kaeios.matrix.suppliers;

import fr.kaeios.api.matrix.CoefficientSupplier;

public class DefaultSuppliers {

    public static final CoefficientSupplier IDENTITY = new IdentityCoefficientSupplier();
    public static final CoefficientSupplier NULL = new NullCoefficientSupplier();

}
