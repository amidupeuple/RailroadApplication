package gui;

public class CompleteTrainInformation {
    int number;
    String fromStation;
    String toStation;
    SimpleTime departureTime;
    SimpleTime arrivalTime;
    int ticketsAmount;

    public int getNumber() {
        return number;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public SimpleTime getDepartureTime() {
        return departureTime;
    }

    public SimpleTime getArrivalTime() {
        return arrivalTime;
    }

    public int getTicketsAmount() {
        return ticketsAmount;
    }
    
    public CompleteTrainInformation() {
        number = 123;
        fromStation = "A";
        toStation = "B";
        departureTime = new SimpleTime("00:00");
        arrivalTime = new SimpleTime("08:00");
        ticketsAmount = 100;
    }
}
