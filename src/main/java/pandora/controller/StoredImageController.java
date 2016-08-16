package pandora.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pandora.domain.CollectibleItem;
import pandora.domain.CollectibleSlot;
import pandora.domain.StoredImage;
import pandora.domain.User;
import pandora.service.CollectibleItemService;
import pandora.service.CollectibleSlotService;
import pandora.service.StoredImageService;
import pandora.service.UserService;

@Controller
@RequestMapping("/kuvat")
public class StoredImageController {
    
    @Autowired
    private StoredImageService storedImageService;
    @Autowired
    private CollectibleSlotService collectibleSlotService;
    @Autowired
    private CollectibleItemService collectibleItemService;
    @Autowired
    private UserService userService;
    private String UPLOAD_DIR = "upload-dir";
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.GET)
    public String create(
            Model model,
            @ModelAttribute StoredImage storedImage,
            @RequestParam(required = false) Long collectibleSlotId,
            @RequestParam(required = false) Long collectibleItemId) {
        User currentUser = userService.getCurrentUser();
        CollectibleSlot collectibleSlot = null;
        CollectibleItem collectibleItem = null;
        try {
            if (collectibleSlotId != null) {
                collectibleSlot = collectibleSlotService.findOne(collectibleSlotId, currentUser);
            } else {
                collectibleItem = collectibleItemService.findOne(collectibleItemId, currentUser);
            }
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        model.addAttribute("collectibleSlot", collectibleSlot);
        model.addAttribute("collectibleItem", collectibleItem);
        return "storedimage_add";
    }
    
    @RequestMapping(value = "/lisaa", method = RequestMethod.POST)
    public String add(
            Model model,
            @Valid @ModelAttribute StoredImage storedImage,
            @RequestParam("image") MultipartFile image,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors() || image.isEmpty()) {
            return "storedimage_add";
        }
        User currentUser = userService.getCurrentUser();
        try {
//            Files.copy(image.getInputStream(), 
//                        Paths.get(UPLOAD_DIR, image.getOriginalFilename()));
            storedImage = storedImageService.save(storedImage, image, currentUser);
        } catch (IOException exc) {
            model.addAttribute("error", "Tiedostoa " + image.getOriginalFilename() + " ei pystytty lataamaan!");
            return "storedimage_add";
        } catch (IllegalArgumentException exc) {
            return "storedimage_add";
        }
        Long slotId = storedImage.getCollectibleItem() != null ? storedImage.getCollectibleItem().getCollectibleSlot().getId() : storedImage.getCollectibleSlot().getId();
        return "redirect:/slotit/" + slotId;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> show(@PathVariable Long id) {
        try {
            StoredImage storedImage = storedImageService.findOne(id, userService.getCurrentUser());
            return ResponseEntity.ok(storedImageService.getImage(storedImage.getId()));
        } catch (IOException exc) {
            return ResponseEntity.noContent().build();
        } catch (Exception exc) {
            return ResponseEntity.notFound().build();
        }
    }
//    
//    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST)
//    public String delete() {
//        
//    }    
}
