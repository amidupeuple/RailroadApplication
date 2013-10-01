package server.services;

import static protocol.Constants.MINUTE;

import dao.PassengerDAO;
import dao.StationInRouteDAO;
import dao.TicketDAO;
import dao.TrainDAO;
import dto.OrderDTO;
import dto.RequestDTO;
import dto.ResponseDTO;
import entity.Passenger;
import org.apache.log4j.Logger;
import protocol.Constants;
import server.exceptions.BookingTicketException;

import javax.persistence.EntityManagerFactory;
import java.sql.Date;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/28/13
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class TicketService {

    private static final Logger log = Logger.getLogger(TicketService.class);

    private EntityManagerFactory entityManagerFactory;

    public TicketService(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    /**
     * Service which allows user to book a place in train.
     * @param reqObj - DTO object from user with parameters of order.
     * @return instance of ResponseDTO contains status of service executing and some auxiliary data.
     * @throws
     */
    public ResponseDTO bookTicket(RequestDTO reqObj) throws BookingTicketException {
        log.debug("BookTicket() start executing");

        PassengerDAO passenger = new PassengerDAO(entityManagerFactory);
        TicketDAO ticket = new TicketDAO(entityManagerFactory);
        TrainDAO train = new TrainDAO(entityManagerFactory);

        //Order's details from client encapsulated in OrderDTO instance
        OrderDTO order = (OrderDTO) reqObj.getObject();

        try {
            //Verify parameters of user's order.
            verifyOrder(order);
            log.debug("User's order is passed verifying");

            //If conditions are correct -> add new passenger and decrement train vacancies.
            Passenger tmpPassenger = passenger.getPassenger(order.getFirstName(),
                                                            order.getSecondName(),
                                                            order.getDateOfBirth());
            if (tmpPassenger == null) tmpPassenger = passenger.addPassenger(order);

            ticket.addNewTicket(train.getTrain(order.getTrainNumber()), tmpPassenger);
            train.decrementVacancies(order.getTrainNumber());

            log.debug("New passenger is added and vacancies in appropriate train are decremented. " +
                    "bookTicket() successfully finished.");

            return new ResponseDTO(Constants.StatusOfExecutedService.success, "Вы успешно зарегистрировались на поезд!");
        } catch (BookingTicketException e) {
            log.error("Incorrect user order: " + e.getMessage());
            return new ResponseDTO(Constants.StatusOfExecutedService.error, e);
        }
    }

    /**
     * Method check possibility of registration new passenger to appropriate train.
     * @param order - parameters of user's order.
     * @throws
     */
    private void verifyOrder(OrderDTO order) throws BookingTicketException {
        log.debug("verifyOrder() start executing");
        TrainDAO train = new TrainDAO(entityManagerFactory);
        PassengerDAO passenger = new PassengerDAO(entityManagerFactory);
        StationInRouteDAO station = new StationInRouteDAO(entityManagerFactory);

        //There's should be vacancy in desired train
        if ( train.getVacanciesInTrain(order.getTrainNumber()) < 1)
            throw new BookingTicketException("В выбранном поезде свободных мест нет");

        //User can't purchase a ticket on the same train twice.
        if ( passenger.isPassengerAlreadyRegistered(order) )
            throw new BookingTicketException("Пассажир с указанными данными\nуже зарегистрирован на выбранный поезд");

        //User can purchase a ticket if it's more than 10 minutes before train departure.
        //Computation of time before departure.
        long dif = compareOnlyTime(new Date(System.currentTimeMillis()),
                new Date(station.getDepartureTime(order.getTrainNumber(), order.getFromStation()).getTime()));
        /*if (dif < 10*MINUTE)
            throw new BookingTicketException("До отправления поезда\nменьше десяти минут");*/
        log.debug("verifyOrder() finished");
    }

    /**
     * This method compares two dates only by time and returns difference. Day of month, month and year don't included
     * in comparison.
     * @param date1 - first date
     * @param date2 - second date
     * @return 0 if time of two dates is equal by time, any negative - time of first date is less than second date's
     * time, any positive first date's time is bigger then second.
     */
    private long compareOnlyTime(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        cal2.set(Calendar.DAY_OF_MONTH, cal1.get(Calendar.DAY_OF_MONTH));
        cal2.set(Calendar.MONTH, cal1.get(Calendar.MONTH));
        cal2.set(Calendar.YEAR, cal1.get(Calendar.YEAR));

        return cal2.getTimeInMillis() - cal1.getTimeInMillis();
    }

}
