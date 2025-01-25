package indie.outsource.WorkerRental.controllers;

import indie.outsource.WorkerRental.exceptions.*;
import indie.outsource.WorkerRental.dtoMappers.RentMapper;
import indie.outsource.WorkerRental.model.user.User;
import indie.outsource.WorkerRental.services.RentService;
import indie.outsource.WorkerRental.services.UserService;
import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController()
public class RentController {
    private RentService rentService;
    private UserService userService;

    @GetMapping("/rents/{id}")
    public ResponseEntity<RentDTO> getRent(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(RentMapper.getRentDTO(rentService.findById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/rents")
    public ResponseEntity<List<RentDTO>> getRents() {
        return ResponseEntity.ok(rentService.findAll().stream().map(RentMapper::getRentDTO).toList());
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.WorkerRental.Roles).ADMIN , T(indie.outsource.WorkerRental.Roles).MANAGER)")
    @PostMapping("/rents/users/{clientId}/workers/{workerId}")
    public ResponseEntity<RentDTO> createRent(@PathVariable UUID clientId, @PathVariable UUID workerId, @RequestBody @Valid CreateRentDTO rent) {
        try {
            return ResponseEntity.ok(RentMapper.getRentDTO(rentService.createRent(clientId, workerId, rent.getStartDate())));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (WorkerRentedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch(UserInactiveException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/rents/user/workers/{workerId}")
    public ResponseEntity<RentDTO> createRent(@PathVariable UUID workerId, @RequestBody @Valid CreateRentDTO rent) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) authentication.getDetails();
        User user = userService.findByUsername(userDetails.getUsername()).getFirst();

        return createRent(user.getId(), workerId, rent);
    }


    @PostMapping("/rents/{id}/finish")
    public ResponseEntity<RentDTO> finishRent(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(RentMapper.getRentDTO(rentService.endRent(id)));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (RentAlreadyEndedException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.WorkerRental.Roles).ADMIN , T(indie.outsource.WorkerRental.Roles).MANAGER)")
    @DeleteMapping("/rents/{id}/delete")
    public ResponseEntity<String> deleteRent(@PathVariable UUID id) {
        try {
            rentService.deleteRent(id);
            return ResponseEntity.ok("");
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (RentAlreadyEndedException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/rents/ended/users/{id}")
    public ResponseEntity<List<RentDTO>> getClientEndedRents(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(rentService.getClientEndedRents(id).stream().map(RentMapper::getRentDTO).toList());
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.WorkerRental.Roles).ADMIN , T(indie.outsource.WorkerRental.Roles).MANAGER)")
    @GetMapping("/rents/ended/workers/{id}")
    public ResponseEntity<List<RentDTO>> getWorkerEndedRents(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(rentService.getWorkerEndedRents(id).stream().map(RentMapper::getRentDTO).toList());
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/rents/current/users/{id}")
    public ResponseEntity<List<RentDTO>> getClientActiveRents(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(rentService.getClientActiveRents(id).stream().map(RentMapper::getRentDTO).toList());
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAnyRole(T(indie.outsource.WorkerRental.Roles).ADMIN , T(indie.outsource.WorkerRental.Roles).MANAGER)")
    @GetMapping("/rents/current/workers/{id}")
    public ResponseEntity<List<RentDTO>> getWorkerActiveRents(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(rentService.getWorkerActiveRents(id).stream().map(RentMapper::getRentDTO).toList());
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
