package server.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/29/13
 * Time: 1:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddTrainException extends EntityUpdateException{

    public AddTrainException(String mes) {
        super(mes);
    }
}
