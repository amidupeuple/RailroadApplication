package server.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/24/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoVacanciesException extends AbstractBookingTicketException {

    public NoVacanciesException(String mes) {
        super(mes);
    }
}
