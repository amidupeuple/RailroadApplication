package server.services;

import dao.StationInRouteDAO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.apache.log4j.Logger;
import protocol.Constants;
import server.exceptions.EntityUpdateException;
import server.exceptions.GetScheduleException;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * This class encapsulates services that appeal to station_in_route table in DB.
 */
public class StationInRouteService {

    private static final Logger log = Logger.getLogger(StationInRouteService.class);

    private EntityManagerFactory entityManagerFactory;

    public StationInRouteService(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    /**
     * Get trains schedule from station A to station B in the given time interval. All required data for this service
     * is encapsulated in parameter reqObj.
     * @param reqObj
     * @return Trains schedule as list of ScheduleDTO objects.
     */
    public ResponseDTO scheduleFromAtoB(RequestDTO reqObj) {
        log.debug("Start: scheduleFromAtoB()");
        List<ScheduleDTO> scheduleList;
        StationInRouteDAO stationInRouteDAO = new StationInRouteDAO(entityManagerFactory);

        ScheduleDTO userRequirements = (ScheduleDTO) reqObj.getObject();

        try {
            scheduleList = stationInRouteDAO.getScheduleFromAtoB(userRequirements.getFromStation(),
                                                                               userRequirements.getToStation(),
                                                                               userRequirements.getDepartureTime(),
                                                                               userRequirements.getArrivalTime());
        } catch (GetScheduleException ex) {
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
    public ResponseDTO scheduleForStation(RequestDTO reqObj) {
        List<ScheduleDTO> scheduleList;
        StationInRouteDAO stationInRouteDAO = new StationInRouteDAO(entityManagerFactory);

        ScheduleDTO userRequirements = (ScheduleDTO) reqObj.getObject();

        try {
            scheduleList = stationInRouteDAO.getScheduleForStation(userRequirements);
        } catch (GetScheduleException ex) {
            return new ResponseDTO(Constants.StatusOfExecutedService.error, ex.getMessage());
        }

        return new ResponseDTO(Constants.StatusOfExecutedService.success, scheduleList);
    }

    /**
     * This service add in DB new route, defines stations that make up this route, set particular train to this route.
     * @param reqObj - it contains as object list of ScheduleDTO instances - stations in new route.
     * @return result of this service: state and explanatory message.
     */
    public ResponseDTO addRoute(RequestDTO reqObj) {
        StationInRouteDAO stationInRouteDAO = new StationInRouteDAO(entityManagerFactory);

        List<ScheduleDTO> userRequirements = (List<ScheduleDTO>) reqObj.getObject();

        try {
            stationInRouteDAO.addRoute(userRequirements);
        } catch (EntityUpdateException e) {
            return new ResponseDTO(Constants.StatusOfExecutedService.error, e.getMessage());
        }

        return new ResponseDTO(Constants.StatusOfExecutedService.success, "Новый маршрут добавлен успешно");
    }
}


