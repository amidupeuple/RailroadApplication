package dto;

import common.Constants;

import java.io.Serializable;

/**
 * This is response from server to client. It contains status of executed requested service and result of
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

    public void setStatus(Constants.StatusOfExecutedService status) {
        this.status = status;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
