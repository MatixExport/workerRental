package spring.controllers;

import entities.user.UserEnt;
import exceptions.RentAlreadyEndedException;
import exceptions.ResourceNotFoundException;
import exceptions.UserInactiveException;
import exceptions.WorkerRentedException;
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
import spring.dto.mappers.RentMapper;
import view.RentService;
import view.UserService;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController()
public class RentController {
    private final RentService rentService;
    private final UserService userService;

    @GetMapping("/rents/{id}")
    public ResponseEntity<RentDTO> getRent(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(RentMapper.getRentDTO(rentService.findById(id)));
    }

    @GetMapping("/rents")
    public ResponseEntity<List<RentDTO>> getRents() {
        return ResponseEntity.ok(rentService.findAll().stream().map(RentMapper::getRentDTO).toList());
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN , T(spring.security.Roles).MANAGER)")
    @PostMapping("/rents/users/{clientId}/workers/{workerId}")
    public ResponseEntity<RentDTO> createRent(@PathVariable UUID clientId, @PathVariable UUID workerId, @RequestBody @Valid CreateRentDTO rent) throws ResourceNotFoundException {
        try {
            return ResponseEntity.ok(RentMapper.getRentDTO(rentService.createRent(clientId, workerId, rent.getStartDate())));
        } catch (WorkerRentedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch(UserInactiveException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/rents/user/workers/{workerId}")
    public ResponseEntity<RentDTO> createRent(@PathVariable UUID workerId, @RequestBody @Valid CreateRentDTO rent) throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) authentication.getDetails();
        UserEnt user = userService.findByUsername(userDetails.getUsername()).getFirst();

        return createRent(user.getId(), workerId, rent);
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN , T(spring.security.Roles).MANAGER)" +
            " or @rentService.isOwner(#id, principal.username)")
    @PostMapping("/rents/{id}/finish")
    public ResponseEntity<RentDTO> finishRent(@PathVariable UUID id) throws ResourceNotFoundException, RentAlreadyEndedException {
        return ResponseEntity.ok(RentMapper.getRentDTO(rentService.endRent(id)));
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN , T(spring.security.Roles).MANAGER)")
    @DeleteMapping("/rents/{id}/delete")
    public ResponseEntity<String> deleteRent(@PathVariable UUID id) throws ResourceNotFoundException, RentAlreadyEndedException {
        rentService.deleteRent(id);
        return ResponseEntity.ok("");
    }

    @GetMapping("/rents/ended/users/{id}")
    public ResponseEntity<List<RentDTO>> getClientEndedRents(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(rentService.getClientEndedRents(id).stream().map(RentMapper::getRentDTO).toList());
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN , T(spring.security.Roles).MANAGER)")
    @GetMapping("/rents/ended/workers/{id}")
    public ResponseEntity<List<RentDTO>> getWorkerEndedRents(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(rentService.getWorkerEndedRents(id).stream().map(RentMapper::getRentDTO).toList());
    }

    @GetMapping("/rents/current/users/{id}")
    public ResponseEntity<List<RentDTO>> getClientActiveRents(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(rentService.getClientActiveRents(id).stream().map(RentMapper::getRentDTO).toList());
    }

    @PreAuthorize("hasAnyRole(T(spring.security.Roles).ADMIN , T(spring.security.Roles).MANAGER)")
    @GetMapping("/rents/current/workers/{id}")
    public ResponseEntity<List<RentDTO>> getWorkerActiveRents(@PathVariable UUID id) throws ResourceNotFoundException {
        return ResponseEntity.ok(rentService.getWorkerActiveRents(id).stream().map(RentMapper::getRentDTO).toList());
    }
}
