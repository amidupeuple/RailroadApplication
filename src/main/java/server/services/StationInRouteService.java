package server.services;

import dao.StationInRouteDAO;
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
 * Date: 9/28/13
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
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
        log.debug("Start method scheduleFromAtoB(...)");
        StationInRouteDAO stationInRouteDAO = new StationInRouteDAO(entityManagerFactory);

        ScheduleDTO userRequirements = (ScheduleDTO) reqObj.getObject();

        List<ScheduleDTO> scheduleList = stationInRouteDAO.getScheduleFromAtoB(userRequirements.getFromStation(),
                                                                               userRequirements.getToStation(),
                                                                               userRequirements.getDepartureTime(),
                                                                               userRequirements.getArrivalTime());

        log.debug("Finish method scheduleFromAtoB(...)");
        return new ResponseDTO(Constants.StatusOfExecutedService.success, scheduleList);
    }

    /**
     * Get schedule for station.
     * @param reqObj - this object encapsulate required data to execute service.
     * @return Train schedule as list of ScheduleDTO objects.
     */
    public ResponseDTO scheduleForStation(RequestDTO reqObj) {
        StationInRouteDAO stationInRouteDAO = new StationInRouteDAO(entityManagerFactory);

        ScheduleDTO userRequirements = (ScheduleDTO) reqObj.getObject();
        List<ScheduleDTO> scheduleList = stationInRouteDAO.getScheduleForStation(userRequirements);

        return new ResponseDTO(Constants.StatusOfExecutedService.success, scheduleList);
    }

    public ResponseDTO addRoute(RequestDTO reqObj) {
        StationInRouteDAO stationInRouteDAO = new StationInRouteDAO(entityManagerFactory);

        List<ScheduleDTO> userRequirements = (List<ScheduleDTO>) reqObj.getObject();

        try {
            stationInRouteDAO.addRoute(userRequirements);
        } catch (EntityUpdateException e) {
            return new ResponseDTO(Constants.StatusOfExecutedService.error, e.getMessage());
        }

        return new ResponseDTO(Constants.StatusOfExecutedService.success, "Новый маршрут добавлен успешно.");
    }
}


