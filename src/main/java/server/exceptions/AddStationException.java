package server.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/29/13
 * Time: 1:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddStationException extends EntityUpdateException {

    public AddStationException(String mes) {
        super(mes);
    }
}
