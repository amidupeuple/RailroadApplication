package dto;

import common.Constants;

import java.io.Serializable;

/**
 * Request data from client to server. It transmitted to server through serialization with nio. It encapsulates
 * type of desired service and parameters for executing this service.
 */
public class RequestDTO implements Serializable{
    Constants.ClientService service;    //Service type

    Object object;                      //Parameters for service

    public RequestDTO() {}

    public RequestDTO(Constants.ClientService ser, Object obj) {
        service = ser;
        object = obj;
    }

    public Constants.ClientService getService() {
        return service;
    }

    public Object getObject() {
        return object;
    }

    public void setService(Constants.ClientService service) {
        this.service = service;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
