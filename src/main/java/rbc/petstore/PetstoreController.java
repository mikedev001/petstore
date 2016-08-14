package rbc.petstore;

import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
