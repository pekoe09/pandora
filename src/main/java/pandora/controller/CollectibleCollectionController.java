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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pandora.domain.CollectibleCollection;
import pandora.domain.User;
import pandora.service.CollectibleCollectionService;
import pandora.service.UserService;

@Controller
@RequestMapping("/kokoelmat")
public class CollectibleCollectionController {
    
    @Autowired
    private CollectibleCollectionService collectibleCollectionService;
    @Autowired
    private UserService userService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String list(
            Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("collections", collectibleCollectionService.findAll(currentUser));
        return "collections";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.GET)
    public String create(
            Model model,
            @ModelAttribute CollectibleCollection collectibleCollection)  {
        User currentUser = userService.getCurrentUser();
        return "collection_add";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String add(
            Model model,
            @Valid @ModelAttribute CollectibleCollection collectibleCollection,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            return "collection_add";
        }
        User currentUser = userService.getCurrentUser();
        try {
            collectibleCollection = collectibleCollectionService.save(collectibleCollection, currentUser);
        } catch (IllegalArgumentException exc) {
            return "collection_add";
        }
        return "redirect:/kokoelmat";
    }
    
    @RequestMapping(value = "/{id}/muuta", method = RequestMethod.GET)
    public String edit(
            @PathVariable Long id,
            Model model) {
        User currentUser = userService.getCurrentUser();
        CollectibleCollection collectibleCollection = null;
        try {
            collectibleCollection = collectibleCollectionService.findOne(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        model.addAttribute("collectibleCollection", collectibleCollection);
        return "collection_edit";        
    }
    
    @RequestMapping(value = "/{id}/muuta", method = RequestMethod.POST)
    public String update(
            Model model,
            @Valid @ModelAttribute CollectibleCollection collectibleCollection,
            BindingResult bindingResult,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "collection_edit";
        }
        try {
            User currentUser = userService.getCurrentUser();
            collectibleCollection = collectibleCollectionService.save(collectibleCollection, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Muutokset tallennettu");
        return "redirect:/kokoelmat";
    }
    
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST)
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        CollectibleCollection collectibleCollection = null;
        try {
            User currentUser = userService.getCurrentUser();
            collectibleCollection = collectibleCollectionService.delete(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        redirectAttributes.addFlashAttribute("success", "Kokoelma poistettu");
        return "redirect:/kokoelmat";
    }
}
