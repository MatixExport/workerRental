package endpoints;

import entities.WorkerEnt;
import com.example.soap.*;
import exceptions.ResourceNotFoundException;
import exceptions.WorkerRentedException;
import lombok.AllArgsConstructor;
import mappers.WorkerMapper;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import view.WorkerService;

import java.util.UUID;

@Endpoint
@AllArgsConstructor
public class WorkerEndpoint {


    private static final String NAMESPACE_URI = "http://example.com/soap";
    private final WorkerService workerService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllWorkersRequest")
    @ResponsePayload
    public GetAllWorkersResponse getAllWorkers() {
        GetAllWorkersResponse response = new GetAllWorkersResponse();
        response.getWorkers().addAll(
                workerService.findAll()
                        .stream()
                        .map(WorkerMapper::fromDomainModel)
                        .toList()
        );
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getWorkerRequest")
    @ResponsePayload
    public GetWorkerResponse getWorker(@RequestPayload GetWorkerRequest request) {
        GetWorkerResponse response = new GetWorkerResponse();
        try {
            WorkerEnt workerEnt = workerService.findById(UUID.fromString(request.getId()));
            response.setWorker(WorkerMapper.fromDomainModel(workerEnt));
        }catch (ResourceNotFoundException e) {
            response.setWorker(null);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteWorkerRequest")
    @ResponsePayload
    public DeleteWorkerResponse deleteWorker(@RequestPayload DeleteWorkerRequest request) {
        DeleteWorkerResponse response = new DeleteWorkerResponse();
        try {
            workerService.delete(UUID.fromString(request.getId()));
            response.setSuccess(Boolean.TRUE);
        }catch (ResourceNotFoundException | WorkerRentedException e) {
            response.setSuccess(Boolean.FALSE);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createWorkerRequest")
    @ResponsePayload
    public CreateWorkerResponse createWorker(@RequestPayload CreateWorkerRequest request) {
        CreateWorkerResponse response = new CreateWorkerResponse();
        try {
            WorkerEnt worker = new WorkerEnt();
            worker.setName(request.getName());
            response.setWorker(
                    WorkerMapper.fromDomainModel(workerService.save(worker))
            );
        }catch (Exception e) {
            response.setWorker(null);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateWorkerRequest")
    @ResponsePayload
    public UpdateWorkerResponse updateWorker(@RequestPayload UpdateWorkerRequest request) {
        UpdateWorkerResponse response = new UpdateWorkerResponse();
        try {
            WorkerEnt workerEnt = new WorkerEnt();
            workerEnt.setName(request.getName());
            workerEnt.setId(UUID.fromString(request.getId()));
            response.setWorker(
                    WorkerMapper.fromDomainModel(
                            workerService.save(workerEnt)
                    )
            );
        }
        catch (Exception e) {
            response.setWorker(null);
        }
        return response;
    }





}
