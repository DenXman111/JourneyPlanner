public class FormsCheckException extends Exception{
    private String massage;

    public FormsCheckException(String massage){
        this.massage = massage;
    }

    public String getMassage() {
        return massage;
    }
}
