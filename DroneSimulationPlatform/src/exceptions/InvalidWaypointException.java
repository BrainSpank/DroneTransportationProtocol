package exceptions;

public class InvalidWaypointException extends Exception {
    public InvalidWaypointException(){
        super();
    }

    public InvalidWaypointException(String message) { super(message); }
}
