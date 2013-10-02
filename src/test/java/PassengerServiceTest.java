import dao.PassengerDAO;
import dto.PassengerDTO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import common.Constants;
import server.exceptions.NoPassengersException;
import server.services.PassengerService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class contains methods to test PassengerServices functionality.
 */
public class PassengerServiceTest {

    PassengerDAO passengerDAO;
    RequestDTO requestDTO;
    ResponseDTO responseDTO;
    List<PassengerDTO> passengers;

    @Before
    public void setUp() {
        passengerDAO = EasyMock.createMock(PassengerDAO.class);

        requestDTO = new RequestDTO();
        requestDTO.setService(Constants.ClientService.viewPassangers);
        ArrayList<ScheduleDTO> buf = new ArrayList<ScheduleDTO>();
        ScheduleDTO dataFromClient = new ScheduleDTO();
        dataFromClient.setNumber(111);
        buf.add(dataFromClient);
        requestDTO.setObject(buf);

        responseDTO = new ResponseDTO();
        passengers = new ArrayList<PassengerDTO>();
        passengers.add(new PassengerDTO("Ivan", "Ivanov", new Date(Constants.POINT_OF_REFERENCE), 111));
        responseDTO.setStatus(Constants.StatusOfExecutedService.success);
        responseDTO.setObject(passengers);
    }

    @After
    public void tearDown() {
        passengerDAO = null;
    }

    @Test
    public void showPassengerPositiveTest() throws NoPassengersException {
        EasyMock.expect(passengerDAO.getPassengersByTrain(111)).andReturn(passengers);
        EasyMock.replay(passengerDAO);

        ResponseDTO result = PassengerService.showPassengers(requestDTO, passengerDAO);

        assertEquals(responseDTO.getStatus(), result.getStatus());
        assertEquals(responseDTO.getObject(), result.getObject());
    }

    @Test
    public void showPassengersNegativeTest() throws NoPassengersException {
        responseDTO.setStatus(Constants.StatusOfExecutedService.error);
        responseDTO.setObject("На указанном поезде пассажиров нет");

        EasyMock.expect(passengerDAO.getPassengersByTrain(111)).andThrow(new NoPassengersException(
                                                                                 "На указанном поезде пассажиров нет"));
        EasyMock.replay(passengerDAO);

        ResponseDTO result = PassengerService.showPassengers(requestDTO, passengerDAO);

        assertEquals(responseDTO.getStatus(), result.getStatus());
        assertEquals(responseDTO.getObject(), responseDTO.getObject());
    }

}
