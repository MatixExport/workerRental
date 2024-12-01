package indie.outsource.mvc.controllers;


import indie.outsource.mvc.services.RentService;
import indie.outsource.rent.FinishRentDTO;
import indie.outsource.rent.FullCreateRentDTO;
import indie.outsource.rent.RentDTO;
import indie.outsource.worker.WorkerDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Controller
public class RentController {

    RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }



    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/rent")
    public String rentForm(Model model) {
        FullCreateRentDTO createRentDTO = new FullCreateRentDTO();
        List<WorkerDTO> list = rentService.getAllWorkers();
        model.addAttribute("rent", createRentDTO);
        model.addAttribute("workers", list);
        return "rentForm";
    }

    @PostMapping("/rent")
    public String rentSubmit(@ModelAttribute("rent") @Valid FullCreateRentDTO createRentDTO, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("rent", createRentDTO);
            model.addAttribute("workers",rentService.getAllWorkers());
            return "rentForm";
        }
        RentDTO rentDTO = null;

        try{
            rentDTO = rentService.createRent(createRentDTO);
        }catch(RuntimeException e){
            bindingResult.addError(new ObjectError("rent", e.getMessage()));
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("rent", createRentDTO);
            model.addAttribute("workers",rentService.getAllWorkers());
            return "rentForm";
        }
        model.addAttribute("rent", rentDTO);
        return "rent";

    }

    @GetMapping("/rents")
    public String rentList(Model model) {
        List<RentDTO> list = rentService.getAllRents();
        model.addAttribute("rents", list);
        return "rentList";
    }

    @GetMapping("/rent/finish")
    public String finishRentForm(Model model) {
        List<RentDTO> list = rentService.getAllRents();
        FinishRentDTO finishRentDTO = new FinishRentDTO();
        model.addAttribute("rent", finishRentDTO);
        model.addAttribute("rents", list);
        return "endRentForm";
    }

    @PostMapping("/rent/finish")
    public String finishRentSubmit(@ModelAttribute("rent") @Valid FinishRentDTO finishRentDTO,
                                   BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("rent", finishRentDTO);
            model.addAttribute("rents",rentService.getAllRents());
            return "endRentForm";
        }
        try{
            rentService.endRent(finishRentDTO.getRentID());
            return "endRentSuccess";

        }catch (RuntimeException e){
            bindingResult.reject("error",e.getMessage());
            List<RentDTO> list = rentService.getAllRents();
            model.addAttribute("rents", list);
            return "endRentForm";
        }
    }



}
