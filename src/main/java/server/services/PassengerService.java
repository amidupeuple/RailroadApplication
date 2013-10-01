package server.services;

import dao.PassengerDAO;
import dto.PassengerDTO;
import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import org.apache.log4j.Logger;
import protocol.Constants;
import server.exceptions.NoPassengersException;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: danya_000
 * Date: 9/29/13
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PassengerService {
    private static final Logger log = Logger.getLogger(StationInRouteService.class);

    private EntityManagerFactory entityManagerFactory;

    public PassengerService(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    public ResponseDTO showPassengers(RequestDTO reqObj) {
        PassengerDAO passengerDAO = new PassengerDAO(entityManagerFactory);
        List<PassengerDTO> passengers;

        int trainNumber = ((List<ScheduleDTO>)reqObj.getObject()).get(0).getNumber();

        try {
            passengers = passengerDAO.getPassengersByTrain(trainNumber);
        } catch (NoPassengersException ex) {
            return new ResponseDTO(Constants.StatusOfExecutedService.error, ex.getMessage());
        }

        return new ResponseDTO(Constants.StatusOfExecutedService.success, passengers);
    }
}
