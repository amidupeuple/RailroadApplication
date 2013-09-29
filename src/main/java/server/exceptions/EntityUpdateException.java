package server.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/29/13
 * Time: 1:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class EntityUpdateException extends Exception{
    public EntityUpdateException(String mes) {
        super(mes);
    }
}
