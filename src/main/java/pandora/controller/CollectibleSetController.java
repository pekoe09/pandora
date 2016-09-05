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
import pandora.domain.CollectibleCollection;
import pandora.domain.CollectibleSet;
import pandora.domain.User;
import pandora.service.CollectibleCollectionService;
import pandora.service.CollectibleSetService;
import pandora.service.UserService;

@Controller
@RequestMapping("/setit")
public class CollectibleSetController {
    
    @Autowired
    private CollectibleSetService collectibleSetService;
    @Autowired
    private CollectibleCollectionService collectibleCollectionService;
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.GET)
    public String create(
            Model model,
            @ModelAttribute CollectibleSet collectibleSet,
            @RequestParam(required = true) Long collectibleCollectionId,
            @RequestParam(required = false) Long parentSetId) {
        User currentUser = userService.getCurrentUser();
        CollectibleCollection collectibleCollection = null;
        CollectibleSet parentSet = null;
        try {
            collectibleCollection = collectibleCollectionService.findOne(collectibleCollectionId, currentUser);
            if(parentSetId != null) {
                parentSet = collectibleSetService.findOne(parentSetId, currentUser);
            }
        } catch (IllegalArgumentException exc) {
            return "kokoelma/" + collectibleCollectionId + 
                    ((parentSetId != null && parentSet != null) ? ("/" + parentSetId) : null);
        }
        model.addAttribute("collectibleCollection", collectibleCollection);
        model.addAttribute("parentSet", parentSet);
        return "collectibleset_add";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String add(
            Model model,
            @Valid @ModelAttribute CollectibleSet collectibleSet,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "collection_add";
        }
        User currentUser = userService.getCurrentUser();
        try {
            collectibleSet = collectibleSetService.save(collectibleSet, currentUser);
        } catch (IllegalArgumentException exc) {
            return "collection_add";
        }
        String parentSetParam = collectibleSet.getParentSet() != null ? ("/" + collectibleSet.getParentSet().getId()) : "";
        return "redirect:/kokoelmat/" + collectibleSet.getCollectibleCollection().getId() + parentSetParam;
    }
    
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST)
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        CollectibleSet collectibleSet = null;
        User currentUser = userService.getCurrentUser();
        try {
            collectibleSet = collectibleSetService.delete(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Setti poistettu");
        return "redirect:/kokoelmat/" + collectibleSet.getCollectibleCollection().getId();
    }
}
