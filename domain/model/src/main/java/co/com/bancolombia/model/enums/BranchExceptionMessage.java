package co.com.bancolombia.model.enums;


public enum BranchExceptionMessage implements IExceptionMessage {
    FRANCHISE_NOT_EXISTS("La franquicia con id %s no existe", "404"),
    BRANCH_WITH_NAME_EXIST("Sucursal con este nombre ya existe", "409");
    private final String message;
    private final String code;

    BranchExceptionMessage(String s, String number) {
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
