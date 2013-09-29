package server.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/29/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoSuchStationNameException extends EntityUpdateException {
    public NoSuchStationNameException(String mes) {
        super(mes);
    }
}
