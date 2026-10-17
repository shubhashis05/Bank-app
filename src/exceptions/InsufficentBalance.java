package exceptions;

public class InsufficentBalance extends RuntimeException{
    public InsufficentBalance(String message){
        super(message);
    }
}
