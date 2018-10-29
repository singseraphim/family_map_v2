package Server.Services.Load;

/**
 * Contains a return message used by the Server.Services.Load.load() method.
 * Will indicate whether or not the load was successful.
 */
public class LoadResponse {
    public String message;
    public boolean success = true;

}
