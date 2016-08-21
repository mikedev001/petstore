package rbc.petstore;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    PetImageRepository petImageRepository;

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
    @Transactional
    public void uploadImage(final InputStream iSImage, String contentType, String name, Long petId) {
        LOGGER.debug("upload image " + name + " service for content type " + contentType + " for pet id " + petId);
        final PetImage e = new PetImage();
        e.setName(name);
        e.setContentType(contentType);
        e.setPetId(petId);
        final byte[] image = getImageByteArray(iSImage);
        e.setImage(image);
        petImageRepository.saveAndFlush(e);
    }

    private byte[] getImageByteArray(final InputStream inputStream) {
        byte images[] = null;
        try {
            final BufferedImage image = ImageIO.read(inputStream);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", baos);
            images = baos.toByteArray();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    @Override
    @Transactional
    public PetImage findImageById(final Long imageId) {
        return petImageRepository.findOne(imageId);
    }

    @Override
    @Transactional
    public List<PetImageLight> getPhotosList(Long petId) {
        List<PetImage> list = petImageRepository.findPetImageByPetId(petId);
        ArrayList<PetImageLight> photosList = new ArrayList<PetImageLight>();
        for (PetImage item : list) {
            photosList.add(new PetImageLight(item.getId(), item.getName()));
        }
        return photosList;
    }
}
