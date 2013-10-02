package dao;

import dto.ScheduleDTO;
import entity.Train;
import org.apache.log4j.Logger;
import server.exceptions.EntityUpdateException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Train entity.
 */
public class TrainDAO {
    private static final Logger log = Logger.getLogger(TrainDAO.class);

    private EntityManagerFactory entityManagerFactory;

    public TrainDAO(EntityManagerFactory factory) {
        entityManagerFactory = factory;
    }

    /**
     * Get amount of vacancies in train by train number.
     * @param numb - number of desired train.
     * @return amount of vacancies int train.
     */
    public int getVacanciesInTrain(int numb) {
        log.debug("Start: getVacanciesInTrain()");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Integer> vacancies = entityManager.createQuery(
                "select t.vacancies from Train t " +
                "where t.number = ?1"
        ).setParameter(1, numb)
         .getResultList();

        entityManager.getTransaction().commit();

        log.debug("Finish: getVacanciesInTrain()");
        return vacancies.get(0);
    }

    public Train getTrain(int number) {
        log.debug("Start: getTrain()");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        List<Train> buf = entityManager.createQuery(
                "select t from Train t " +
                        "where t.number = ?1"
        ).setParameter(1, number).getResultList();

        entityManager.getTransaction().commit();

        log.debug("Finish: getTrain()");
        return buf.get(0);
    }

    public void decrementVacancies(int trainNumber) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Query decrementVacanciesQuery = entityManager.createQuery(
                "update Train t " +
                "set t.vacancies = (t.vacancies - 1) " +
                "where t.number = ?1").setParameter(1, trainNumber);

        decrementVacanciesQuery.executeUpdate();

        entityManager.getTransaction().commit();
    }

    public boolean addTrain(int number, int vacancies) throws EntityUpdateException{
        log.debug("Start: addTrain()");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Train newTrain = new Train(number, vacancies);

        entityManager.persist(newTrain);
        try {
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            log.warn("Such train already exists");
            throw new EntityUpdateException("Ошибка! Такой поезд уже существует");
        }

        log.debug("Finish: addTrain()");
        return true;
    }

    public ArrayList<ScheduleDTO> getAllTrains() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        ArrayList<ScheduleDTO> trainsList = new ArrayList<ScheduleDTO>();

        List<Object[]> bufTrainsInRoutes = entityManager.createQuery(
                "select tr.number, sir1.station.name, sir2.station.name, sir1.departureTime," +
                "       sir2.arrivalTime, tr.vacancies " +
                "from Train tr, StationInRoute sir1, StationInRoute sir2  " +
                "where tr.id = sir1.train.id and tr.id = sir2.train.id and " +
                "      sir1.departureTime = (select min(sir3.departureTime) " +
                "                            from StationInRoute sir3 " +
                "                            where sir3.route.id = sir1.route.id) and " +
                "      sir2.arrivalTime = (select max(sir4.arrivalTime) " +
                "                          from StationInRoute sir4 " +
                "                          where sir4.route.id = sir2.route.id)"
        ).getResultList();

        List<Train> bufTrainsNotInRoutes = entityManager.createQuery(
                "select t from Train t " +
                "where t.id not in (select distinct s.train.id from StationInRoute s)"
        ).getResultList();

        entityManager.getTransaction().commit();

        for (int i = 0; i < bufTrainsInRoutes.size(); i++) {
            Object[] trainInfo = bufTrainsInRoutes.get(i);
            ScheduleDTO train = new ScheduleDTO((Integer) trainInfo[0],
                                                (String) trainInfo[1],
                                                (String) trainInfo[2],
                                                (Time) trainInfo[3],
                                                (Time) trainInfo[4],
                                                (Integer) trainInfo[5]);
            trainsList.add(train);
        }

        for (int i = 0; i < bufTrainsNotInRoutes.size(); i++) {
            Train trainInfo = bufTrainsNotInRoutes.get(i);
            ScheduleDTO train = new ScheduleDTO();
            train.setNumber(trainInfo.getNumber());
            train.setTicketsAmount(trainInfo.getVacancies());
            trainsList.add(train);
        }

        return trainsList;
    }

}
