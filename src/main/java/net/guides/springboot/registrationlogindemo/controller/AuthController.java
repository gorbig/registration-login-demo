package net.guides.springboot.registrationlogindemo.controller;

import jakarta.validation.Valid;
import net.guides.springboot.registrationlogindemo.dto.UserDto;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.service.UserServiceInterface;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AuthController
{

    private UserServiceInterface userServiceInterface;
    public AuthController(UserServiceInterface userService) {
        this.userServiceInterface = userService;
    }

    @GetMapping("/index")
    public String home()
    {
        return "index";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model)
    {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model)
    {
        User existingUser = userServiceInterface.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty())
        {
            result.rejectValue("email", null, "There is already an account registered with the same email");

        }

        if(result.hasErrors())
        {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userServiceInterface.saveUser(userDto);
        return "redirect: /register?success";

    }
}
