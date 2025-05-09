package app.helper;


import com.example.soap.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class SoapWorkerClient extends WebServiceGatewaySupport {

    public SoapWorkerClient() {
        this.setDefaultUri("http://localhost:80/ws");
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.example.soap");
        this.setMarshaller(marshaller);
        this.setUnmarshaller(marshaller);
    }

    public GetWorkerResponse getWorker(String id) {
        GetWorkerRequest request = new GetWorkerRequest();
        request.setId(id);

        return (GetWorkerResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }

    public CreateWorkerResponse createWorker(String workerName) {
        CreateWorkerRequest request = new CreateWorkerRequest();
        request.setName(workerName);

        return (CreateWorkerResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }

    public DeleteWorkerResponse deleteWorker(String id) {
        DeleteWorkerRequest request = new DeleteWorkerRequest();
        request.setId(id);
        return (DeleteWorkerResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }

    public UpdateWorkerResponse updateWorker(String id, String name) {
        UpdateWorkerRequest request = new UpdateWorkerRequest();
        request.setId(id);
        request.setName(name);
        return (UpdateWorkerResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }
}
