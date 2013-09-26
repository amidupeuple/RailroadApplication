package server.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/24/13
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class PassengerAlreadyRegisteredException extends AbstractBookingTicketException {
    public PassengerAlreadyRegisteredException(String mes) {
        super(mes);
    }
}
