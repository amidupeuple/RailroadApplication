package server.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/24/13
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class LessThanTenMinutesBeforeDeparture extends AbstractBookingTicketException {

    public LessThanTenMinutesBeforeDeparture(String mes) {
        super(mes);
    }
}
