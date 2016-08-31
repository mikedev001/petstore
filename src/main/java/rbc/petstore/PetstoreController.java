package rbc.petstore;

import java.io.ByteArrayInputStream;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PetstoreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetstoreController.class);
    private final PetstoreService petstoreService;

    @Inject
    public PetstoreController(final PetstoreService petstoreService) {
        this.petstoreService = petstoreService;
    }

    @RequestMapping(value = "/pet", method = RequestMethod.POST)
    public Pet createPet(@RequestBody @Valid final Pet pet, HttpServletResponse response) {
        LOGGER.debug("Received request to create a pet {}", pet);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        return petstoreService.savePet(pet);
    }

    @RequestMapping(value = "/pet/{id}", method = RequestMethod.PUT)
    public Pet updatePet(@PathVariable("id") Long petId, @RequestBody @Valid final Pet pet, HttpServletResponse response) {
        LOGGER.debug("Received request to update a pet {}", pet);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        return petstoreService.updatePet(pet);
    }

    @RequestMapping(value = "/pet", method = RequestMethod.GET)
    public List<Pet> listPets(HttpServletResponse response) {
        LOGGER.debug("Received request for pet list");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        return petstoreService.getPetList();
    }

    @RequestMapping(value = "/tags", method = RequestMethod.GET)
    public List<Tag> listTags(HttpServletResponse response) {
        LOGGER.debug("Received request for tag list");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        return petstoreService.getTagList();
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<Category> listCategories(HttpServletResponse response) {
        LOGGER.debug("Received request for Category list");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        return petstoreService.getCategoryList();
    }

    @RequestMapping(value = "/pet/{petId}", method = RequestMethod.DELETE)
    void deletePet(@PathVariable("petId") Long petId, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("Received request to delete a pet {}", petId);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        petstoreService.deletePet(petId);
    }

    @RequestMapping(value = "/pet/{petId}", method = RequestMethod.GET)
    Pet findById(@PathVariable("petId") Long petId, HttpServletResponse response) {
        LOGGER.debug("Received request to find a pet {}", petId);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        return petstoreService.findPetById(petId);
    }

    @RequestMapping(value = "/pet/findPetsByStatus", method = RequestMethod.GET)
    List<Pet> findByStatus(@RequestParam(value = "status", defaultValue = "") String status, HttpServletResponse response) {
        LOGGER.debug("Received request to find pets by status {}", status);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        return petstoreService.findPetsByStatus(status);
    }

    @RequestMapping(value = "/pet/upload", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> upload(@RequestParam("file") MultipartFile uploadFile, @RequestParam("petId") Long petId) throws Exception {
        LOGGER.debug("Received multipart upload request for pet id:" + petId);
        try {
            petstoreService.uploadImage(uploadFile.getInputStream(), uploadFile.getContentType(), uploadFile.getOriginalFilename(), petId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (final Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/petdownload", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> download(@RequestParam(value = "url", defaultValue = "") String url, @RequestParam(value = "name", defaultValue = "") String name, @RequestParam(value = "petId", defaultValue = "") Long petId, HttpServletResponse response) throws Exception {
        LOGGER.debug("Received download request for pet id:" + petId);
        LOGGER.debug("url:" + url);
        try {
            petstoreService.downloadImage(url, name, petId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (final Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/pet/photo/{photoId}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<InputStreamResource> download(@PathVariable Long photoId) {
        PetImage image = petstoreService.findImageById(photoId);
        MediaType media = null;
        if (image.getContentType().toLowerCase().contains("jpeg")) {
            media = MediaType.IMAGE_JPEG;
        } else if (image.getContentType().toLowerCase().contains("gif")) {
            media = MediaType.IMAGE_GIF;
        } else if (image.getContentType().toLowerCase().contains("png")) {
            media = MediaType.IMAGE_PNG;
        }
        return ResponseEntity.ok()
                .contentType(media)
                .body(new InputStreamResource(new ByteArrayInputStream(image.getImage())));
    }

    @RequestMapping(value = "/photos/{petId}", method = RequestMethod.GET)
    public List<PetImageLight> listPhotos(@PathVariable Long petId, HttpServletResponse response) {
        LOGGER.debug("Received request for photos list");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        return petstoreService.getPhotosList(petId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlePetAlreadyExistsException(PetAlreadyExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handlePetNotExistsException(PetNotExistsException e) {
        return e.getMessage();
    }
}
