package server.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/29/13
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoSuchTrainException extends EntityUpdateException {
    public NoSuchTrainException(String mes) {
        super(mes);
    }
}
