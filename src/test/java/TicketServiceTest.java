import static org.junit.Assert.*;

import dao.PassengerDAO;
import dao.StationInRouteDAO;
import dao.TicketDAO;
import dao.TrainDAO;
import dto.OrderDTO;
import dto.RequestDTO;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import common.Constants;
import server.services.TicketService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 10/2/13
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class TicketServiceTest {

    PassengerDAO passengerDAO;
    TicketDAO ticketDAO;
    TrainDAO trainDAO;
    StationInRouteDAO sirDAO;
    RequestDTO request;
    OrderDTO order;

    @Before
    public void setUp() {
        passengerDAO = EasyMock.createMock(PassengerDAO.class);
        ticketDAO = EasyMock.createMock(TicketDAO.class);
        trainDAO = EasyMock.createMock(TrainDAO.class);
        sirDAO = EasyMock.createMock(StationInRouteDAO.class);

        order = new OrderDTO();
        order.setFirstName("Ivan");
        order.setSecondName("Ivanov");
        order.setDateOfBirth(new Date(10*Calendar.YEAR));
        order.setTrainNumber(111);
        order.setFromStation("Псков");

        request = new RequestDTO();
        request.setService(Constants.ClientService.buyTicket);
        request.setObject(order);
    }

    @After
    public void tearDown() {
        passengerDAO = null;
        ticketDAO = null;
        trainDAO = null;
        sirDAO = null;
    }

    @Test
    @Ignore
    public void verifyOrderPositiveTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        EasyMock.expect(trainDAO.getVacanciesInTrain(order.getTrainNumber())).andReturn(250);
        EasyMock.replay(trainDAO);

        EasyMock.expect(passengerDAO.isPassengerAlreadyRegistered(order)).andReturn(false);
        EasyMock.replay(passengerDAO);

        EasyMock.expect(sirDAO.getDepartureTime(order.getTrainNumber(), order.getFromStation())).andReturn(new Time(23*Calendar.HOUR));
        EasyMock.replay();

        Method method = TicketService.class.getDeclaredMethod("verifyOrder", OrderDTO.class, PassengerDAO.class, StationInRouteDAO.class, TrainDAO.class);
        method.setAccessible(true);
        Boolean result = (Boolean) method.invoke(new TicketService(), order, passengerDAO, sirDAO, trainDAO);

        assertTrue(result);
    }


}
