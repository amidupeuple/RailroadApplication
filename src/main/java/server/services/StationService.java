package server.services;

import dao.StationDAO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.apache.log4j.Logger;
import protocol.Constants;
import server.exceptions.EntityUpdateException;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/29/13
 * Time: 1:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class StationService {

    private static final Logger log = Logger.getLogger(StationInRouteService.class);

    private EntityManagerFactory entityManagerFactory;

    public StationService(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    public ResponseDTO addStation(RequestDTO reqObj) {
        log.debug("addTrain() start");
        StationDAO stationDAO = new StationDAO(entityManagerFactory);

        List<ScheduleDTO> stationInfo = (List<ScheduleDTO>) reqObj.getObject();

        try {
            stationDAO.addStation(stationInfo.get(0).getFromStation());
        } catch (EntityUpdateException e) {
            log.debug("Exception: station with given name already exist");
            return new ResponseDTO(Constants.StatusOfExecutedService.error, e.getMessage());
        }

        log.debug("addStation() start");
        return new ResponseDTO(Constants.StatusOfExecutedService.success, "Станция успешно добавлена");
    }
}
