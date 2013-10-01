package server.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 10/1/13
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoPassengersException extends Exception {
    public NoPassengersException(String mes) {
        super(mes);
    }
}
