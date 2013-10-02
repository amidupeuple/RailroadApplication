package server.services;

import dao.PassengerDAO;
import dto.PassengerDTO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.apache.log4j.Logger;
import common.Constants;
import server.exceptions.NoPassengersException;

import java.util.List;

/**
 * Services that interact with PassengerDAO instances.
 */
public class PassengerService {
    private static final Logger log = Logger.getLogger(StationInRouteService.class);

    /**
     * This method return all passengers corresponding to particular train.
     * @param reqObj - it contains number of desired train.
     * @param passengerDAO - dao to get data from db
     * @return - ResponceDTO instance which contains list of passengers on the given train.
     */
    public static ResponseDTO showPassengers(RequestDTO reqObj, PassengerDAO passengerDAO) {
        log.debug("Start: showPassengers()");

        List<PassengerDTO> passengers;

        int trainNumber = ((List<ScheduleDTO>)reqObj.getObject()).get(0).getNumber();

        try {
            passengers = passengerDAO.getPassengersByTrain(trainNumber);
        } catch (NoPassengersException ex) {
            log.warn("Exception: no passengers for given train");
            return new ResponseDTO(Constants.StatusOfExecutedService.error, ex.getMessage());
        }

        log.debug("Finish: showPassengers()");

        return new ResponseDTO(Constants.StatusOfExecutedService.success, passengers);
    }
}
