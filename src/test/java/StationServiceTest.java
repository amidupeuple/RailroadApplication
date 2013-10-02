import dao.StationDAO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import common.Constants;
import server.exceptions.EntityUpdateException;
import server.services.StationService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests to test StationServer functionality.
 */
public class StationServiceTest {
    StationDAO stationDAO;
    RequestDTO request;
    List<ScheduleDTO> buf;
    ResponseDTO response;

    @Before
    public void setUp() {
        stationDAO = EasyMock.createMock(StationDAO.class);

        request = new RequestDTO();
        request.setService(Constants.ClientService.addStation);
        buf = new ArrayList<ScheduleDTO>();
        ScheduleDTO reqData = new ScheduleDTO();
        reqData.setFromStation("Минск");
        buf.add(reqData);
        request.setObject(buf);

        response = new ResponseDTO();
        response.setStatus(Constants.StatusOfExecutedService.success);

    }

    @After
    public void tearDown() {
        stationDAO = null;
    }

    @Test
    public void addStationPositiveTest() throws EntityUpdateException {
        EasyMock.expect(stationDAO.addStation("Минск")).andReturn(true);
        EasyMock.replay(stationDAO);

        ResponseDTO result = StationService.addStation(request, stationDAO);

        assertEquals(response.getStatus(), result.getStatus());
    }

    @Test
    public void addStationNegativeTest() throws EntityUpdateException {
        response.setStatus(Constants.StatusOfExecutedService.error);

        EasyMock.expect((stationDAO.addStation("Минск"))).andThrow(new EntityUpdateException(
                                                                   "Ошибка! Станция с таким названием уже существует"));
        EasyMock.replay(stationDAO);

        ResponseDTO result = StationService.addStation(request, stationDAO);

        assertEquals(response.getStatus(), result.getStatus());
    }

}
