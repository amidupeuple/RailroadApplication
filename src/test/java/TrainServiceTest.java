import static org.junit.Assert.*;

import dao.TrainDAO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import common.Constants;
import server.exceptions.EntityUpdateException;
import server.services.TrainService;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests to test TrainService functionality.
 */
public class TrainServiceTest {
    TrainDAO trainDAO;
    ResponseDTO response;
    RequestDTO request;
    List<ScheduleDTO> buf;

    @Before
    public void setUp() {
        trainDAO = EasyMock.createMock(TrainDAO.class);

        response = new ResponseDTO();
        response.setStatus(Constants.StatusOfExecutedService.success);

        request = new RequestDTO();
        request.setService(Constants.ClientService.addTrain);
        buf = new ArrayList<ScheduleDTO>();
        ScheduleDTO reqData = new ScheduleDTO();
        reqData.setNumber(456);
        reqData.setTicketsAmount(300);
        buf.add(reqData);
        request.setObject(buf);
    }

    @After
    public void tearUp() {
        trainDAO = null;
        response = null;
        request = null;
    }

    @Test
    public void viewAllTrainsDAOPositive() {
        EasyMock.expect(trainDAO.getAllTrains()).andReturn(new ArrayList<ScheduleDTO>());
        EasyMock.replay(trainDAO);

        ResponseDTO result = TrainService.viewAllTrains(trainDAO);

        assertEquals(response.getStatus(), result.getStatus());
    }

    @Test
    public void addTrainPositiveTest() throws EntityUpdateException {
        EasyMock.expect(trainDAO.addTrain(456, 300)).andReturn(true);
        EasyMock.replay(trainDAO);

        ResponseDTO result = TrainService.addTrain(request, trainDAO);

        assertEquals(response.getStatus(), result.getStatus());
    }

    @Test
    public void addTrainNegativeTest() throws EntityUpdateException {
        response.setStatus(Constants.StatusOfExecutedService.error);

        EasyMock.expect(trainDAO.addTrain(456, 300)).andThrow(new EntityUpdateException("Ошибка! Такой поезд уже существует."));
        EasyMock.replay(trainDAO);

        ResponseDTO result = TrainService.addTrain(request, trainDAO);

        assertEquals(response.getStatus(), result.getStatus());
    }

}
