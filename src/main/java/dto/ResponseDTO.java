package dto;

import protocol.Constants;

import java.io.Serializable;

/**
 * This is response from db to client. It contains status of executed requested service and result of
 * this service.
 */
public class ResponseDTO implements Serializable{
    Constants.StatusOfExecutedService status;   //Status

    Object object;                              //Required data from db ~ result

    public ResponseDTO() {}

    public ResponseDTO(Constants.StatusOfExecutedService st, Object obj) {
        status = st;
        object = obj;
    }

    public Constants.StatusOfExecutedService getStatus() {
        return status;
    }

    public Object getObject() {
        return object;
    }
}
