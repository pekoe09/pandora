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
import pandora.domain.CollectibleItem;
import pandora.domain.CollectibleSlot;
import pandora.domain.User;
import pandora.service.CollectibleItemService;
import pandora.service.CollectibleSlotService;
import pandora.service.UserService;

@Controller
@RequestMapping("/kohteet")
public class CollectibleItemController {
    
    @Autowired
    private CollectibleItemService collectibleItemService;
    @Autowired
    private CollectibleSlotService collectibleSlotService;
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.GET)
    public String create(
            Model model,
            @ModelAttribute CollectibleItem collectibleItem,
            @RequestParam(required = true) Long collectibleSlotId) {
        User currentUser = userService.getCurrentUser();
        CollectibleSlot collectibleSlot = null;
        try {
            collectibleSlot = collectibleSlotService.findOne(collectibleSlotId, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        model.addAttribute("collectibleSlot", collectibleSlot);
        return "collectibleitem_add";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String add(
            Model model,
            @Valid @ModelAttribute CollectibleItem collectibleItem,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "collectibleitem_add";
        }
        User currentUser = userService.getCurrentUser();
        try {
            collectibleItem = collectibleItemService.save(collectibleItem, currentUser);
        } catch (IllegalArgumentException exc) {
            return "collectibleitem_add";
        }
        return "redirect:/slotit/" + collectibleItem.getCollectibleSlot().getId();
    }
    
    @RequestMapping(value = "/{id}/muokkaa", method = RequestMethod.GET)
    public String edit(
            @PathVariable Long id,
            Model model) {
        User currentUser = userService.getCurrentUser();
        CollectibleItem collectibleItem = null;
        try {
            collectibleItem = collectibleItemService.findOne(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        model.addAttribute("collectibleItem", collectibleItem);
        return "collectibleitem_edit";
    }
    
    @RequestMapping(value = "/{id}/muokkaa", method = RequestMethod.POST)
    public String update(
            Model model,
            @Valid @ModelAttribute CollectibleItem collectibleItem,
            BindingResult bindingResult,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "collectibleitem_edit";
        }
        try {
            User currentUser = userService.getCurrentUser();
            collectibleItem = collectibleItemService.save(collectibleItem, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Muutokset tallennettu!");
        return "redirect:/slotit/" + collectibleItem.getCollectibleSlot().getId();
    }
    
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST)
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        CollectibleItem collectibleItem = null;
        try {
            User currentUser = userService.getCurrentUser();
            collectibleItem = collectibleItemService.delete(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Kohde poistettu!");
        return "redirect:/slotti/" + collectibleItem.getCollectibleSlot().getId();
    }
}
