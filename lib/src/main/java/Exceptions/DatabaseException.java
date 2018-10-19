package Exceptions;

public class DatabaseException extends Exception {
    public DatabaseException() {}

    // Constructor that accepts a message
    public DatabaseException(String message)
    {
        super(message);
    }
}
