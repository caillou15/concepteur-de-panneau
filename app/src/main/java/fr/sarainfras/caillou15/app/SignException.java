package fr.sarainfras.caillou15.app;

public class SignException extends Exception{
    public SignException() {super();}

    public SignException(String message) {
        super(message);
    }

    public SignException(Throwable cause) {
        super(cause);
    }

    public SignException(String message, Throwable cause) {
        super(message, cause);
    }
}
