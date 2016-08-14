package rbc.petstore;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class PetstoreServiceImpl implements PetstoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetstoreServiceImpl.class);
    private final PetstoreRepository petstoreRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;

    @Inject
    public PetstoreServiceImpl(final PetstoreRepository petstoreRepository, final TagRepository tagRepository, final CategoryRepository categoryRepository) {
        this.petstoreRepository = petstoreRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Pet savePet(@NotNull @Valid final Pet pet) {
        LOGGER.debug("Creating {}", pet);
        LOGGER.debug("find if pet not exists {}");
        Pet existing = petstoreRepository.findOne(pet.getId());
        if (existing != null) {
            throw new PetAlreadyExistsException(String.format("A pet already exists with id=%s", pet.getId()));
        }
        /*
        LOGGER.debug(pet.toString());
        
        LOGGER.debug("retrieve category from existing one");
        Category petCategory = categoryRepository.findOne(pet.getCategory().getId());
        
        if (petCategory == null) {
            throw new PetCategoryNotExistsException(String.format("Pat categort not exists for pet id=%s", pet.getId()));
        }
        pet.setCategory(petCategory);
        
        LOGGER.debug("retrieve tags from existing ones");
        ArrayList<Tag> petTags = new ArrayList<Tag>();
        for (Tag tag : pet.getTags()) {
            Tag petTag = tagRepository.findOne(tag.getId());
            if (petTag == null) {
                throw new PetTagNotExistsException(String.format("Pat tag not exists for pet id=%s", pet.getId()));
            }
            petTags.add(petTag);
        }
        pet.setTags(petTags);
        */
        return petstoreRepository.save(pet);
    }

    @Override
    @Transactional
    public Pet updatePet(@NotNull @Valid final Pet pet) {
        LOGGER.debug("Updating {}", pet);
        LOGGER.debug("find if pet exists {}");
        Pet existing = petstoreRepository.findOne(pet.getId());
        if (existing == null) {
            throw new PetNotExistsException(String.format("The pet to update do not exists id=%s", pet.getId()));
        }
        existing.setName(pet.getName());
        existing.setCategory(pet.getCategory());
        existing.setTags(pet.getTags());
        existing.setPhotoUrls(pet.getPhotoUrls());
        existing.setStatus(pet.getStatus());
        return petstoreRepository.save(existing);
    }
    
    @Override
    @Transactional
    public Pet findPetById(Long petId) {
        LOGGER.debug("Gettting pet by id", petId);
        Pet existing = petstoreRepository.findOne(petId);
        if (existing == null) {
            throw new PetNotExistsException(String.format("No pet exists with id=%s", petId));
        } else {
            return existing;
        }
    } 
    
    @Override
    @Transactional(readOnly = true)
    public List<Pet> findPetsByStatus(String csvStatuses) {
        return petstoreRepository.findPetsByStatuses(Arrays.asList(csvStatuses.toUpperCase().split(",")));
    }
    
    @Override
    @Transactional
    public void deletePet(Long petId) {
        LOGGER.debug("Deleting pet by id", petId);
        Pet existing = petstoreRepository.findOne(petId);
        if (existing == null) {
            throw new PetNotExistsException(String.format("No pet exists with id=%s", petId));
        } else {
            petstoreRepository.delete(existing);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pet> getPetList() {
        LOGGER.debug("List of pets retrieved");
        return petstoreRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Tag> getTagList() {
        LOGGER.debug("List of tags retrieved");
        return tagRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategoryList() {
        LOGGER.debug("List of categories retrieved");
        return categoryRepository.findAll();
    }
    
    @Override
    @Transactional()
    public void initRepositories() {
        LOGGER.debug("init tag repository {}");
        List<Tag> existingTags = tagRepository.findAll();
        if ((existingTags != null) && (existingTags.size()==0)) {
            Tag newTag0 = new Tag(0L, "Tag0");
            Tag newTag1 = new Tag(1L, "Tag1");
            Tag newTag2 = new Tag(2L, "Tag2");
            Tag newTag3 = new Tag(3L, "Tag3");
            Tag newTag4 = new Tag(4L, "Tag4");
            tagRepository.save(newTag0);
            tagRepository.save(newTag1);
            tagRepository.save(newTag2);
            tagRepository.save(newTag3);
            tagRepository.save(newTag4);
        }
        
        LOGGER.debug("init categories repository {}");
        List<Category> existingCategories = categoryRepository.findAll();
        if ((existingCategories != null) && (existingCategories.size()==0)) {
            Category newCategory0 = new Category(0L, "Category0");
            Category newCategory1 = new Category(1L, "Category1");
            Category newCategory2 = new Category(2L, "Category2");
            Category newCategory3 = new Category(3L, "Category3");
            Category newCategory4 = new Category(4L, "Category4");
            categoryRepository.save(newCategory0);
            categoryRepository.save(newCategory1);
            categoryRepository.save(newCategory2);
            categoryRepository.save(newCategory3);
            categoryRepository.save(newCategory4);
        }
        
    }
    
}
