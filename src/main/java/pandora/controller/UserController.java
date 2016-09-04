package pandora.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pandora.domain.User;
import pandora.service.UserService;

@Controller
@RequestMapping("/kayttajat")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(
            Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.GET)
    public String create(
            Model model,
            @ModelAttribute User user) {
        return "user_add";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String add(
            Model model,
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            return "user_add";
        }
        try {
            user = userService.save(user);
        } catch (IllegalArgumentException exc) {
            return "user_add";
        }
        return "redirect:/kayttajat";
    }
}
