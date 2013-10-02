package server.services;

import dao.StationInRouteDAO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.apache.log4j.Logger;
import common.Constants;
import server.exceptions.EntityUpdateException;
import server.exceptions.GetScheduleException;

import java.util.List;

/**
 * This class encapsulates services that appeal to station_in_route table in DB.
 */
public class StationInRouteService {

    private static final Logger log = Logger.getLogger(StationInRouteService.class);

    /**
     * Get trains schedule from station A to station B in the given time interval. All required data for this service
     * is encapsulated in parameter reqObj.
     * @param reqObj
     * @return Trains schedule as list of ScheduleDTO objects.
     */
    public static ResponseDTO scheduleFromAtoB(RequestDTO reqObj, StationInRouteDAO stationInRouteDAO) {
        log.debug("Start: scheduleFromAtoB()");
        List<ScheduleDTO> scheduleList;

        ScheduleDTO userRequirements = (ScheduleDTO) reqObj.getObject();

        try {
            scheduleList = stationInRouteDAO.getScheduleFromAtoB(userRequirements);
        } catch (GetScheduleException ex) {
            log.warn("Exception: " + ex.getMessage());
            return new ResponseDTO(Constants.StatusOfExecutedService.error, ex.getMessage());
        }

        log.debug("Finish: scheduleFromAtoB()");
        return new ResponseDTO(Constants.StatusOfExecutedService.success, scheduleList);
    }

    /**
     * Get schedule for station.
     * @param reqObj - this object encapsulate required data to execute service.
     * @return Train schedule as list of ScheduleDTO objects.
     */
    public static ResponseDTO scheduleForStation(RequestDTO reqObj, StationInRouteDAO stationInRouteDAO) {
        log.debug("Start: scheduleForStation()");

        List<ScheduleDTO> scheduleList;

        ScheduleDTO userRequirements = (ScheduleDTO) reqObj.getObject();

        try {
            scheduleList = stationInRouteDAO.getScheduleForStation(userRequirements);
        } catch (GetScheduleException ex) {
            log.warn("Exception: " + ex.getMessage());
            return new ResponseDTO(Constants.StatusOfExecutedService.error, ex.getMessage());
        }

        log.debug("Finish: scheduleForStation()");
        return new ResponseDTO(Constants.StatusOfExecutedService.success, scheduleList);
    }

    /**
     * This service add in DB new route, defines stations that make up this route, set particular train to this route.
     * @param reqObj - it contains as object list of ScheduleDTO instances - stations in new route.
     * @return result of this service: state and explanatory message.
     */
    public static ResponseDTO addRoute(RequestDTO reqObj, StationInRouteDAO stationInRouteDAO) {
        log.debug("Start: addRoute()");

        List<ScheduleDTO> userRequirements = (List<ScheduleDTO>) reqObj.getObject();

        try {
            stationInRouteDAO.addRoute(userRequirements);
        } catch (EntityUpdateException e) {
            log.warn("Exception: " + e.getMessage());
            return new ResponseDTO(Constants.StatusOfExecutedService.error, e.getMessage());
        }

        log.debug("Finish: addRoute()");
        return new ResponseDTO(Constants.StatusOfExecutedService.success, "Новый маршрут добавлен успешно");
    }
}


