package protocol;

import java.io.Serializable;

/**
 * Request data from client to db. It transmitted to db through serialization with nio. It encapsulates
 * type of desired service and parameters for executing this service.
 */
public class RequestObject implements Serializable{
    Constants.ClientService service;    //Service type

    Object object;                      //Parameters for service

    public RequestObject(Constants.ClientService ser, Object obj) {
        service = ser;
        object = obj;
    }

    public Constants.ClientService getService() {
        return service;
    }

    public Object getObject() {
        return object;
    }
}
