package indie.outsource.WorkerRental.controllers;

import indie.outsource.WorkerRental.exceptions.*;
import indie.outsource.WorkerRental.dtoMappers.RentMapper;
import indie.outsource.WorkerRental.services.RentService;
import indie.outsource.WorkerRental.DTO.rent.CreateRentDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Path("")
@AllArgsConstructor
public class RentController {

    @Inject
    RentService rentService;

    @GET
    @Path("/rents/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRent(@PathParam("id") UUID id) {
        try {
            return Response.ok(RentMapper.getRentDTO(rentService.findById(id))).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/rents")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRents() {
        return Response.ok(rentService.findAll().stream().map(RentMapper::getRentDTO).toList()).build();
    }

    @POST
    @Path("/rents/users/{clientId}/workers/{workerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRent(@PathParam("clientId") UUID clientId, @PathParam("workerId") UUID workerId, @Valid CreateRentDTO rent) {
        try {
            return Response.ok(RentMapper.getRentDTO(rentService.createRent(clientId, workerId, rent.getStartDate()))).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (UserInactiveException | WorkerRentedException e) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @POST
    @Path("/rents/{id}/finish")
    @Produces(MediaType.APPLICATION_JSON)
    public Response finishRent(@PathParam("id") UUID id) {
        try {
            return Response.ok(RentMapper.getRentDTO(rentService.endRent(id))).build();
        }
        catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (RentAlreadyEndedException e){
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @DELETE
    @Path("/rents/{id}/delete")
    public Response deleteRent(@PathParam("id") UUID id) {
        try {
            rentService.deleteRent(id);
            return Response.ok("").build();
        }
        catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (RentAlreadyEndedException e){
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @GET
    @Path("/rents/ended/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientEndedRents(@PathParam("id") UUID id) {
        try{
            return Response.ok(rentService.getClientEndedRents(id).stream().map(RentMapper::getRentDTO).toList()).build();
        }
        catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/rents/ended/workers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkerEndedRents(@PathParam("id") UUID id) {
        try{
            return Response.ok(rentService.getWorkerEndedRents(id).stream().map(RentMapper::getRentDTO).toList()).build();
        }
        catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/rents/current/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientActiveRents(@PathParam("id") UUID id) {
        try{
            return Response.ok(rentService.getClientActiveRents(id).stream().map(RentMapper::getRentDTO).toList()).build();
        }
        catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/rents/current/workers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkerActiveRents(@PathParam("id") UUID id) {
        try{
            return Response.ok(rentService.getWorkerActiveRents(id).stream().map(RentMapper::getRentDTO).toList()).build();
        }
        catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
