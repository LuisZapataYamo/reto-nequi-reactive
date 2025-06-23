package co.com.bancolombia.model.enums;


public enum FranchiseExceptionMessage implements IExceptionMessage {
    FRANCHISE_WITH_NAME_EXIST("Franquicia con este nombre ya existe", "400");

    private final String message;
    private final String code;

    FranchiseExceptionMessage(String s, String number) {
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
