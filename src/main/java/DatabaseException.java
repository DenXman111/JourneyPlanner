public class DatabaseException extends Exception{
    private String massage;

    public DatabaseException(String massage){
        this.massage = massage;
    }

    public String getMassage() {
        return massage;
    }

}
