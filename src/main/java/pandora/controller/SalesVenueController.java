package pandora.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pandora.domain.SalesVenue;
import pandora.domain.User;
import pandora.service.SalesVenueService;
import pandora.service.UserService;

@Controller
@RequestMapping("/markkinat")
public class SalesVenueController {
    
    @Autowired
    private SalesVenueService salesVenueService;
    @Autowired
    private UserService userService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(
            Model model) {
        User currentUser = userService.getCurrentUser();
        List<SalesVenue> salesVenues = salesVenueService.findAll(currentUser);
        model.addAttribute("salesVenues", salesVenues);
        return "salesvenues";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.GET)
    public String create(
            Model model,
            @ModelAttribute SalesVenue salesVenue) {
        return "salesvenue_add";        
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String add(
            Model model,
            @Valid @ModelAttribute SalesVenue salesVenue,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "salesvenue_add";
        }
        User currentUser = userService.getCurrentUser();
        try {
            salesVenue = salesVenueService.save(salesVenue, currentUser);
        } catch (IllegalArgumentException exc) {
            return "salesvenue_add";
        }
        return "redirect:/markkinat";
    }
    
    @RequestMapping(value = "/{id}/muokkaa", method = RequestMethod.GET)
    public String edit(
            @PathVariable Long id,
            Model model) {
        User currentUser = userService.getCurrentUser();
        SalesVenue salesVenue = null;
        try {
            salesVenue = salesVenueService.findOne(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        model.addAttribute("salesVenue", salesVenue);
        return "salesvenue_edit";
    }
    
    @RequestMapping(value = "/{id}/muokkaa", method = RequestMethod.POST)
    public String update(
            Model model,
            @Valid @ModelAttribute SalesVenue salesVenue,
            BindingResult bindingResult,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "salesvenue_edit";
        }
        User currentUser = userService.getCurrentUser();
        try {
            salesVenue = salesVenueService.save(salesVenue, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Muutokset tallennettu!");
        model.addAttribute("salesVenues", salesVenueService.findAll(currentUser));
        return "redirect:/markkinat";
    }
    
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST)
    public String delete(
            Model model,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        SalesVenue salesVenue = null;
        User currentUser = userService.getCurrentUser();
        try {
            salesVenue = salesVenueService.delete(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Markkina poistettu");
        model.addAttribute("salesVenues", salesVenueService.findAll(currentUser));
        return "redirect:/markkinat";
    }
 }
