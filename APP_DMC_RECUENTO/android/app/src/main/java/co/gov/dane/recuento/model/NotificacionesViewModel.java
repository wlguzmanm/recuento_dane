package co.gov.dane.recuento.model;

import java.util.List;

public class NotificacionesViewModel {

    private List<RequestStatusViewModel> requestStatus;

    public List<RequestStatusViewModel> getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(List<RequestStatusViewModel> requestStatus) {
        this.requestStatus = requestStatus;
    }
}
