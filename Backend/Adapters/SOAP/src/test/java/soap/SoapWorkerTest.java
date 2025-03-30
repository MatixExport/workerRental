package soap;

import com.example.soap.*;
import endpoints.WorkerEndpoint;
import entities.WorkerEnt;
import exceptions.ResourceNotFoundException;
import exceptions.WorkerRentedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import view.WorkerService;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoapWorkerTest {

    @Mock
    private WorkerService workerService;

    private WorkerEndpoint workerEndpoint;

    @BeforeEach
    void setUp() {
        workerEndpoint = new WorkerEndpoint(workerService);
    }

    @Test
    void getWorkerTest() throws ResourceNotFoundException {
        GetWorkerRequest request = new GetWorkerRequest();
        request.setId(UUID.randomUUID().toString());

        WorkerEnt workerEnt = new WorkerEnt();
        workerEnt.setId(UUID.fromString(request.getId()));
        workerEnt.setName("best worker");

        when(workerService.findById(UUID.fromString(request.getId()))).thenReturn(workerEnt);
        GetWorkerResponse response = workerEndpoint.getWorker(request);
        Assertions.assertEquals("best worker", response.getWorker().getName());
    }

    @Test
    void getWorkerWhenNotExistsTest() throws ResourceNotFoundException {
        GetWorkerRequest request = new GetWorkerRequest();
        request.setId(UUID.randomUUID().toString());

        when(workerService.findById(UUID.fromString(request.getId()))).thenThrow(new ResourceNotFoundException());
        GetWorkerResponse response = workerEndpoint.getWorker(request);
        Assertions.assertNull(response.getWorker());
    }

    @Test
    void createWorkerTest() {
        CreateWorkerRequest request = new CreateWorkerRequest();
        request.setName("best worker");

        WorkerEnt worker = new WorkerEnt();
        worker.setName("best worker");
        worker.setId(UUID.randomUUID());

        when(workerService.save(new WorkerEnt("best worker"))).thenReturn(worker);
        CreateWorkerResponse response = workerEndpoint.createWorker(request);
        Assertions.assertEquals("best worker", response.getWorker().getName());
    }

    @Test
    void updateWorkerTest() {
        UpdateWorkerRequest request = new UpdateWorkerRequest();
        request.setName("best worker");
        request.setId(UUID.randomUUID().toString());

        WorkerEnt worker = new WorkerEnt();
        worker.setName("best worker updated");
        worker.setId(UUID.fromString(request.getId()));

        when(workerService.save(worker)).thenReturn(worker);
        UpdateWorkerResponse response = workerEndpoint.updateWorker(request);
        Assertions.assertEquals("best worker updated", response.getWorker().getName());
    }

    @Test
    void deleteWorkerTest() throws WorkerRentedException, ResourceNotFoundException {
        DeleteWorkerRequest request = new DeleteWorkerRequest();
        request.setId(UUID.randomUUID().toString());

        doNothing().when(workerService).delete(Mockito.any(UUID.class));
        DeleteWorkerResponse response = workerEndpoint.deleteWorker(request);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void deleteWorkerWhenWorkerRentedTest() throws WorkerRentedException, ResourceNotFoundException {
        DeleteWorkerRequest request = new DeleteWorkerRequest();
        request.setId(UUID.randomUUID().toString());

        doThrow(new WorkerRentedException()).when(workerService).delete(Mockito.any(UUID.class));
        DeleteWorkerResponse response = workerEndpoint.deleteWorker(request);
        Assertions.assertFalse(response.isSuccess());
    }

    @Test
    void deleteWorkerWhenWorkerNotFoundTest() throws WorkerRentedException, ResourceNotFoundException {
        DeleteWorkerRequest request = new DeleteWorkerRequest();
        request.setId(UUID.randomUUID().toString());

        doThrow(new ResourceNotFoundException()).when(workerService).delete(Mockito.any(UUID.class));
        DeleteWorkerResponse response = workerEndpoint.deleteWorker(request);
        Assertions.assertFalse(response.isSuccess());
    }
}
