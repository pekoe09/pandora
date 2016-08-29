package pandora.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Autowired
    private ServletContext servletContext;
    
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
            model.addAttribute("collectibleSlot", storedImage.getCollectibleSlot());
            model.addAttribute("collectibleItem", storedImage.getCollectibleItem());
            return "storedimage_add";
        }
        User currentUser = userService.getCurrentUser();
        try {
            storedImage = storedImageService.save(storedImage, image, currentUser);
        } catch (IOException exc) {
            model.addAttribute("error", "Tiedostoa " + image.getOriginalFilename() + " ei pystytty lataamaan!");
            model.addAttribute("collectibleSlot", storedImage.getCollectibleSlot());
            model.addAttribute("collectibleItem", storedImage.getCollectibleItem());
            return "storedimage_add";
        } catch (IllegalArgumentException exc) {
            model.addAttribute("collectibleSlot", storedImage.getCollectibleSlot());
            model.addAttribute("collectibleItem", storedImage.getCollectibleItem());
            return "storedimage_add";
        }
        Long slotId = storedImage.getCollectibleItem() != null ? storedImage.getCollectibleItem().getCollectibleSlot().getId() : storedImage.getCollectibleSlot().getId();
        return "redirect:/slotit/" + slotId;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> show(
            @PathVariable Long id
//            @RequestParam(required = false) Boolean thumbnail
    ) {
        HttpHeaders headers = new HttpHeaders();
        byte[] imageBytes = null;
        try {
            StoredImage storedImage = storedImageService.findOne(id, userService.getCurrentUser());
            Image image = storedImageService.getImage(storedImage.getId());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write((BufferedImage)image, "jpg", os);
            InputStream in = new ByteArrayInputStream(os.toByteArray());
            imageBytes = IOUtils.toByteArray(in);
            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            return ResponseEntity.ok(imageBytes);
        } catch (IOException exc) {
            return new ResponseEntity<>(null, headers, HttpStatus.I_AM_A_TEAPOT);
        } catch (Exception exc) {
            return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
        }
    }
    
    @RequestMapping(value = "/{id}/poista", method = RequestMethod.POST)
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        StoredImage storedImage = null;
        try {
            User currentUser = userService.getCurrentUser();
            storedImage = storedImageService.delete(id, currentUser);
        } catch (IllegalArgumentException exc) {
            return "redirect:/paasyvirhe";
        }
        Long imageParentSlotId = storedImage.getCollectibleItem() != null
            ? storedImage.getCollectibleItem().getCollectibleSlot().getId()
            : storedImage.getCollectibleSlot().getId();
        redirectAttributes.addFlashAttribute("success", "Kuva poistettu!");
        return "redirect:/slotit/" + imageParentSlotId;
    }    
}
