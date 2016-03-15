package exceptions;

public class OutOfBatteryException extends Exception {
    public OutOfBatteryException(){ super(); }

    public OutOfBatteryException(String message){ super(message); }
}
