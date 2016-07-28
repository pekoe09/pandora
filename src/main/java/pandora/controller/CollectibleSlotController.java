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
import pandora.domain.CollectibleSet;
import pandora.domain.CollectibleSlot;
import pandora.domain.User;
import pandora.service.CollectibleSetService;
import pandora.service.CollectibleSlotService;
import pandora.service.UserService;

@Controller
@RequestMapping("/slotit")
public class CollectibleSlotController {
    
    @Autowired
    private CollectibleSlotService collectibleSlotService;
    @Autowired
    private CollectibleSetService collectibleSetService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(
                    Model model,
                    @PathVariable Long id,
                    @RequestParam(required = false) Long collectibleCollectionId) {
            User currentUser = userService.getCurrentUser();
            CollectibleSlot collectibleSlot = null;
            try {
                    collectibleSlot = collectibleSlotService.findOne(id, currentUser);
            } catch (IllegalArgumentException exc) {
                    return "/kokoelmat" + (collectibleCollectionId != null ? ("/" + collectibleCollectionId) : null);
            }
            model.addAttribute("collectibleSlot", collectibleSlot);
            return "collectibleslot";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.GET)
    public String create(
            Model model,
            @ModelAttribute CollectibleSlot collectibleSlot,
            @RequestParam(required = true) Long collectibleSetId) {
        User currentUser = userService.getCurrentUser();
        CollectibleSet collectibleSet = null;
        try {
            collectibleSet = collectibleSetService.findOne(collectibleSetId, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        model.addAttribute("collectibleSet", collectibleSet);
        return "collectibleslot_add";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String add(
            Model model,
            @Valid @ModelAttribute CollectibleSlot collectibleSlot,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "collectibleslot_add";
        }
        User currentUser = userService.getCurrentUser();
        try {
            collectibleSlot = collectibleSlotService.save(collectibleSlot, currentUser);
        } catch (IllegalArgumentException exc) {
            return "collectibleslot_add";
        }
        return "redirect:/kokoelmat/" + collectibleSlot.getCollectibleSet().getCollectibleCollection().getId();
    }
    
    @RequestMapping(value = "/{id}/muokkaa", method = RequestMethod.GET)
    public String edit(
            @PathVariable Long id,
            Model model) {
        User currentUser = userService.getCurrentUser();
        CollectibleSlot collectibleSlot = null;
        try {
            collectibleSlot = collectibleSlotService.findOne(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        model.addAttribute("collectibleSlot", collectibleSlot);
        return "collectibleslot_edit";
    }
    
    @RequestMapping(value = "/{id}/muokkaa", method = RequestMethod.POST)
    public String update(
            Model model,
            @Valid @ModelAttribute CollectibleSlot collectibleSlot,
            BindingResult bindingResult,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "collectibleslot_edit";
        }
        try {
            User currentUser = userService.getCurrentUser();
            collectibleSlot = collectibleSlotService.save(collectibleSlot, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Muutokset tallennettu!");
        return "redirect:/kokoelmat/" + collectibleSlot.getCollectibleSet().getCollectibleCollection().getId();
    }
    
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST)
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        CollectibleSlot collectibleSlot = null;
        try {
            User currentUser = userService.getCurrentUser();
            collectibleSlot = collectibleSlotService.delete(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Slotti poistettu");
        return "redirect:/kokoelmat/" + collectibleSlot.getCollectibleSet().getCollectibleCollection().getId();            
    }
}
