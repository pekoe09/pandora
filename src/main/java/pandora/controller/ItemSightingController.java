package pandora.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pandora.domain.CollectibleSlot;
import pandora.domain.ItemSighting;
import pandora.domain.User;
import pandora.service.CollectibleSlotService;
import pandora.service.ItemSightingService;
import pandora.service.UserService;

@Controller
@RequestMapping("/havainnot")
public class ItemSightingController {
    
    @Autowired
    private ItemSightingService itemSightingService;
    @Autowired
    private CollectibleSlotService collectibleSlotService;
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.GET)
    public String create(
            Model model,
            @ModelAttribute ItemSighting itemSighting,
            @RequestParam(required = true) Long collectibleSlotId){
        User currentUser = userService.getCurrentUser();
        CollectibleSlot collectibleSlot = null;
        try {
            collectibleSlot = collectibleSlotService.findOne(collectibleSlotId, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        model.addAttribute("collectibleSlot", collectibleSlot);
        return "itemsighting_add";        
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String add(
            Model model,
            @Valid @ModelAttribute ItemSighting itemSighting,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) {
            model.addAttribute("collectibleSlot", itemSighting.getCollectibleSlot());
            return "itemsighting_add";
        }
        User currentUser = userService.getCurrentUser();
        try {
            itemSighting = itemSightingService.save(itemSighting, currentUser);
        } catch (IllegalArgumentException exc) {
            model.addAttribute("collectibleSlot", itemSighting.getCollectibleSlot());
            return "itemsighting_add";
        }
        return "redirect:/slotit/" + itemSighting.getCollectibleSlot().getId();
    }
    
    @RequestMapping(value = "/{id}/muokkaa", method = RequestMethod.GET)
    public String edit(
            @PathVariable Long id,
            Model model){
        User currentUser = userService.getCurrentUser();
        ItemSighting itemSighting = null;
        try {
            itemSighting = itemSightingService.findOne(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        model.addAttribute("itemSighting", itemSighting);
        return "itemsighting_edit";
    }
    
    @RequestMapping(value = "/{id}/muokkaa", method = RequestMethod.POST)
    public String update(
            Model model,
            @Valid @ModelAttribute ItemSighting itemSighting,
            BindingResult bindingResult,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()) {
            return "itemsighting_edit";
        }
        User currentUser = userService.getCurrentUser();
        try {
            itemSighting = itemSightingService.save(itemSighting, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Muutokset tallennettu!");
        return "redirect:/slotit/" + itemSighting.getCollectibleSlot().getId();
    }
    
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST)
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes){
        ItemSighting itemSighting = null;
        User currentUser = userService.getCurrentUser();
        try {
            itemSighting = itemSightingService.delete(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Havainto poistettu");
        return "redirect:/slotit/" + itemSighting.getCollectibleSlot().getId();
    }
}
