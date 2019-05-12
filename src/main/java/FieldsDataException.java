public class FieldsDataException extends Exception{
    private String message;

    FieldsDataException(String massage){
        this.message = massage;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
