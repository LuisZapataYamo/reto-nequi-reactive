package co.com.bancolombia.model.enums;


public enum ProductExceptionMessage implements IExceptionMessage {
    BRANCH_NOT_EXISTS("La sucursal con id %s no existe", "400"),
    PRODUCT_WITH_NAME_EXIST("El nombre del producto ya existe", "400");
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
