package indie.outsource.WorkerRental.controllers;

import indie.outsource.WorkerRental.exceptions.ResourceNotFoundException;
import indie.outsource.WorkerRental.model.Worker;
import indie.outsource.WorkerRental.dtoMappers.WorkerMapper;
import indie.outsource.WorkerRental.exceptions.WorkerRentedException;
import indie.outsource.WorkerRental.services.WorkerService;
import indie.outsource.WorkerRental.DTO.worker.CreateWorkerDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.util.UUID;

@Path("")
public class WorkerController {

    @Inject
    WorkerService workerService;

    @GET()
    @Path("/workers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorker(@PathParam("id") UUID id) {
        try{
            return Response.ok(WorkerMapper.getWorkerDto(workerService.findById(id))).build();
        }
        catch(ResourceNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/workers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllWorkers() {
        return Response.ok(workerService.findAll().stream().map(WorkerMapper::getWorkerDto).toList()).build();
    }

    @POST
    @Path("/workers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createWorker(@Valid CreateWorkerDTO worker) {
        return Response.ok(WorkerMapper.getWorkerDto(workerService.save(WorkerMapper.getWorker(worker)))).build();
    }

    @POST
    @Path("/workers/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") UUID id, CreateWorkerDTO createWorkerDTO) {
        Worker worker = WorkerMapper.getWorker(createWorkerDTO);
        worker.setId(id);
        try{
            return Response.ok(WorkerMapper.getWorkerDto(workerService.updateWorker(worker))).build();
        }
        catch(ResourceNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/workers/{id}")
    public Response deleteWorker(@PathParam("id") UUID id) {
        try{
            workerService.delete(id);
        }
        catch(ResourceNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (WorkerRentedException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }

}
