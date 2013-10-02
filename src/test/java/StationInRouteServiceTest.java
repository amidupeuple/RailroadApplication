import dao.StationInRouteDAO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import common.Constants;
import server.exceptions.GetScheduleException;
import server.services.StationInRouteService;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for StationInRouteService functionality.
 */
public class StationInRouteServiceTest {
    StationInRouteDAO sirDAO;
    RequestDTO requestDTO;
    ResponseDTO responseDTO;
    ScheduleDTO requestData;
    List<ScheduleDTO> scheduleList;

    @Before
    public void setUp() {
        sirDAO = EasyMock.createMock(StationInRouteDAO.class);

        requestDTO = new RequestDTO();
        requestDTO.setService(Constants.ClientService.getScheduleFromAtoB);
        requestData = new ScheduleDTO();
        requestData.setFromStation("Псков");
        requestData.setToStation("Москва");
        requestData.setDepartureTime(new Time(Calendar.HOUR));
        requestData.setArrivalTime(new Time(23*Calendar.HOUR));
        requestDTO.setObject(requestData);

        responseDTO = new ResponseDTO();
        responseDTO.setStatus(Constants.StatusOfExecutedService.success);

        scheduleList = new ArrayList<ScheduleDTO>();
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setFromStation("Псков");
        scheduleDTO.setToStation("Москва");
        scheduleDTO.setDepartureTime(new Time(12*Calendar.HOUR));
        scheduleDTO.setArrivalTime(new Time(22*Calendar.HOUR));
        scheduleDTO.setTicketsAmount(250);
        scheduleList.add(scheduleDTO);
    }

    @After
    public void tearDown() {
        sirDAO = null;
        requestDTO = null;
        responseDTO = null;
        requestData = null;
        scheduleList = null;
    }

    @Test
    public void scheduleFromAtoBPositiveTest() throws GetScheduleException {
        EasyMock.expect(sirDAO.getScheduleFromAtoB(requestData)).andReturn(scheduleList);
        EasyMock.replay(sirDAO);

        ResponseDTO result = StationInRouteService.scheduleFromAtoB(requestDTO, sirDAO);

        assertEquals(responseDTO.getStatus(), result.getStatus());
    }

    @Test
    public void scheduleFromAtoBNegativeTest()throws GetScheduleException {
        responseDTO.setStatus(Constants.StatusOfExecutedService.error);
        responseDTO.setObject("Для заданных условий нет расписания поездов");
        EasyMock.expect(sirDAO.getScheduleFromAtoB(requestData)).andThrow(new GetScheduleException(
                                                                       "Для заданных условий нет расписания поездов"));
        EasyMock.replay(sirDAO);

        ResponseDTO result = StationInRouteService.scheduleFromAtoB(requestDTO, sirDAO);

        assertEquals(responseDTO.getStatus(), result.getStatus());
        assertEquals(responseDTO.getObject(), result.getObject());
    }

    @Test
    public void scheduleForStationPositiveTest() throws GetScheduleException {
        EasyMock.expect(sirDAO.getScheduleForStation(requestData)).andReturn(scheduleList);
        EasyMock.replay(sirDAO);

        ResponseDTO result = StationInRouteService.scheduleForStation(requestDTO, sirDAO);

        assertEquals(responseDTO.getStatus(), result.getStatus());
    }

    @Test
    public void scheduleForStationNegativeTest() throws GetScheduleException {
        responseDTO.setStatus(Constants.StatusOfExecutedService.error);
        responseDTO.setObject("Для запрошенной станции нет расписания");

        EasyMock.expect(sirDAO.getScheduleForStation(requestData)).andThrow(new GetScheduleException(
                                                                            "Для запрошенной станции нет расписания"));
        EasyMock.replay(sirDAO);

        ResponseDTO result = StationInRouteService.scheduleForStation(requestDTO, sirDAO);

        assertEquals(responseDTO.getStatus(), result.getStatus());
        assertEquals(responseDTO.getObject(), result.getObject());
    }
}
