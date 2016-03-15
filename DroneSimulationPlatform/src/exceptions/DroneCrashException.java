package exceptions;

public class DroneCrashException extends Exception {
    public DroneCrashException(){}

    public DroneCrashException(String message){
        super(message);
    }
}
