package server.services;

import dao.StationDAO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.apache.log4j.Logger;
import common.Constants;
import server.exceptions.EntityUpdateException;

import java.util.List;

/**
 * Services which interact with StationDAO.
 */
public class StationService {

    private static final Logger log = Logger.getLogger(StationInRouteService.class);

    public static ResponseDTO addStation(RequestDTO reqObj, StationDAO stationDAO) {
        log.debug("addTrain() start");

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
