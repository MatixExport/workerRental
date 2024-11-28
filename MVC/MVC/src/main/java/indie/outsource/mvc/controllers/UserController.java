package indie.outsource.mvc.controllers;


import indie.outsource.mvc.services.UserService;
import indie.outsource.user.CreateUserDTO;
import indie.outsource.user.USERTYPE;
import indie.outsource.user.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setType(USERTYPE.CLIENT);
        model.addAttribute("user", createUserDTO);

        return "registerForm";
    }

    @PostMapping("/registerSubmit")
    public String registerSubmit(@ModelAttribute("user") @Valid CreateUserDTO createUserDTO, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("user", createUserDTO);
            return "registerForm";
        }

        UserDTO userDTO = null;
        try{
            userDTO = userService.createUser(createUserDTO);
        }catch (RuntimeException e){
            bindingResult.addError(new ObjectError("user", e.getMessage()));
        }


        if(bindingResult.hasErrors()) {
            model.addAttribute("user", createUserDTO);
            return "registerForm";
        }

        model.addAttribute("user", userDTO);
        return "account";
    }
}
