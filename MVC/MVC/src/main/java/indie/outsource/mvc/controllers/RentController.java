package indie.outsource.mvc.controllers;


import indie.outsource.mvc.services.RentService;
import indie.outsource.rent.CreateRentDTO;
import indie.outsource.rent.RentDTO;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.USERTYPE;
import indie.outsource.user.UserDTO;
import indie.outsource.worker.WorkerDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
public class RentController {

    RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping("/rent")
    public String rentForm(Model model) {
        CreateRentDTO createRentDTO = new CreateRentDTO();
        List<WorkerDTO> list = rentService.getAllWorkers();
        model.addAttribute("rent", createRentDTO);
        model.addAttribute("workers", list);
        return "rentForm";
    }


    @PostMapping("/rent/users/{clientId}/workers/{workerId}")
    public String rentSubmit(@ModelAttribute("rent") @Valid CreateRentDTO createRentDTO, BindingResult bindingResult, Model model,
                             @PathVariable UUID clientId, @PathVariable UUID workerId) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("rent", createRentDTO);
            model.addAttribute("workers",rentService.getAllWorkers());
            return "rentForm";
        }

        RentDTO rentDTO = null;

        try{
            rentDTO = rentService.createRent(createRentDTO, clientId, workerId);
        }catch(RuntimeException e){
            bindingResult.addError(new ObjectError("rent", e.getMessage()));
        }


        if(bindingResult.hasErrors()) {
            model.addAttribute("rent", createRentDTO);
            model.addAttribute("workers",rentService.getAllWorkers());
            return "rentForm";
        }
        model.addAttribute("rent", createRentDTO);
        return "rent";

    }
}
