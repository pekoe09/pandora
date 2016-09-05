package pandora.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
            @RequestParam String password2,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if(password2.trim().length() == 0) {
            bindingResult.rejectValue("password", "error.user", "Salasana ei saa olla tyhjä");
        } else if (!BCrypt.checkpw(password2, user.getPassword())) {
            bindingResult.rejectValue("password", "error.user", "Salasana ei täsmää");
        }
        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            return "user_add";
        }
        try {
            user = userService.save(user);
        } catch (IllegalArgumentException exc) {
            model.addAttribute("user", user);
            return "user_add";
        }
        redirectAttributes.addFlashAttribute("success", "Käyttäjä luotu");
        return "redirect:/kayttajat";
    }
    
    @RequestMapping(value = "/{id}/muokkaa", method = RequestMethod.GET)
    public String edit(
            @PathVariable Long id,
            Model model) {
        User user = null;
        try {
            user = userService.findOne(id);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        model.addAttribute("user", user);
        return "user_edit";
    }
    
    @RequestMapping(value = "/{id}/muokkaa", method = RequestMethod.POST)
    public String update(
            Model model,
            @Valid @ModelAttribute User user,
            BindingResult bindingResult,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "user_edit";
        }
        try {
            user = userService.save(user);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Muutokset tallennettu");
        model.addAttribute("users", userService.findAll());
        return "redirect:/kayttajat";
    }
    
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST)
    public String delete(
            Model model,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        User user = null;
        try {
            user = userService.delete(id);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Käyttäjää ei voitu poistaa!");
        } else {
            redirectAttributes.addFlashAttribute("success", "Käyttäjä poistettu");
        }
        model.addAttribute("users", userService.findAll());
        return "redirect:/kayttajat";
    }
}
