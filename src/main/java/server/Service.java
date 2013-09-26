package server;

import dao.TicketDAO;
import dto.OrderDTO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import entity.Passenger;
import org.apache.log4j.Logger;
import protocol.*;
import entity.StationInRoute;
import dao.PassengerDAO;
import dao.StationInRouteDAO;
import dao.TrainDAO;
import server.exceptions.AbstractBookingTicketException;
import server.exceptions.LessThanTenMinutesBeforeDeparture;
import server.exceptions.NoVacanciesException;
import server.exceptions.PassengerAlreadyRegisteredException;

import static protocol.Constants.HOUR;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * It encapsulates methods which execute all possible services provided by db for clients
 */
public class Service {
    private static final Logger log = Logger.getLogger(Service.class);

    // Entity manager factory is single for every particular service method.
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY;

    static {
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("org.hibernate.ejb.HibernatePersistence");
    }

    public static void shutDownEntityManagerFactory() {
        ENTITY_MANAGER_FACTORY.close();
    }

    /**
     * Method determines which kind of service should be execute. It depends on parameter is passed in this
     * method. And after analyse, execute needed service method.
     * @param reqObj - instructions and required data to execute appropriate service, passed from client.
     * @return Data which are requested by user.
     */
    public static ResponseDTO execute(RequestDTO reqObj) throws AbstractBookingTicketException {
        //Determines what kind of server was required.
        Constants.ClientService reqService = reqObj.getService();

        if (reqService == Constants.ClientService.getScheduleFromAtoB)     return scheduleFromAtoB(reqObj);
        else if (reqService == Constants.ClientService.scheduleForStation) return scheduleForStation(reqObj);
        else if (reqService == Constants.ClientService.buyTicket)     return bookTicket(reqObj);
        else if (reqService == Constants.ClientService.addEntity)          return addNewEntity();
        else if (reqService == Constants.ClientService.viewPassangers)     return viewPassengersOnTrain();
        else if (reqService == Constants.ClientService.viewTrains)         return viewTrains();
        else if (reqService == Constants.ClientService.saleTicket)         return saleTicket();
        else return null;
    }

