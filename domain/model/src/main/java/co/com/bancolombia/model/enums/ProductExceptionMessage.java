package co.com.bancolombia.model.enums;


public enum ProductExceptionMessage implements IExceptionMessage {
    BRANCH_NOT_EXISTS("La sucursal con id %s no existe", "400"),
    PRODUCT_NOT_EXISTS("El producto con id %s no existe", "400"),
    BRANCHID_NOT_VALID("El id %s de sucursal debe ser un uuid", "400"),
    PRODUCTID_NOT_VALID("El id %s de producto debe ser un uuid", "400"),
    PRODUCT_WITH_NAME_EXIST("El nombre del producto ya existe", "409"),
    UPDATE_PRODUCT_STOCK_NAME_NULL("Debe especificar al menos 'name' o 'stock'", "400"),
    UPDATE_PRODUCT_STOCK_NAME_NOT_CHANGES("Los valores de 'name' o 'stock' son los mismos", "400"),;

    private final String message;
    private final String code;

    ProductExceptionMessage(String s, String number) {
        this.message = s;
        this.code = number;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getHttpCode() {
        return this.code;
    }
}