    /**
     * Service which allows user to book a place in train.
     * @param reqObj - DTO object from user with parameters of order.
     * @return instance of ResponseDTO contains status of service executing and some auxiliary data.
     * @throws NoVacanciesException - if no available vacancies in requested train.
     * @throws PassengerAlreadyRegisteredException - passangers with such data has already been registered.
     * @throws LessThanTenMinutesBeforeDeparture - if less than 10 minutes before train departure.
     */
    private static ResponseDTO bookTicket(RequestDTO reqObj) throws AbstractBookingTicketException {
        log.debug("BookTicket() start executing");

        PassengerDAO passenger = new PassengerDAO(ENTITY_MANAGER_FACTORY);
        TicketDAO ticket = new TicketDAO(ENTITY_MANAGER_FACTORY);
        TrainDAO train = new TrainDAO(ENTITY_MANAGER_FACTORY);

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
        } catch (AbstractBookingTicketException e) {
            log.error("Incorrect user order: " + e.getMessage());
            return new ResponseDTO(Constants.StatusOfExecutedService.error, e);
        }
    }


    /**
     * Get trains schedule from station A to station B in the given time interval. All required data for this service
     * is encapsulated in parameter reqObj.
     * @param reqObj
     * @return Train schedule as list of ScheduleDTO objects.
     */
    private static ResponseDTO scheduleFromAtoB(RequestDTO reqObj) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        entityManager.getTransaction().begin();

        ScheduleDTO scheduleObj = (ScheduleDTO) reqObj.getObject();
        List<StationInRoute[]> resultList = entityManager.createQuery(
                "select s1, s2, s3, s4 from StationInRoute s1, StationInRoute s2, StationInRoute s3, StationInRoute s4 " +
                "where s1.name = ?1 and s2.name = ?2 and s1.id.routeId = s2.id.routeId and " +
                "s1.id.numberInRoute < s2.id.numberInRoute and s1.departureTime > ?3 and " +
                "s1.departureTime < ?4 and " +
                "s3.id.routeId = s1.id.routeId and s3.id.numberInRoute = 1 and " +
                "s4.id.routeId = s1.id.routeId and s4.id.numberInRoute = (select max(s5.id.numberInRoute) from StationInRoute s5 " +
                "                                                         where s5.id.routeId = s4.id.routeId)"
        ).setParameter(1, scheduleObj.getFromStation())
         .setParameter(2, scheduleObj.getToStation())
         .setParameter(3, scheduleObj.getDepartureTime())
         .setParameter(4, scheduleObj.getArrivalTime())
         .getResultList();


        // Packaging response list of objects based on data retrieved from data base.
        int size = resultList.size();
        ArrayList<ScheduleDTO> responseList = new ArrayList<ScheduleDTO>(size);
        for (int i = 0; i < size; i++) {
            Object[] stationsBuf = resultList.get(i);
            responseList.add(new ScheduleDTO(((StationInRoute) stationsBuf[0]).getTrain().getNumber(),
                    ((StationInRoute) stationsBuf[2]).getName(),
                    ((StationInRoute) stationsBuf[3]).getName(),
                    ((StationInRoute) stationsBuf[0]).getDepartureTime(),
                    ((StationInRoute) stationsBuf[1]).getArrivalTime(),
                    ((StationInRoute) stationsBuf[0]).getTrain().getVacancies()));
        }

        return new ResponseDTO(Constants.StatusOfExecutedService.success, responseList);
    }

    public static void main(String[] args) {
        RequestDTO req = new RequestDTO(Constants.ClientService.getScheduleFromAtoB, new ScheduleDTO(0,
                                                                                                     "Псков",
                                                                                                     "Москва",
                                                                                                     new Time(HOUR),
                                                                                                     new Time(18*HOUR),
                                                                                                     0));
        scheduleFromAtoB(req);
    }

    /**
     * Get schedule for station.
     * @param reqObj - this object encapsulate required data to execute service.
     * @return Train schedule as list of ScheduleDTO objects.
     */
    private static ResponseDTO scheduleForStation(RequestDTO reqObj) {
        EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        entityManager.getTransaction().begin();

        ScheduleDTO scheduleObj = (ScheduleDTO) reqObj.getObject();
        List<Object[]> resultList = entityManager.createQuery(
                "select s1.train.number, s1.name, s2.name, s3.departureTime, s3.arrivalTime, t.vacancies " +
                "from StationInRoute s1, StationInRoute s2, StationInRoute s3, Train t " +
                "where s1.id.routeId = s2.id.routeId and s1.id.routeId = s3.id.routeId and " +
                "      s1.id.numberInRoute = 1 and " +
                "      s2.id.numberInRoute = (select max(s4.id.numberInRoute) from StationInRoute s4" +
                "                             where s4.id.routeId = s1.id.routeId) and " +
                "      s1.id.routeId in (select s5.id.routeId from StationInRoute s5" +
                "                        where s5.name = ?1) and" +
                "      s3.id.numberInRoute = (select s6.id.numberInRoute from StationInRoute s6" +
                "                             where s6.id.routeId = s3.id.routeId and s6.name = ?1) and " +
                "      t.number = s1.train.number"
        ).setParameter(1, scheduleObj.getFromStation())
         .getResultList();

        int size = resultList.size();
        ArrayList<ScheduleDTO> responseList = new ArrayList<ScheduleDTO>(size);
        for (int i = 0; i < size; i++) {
            Object[] arr = resultList.get(i);
            responseList.add(new ScheduleDTO((Integer) arr[0],
                                                (String) arr[1],
                                                (String) arr[2],
                                                (Time) arr[3],
                                                (Time) arr[4],
                                                (Integer) arr[5]));
        }

        return new ResponseDTO(Constants.StatusOfExecutedService.success, responseList);
    }


    private static ResponseDTO saleTicket() {
        return new ResponseDTO();
    }

    private static ResponseDTO addNewEntity() {

        return new ResponseDTO();
    }


    private static ResponseDTO viewPassengersOnTrain() {
        return new ResponseDTO();
    }

    private static ResponseDTO viewTrains() {
        return new ResponseDTO();
    }

    /**
     * This method compares two dates only by time and returns difference. Day of month, month and year don't included
     * in comparison.
     * @param date1 - first date
     * @param date2 - second date
     * @return 0 if time of two dates is equal by time, any negative - time of first date is less than second date's
     * time, any positive first date's time is bigger then second.
     */
    private static long compareOnlyTime(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        cal2.set(Calendar.DAY_OF_MONTH, cal1.get(Calendar.DAY_OF_MONTH));
        cal2.set(Calendar.MONTH, cal1.get(Calendar.MONTH));
        cal2.set(Calendar.YEAR, cal1.get(Calendar.YEAR));

        return cal2.getTimeInMillis() - cal1.getTimeInMillis();
    }

    /**
     * Method check possibility of registration new passenger to appropriate train.
     * @param order - parameters of user's order.
     * @throws NoVacanciesException - if no available vacancies in requested train.
     * @throws PassengerAlreadyRegisteredException - passangers with such data has already been registered.
     * @throws LessThanTenMinutesBeforeDeparture - if less than 10 minutes before train departure.
     */
    private static void verifyOrder(OrderDTO order) throws NoVacanciesException,
                                                           PassengerAlreadyRegisteredException,
                                                           LessThanTenMinutesBeforeDeparture {
        log.debug("verifyOrder() start executing");
        TrainDAO train = new TrainDAO(ENTITY_MANAGER_FACTORY);
        PassengerDAO passenger = new PassengerDAO(ENTITY_MANAGER_FACTORY);
        StationInRouteDAO station = new StationInRouteDAO(ENTITY_MANAGER_FACTORY);

        //There's should be vacancy in desired train
        if ( train.getVacanciesInTrain(order.getTrainNumber()) < 1)
            throw new NoVacanciesException("В выбранном поезде свободных мест нет");

        //User can't purchase a ticket on the same train twice.
        if ( passenger.isPassengerAlreadyRegistered(order) )
            throw new PassengerAlreadyRegisteredException("Пассажир с указанными данными уже зарегистрирован на выбранный поезд");

        //User can purchase a ticket if it's more than 10 minutes before train departure.
        //Computation of time before departure.
        long dif = compareOnlyTime(new Date(System.currentTimeMillis()),
                                   new Date(station.getDepartureTime(order.getTrainNumber(), order.getFromStation()).getTime()));
        /*if (dif < 10*MINUTE)
            throw new LessThanTenMinutesBeforeDeparture("До отправления поезда меньше десяти минут");*/
        log.debug("verifyOrder() finished");
    }
}
