package rbc.petstore;

import java.io.InputStream;
import java.util.List;

public interface PetstoreService {
    
    public Pet savePet(Pet pet);
    
    public Pet updatePet(Pet pet);
    
    public void deletePet(Long petId);

    public List<Pet> getPetList();
    
    public List<Tag> getTagList();
    
    public List<Category> getCategoryList();
    
    public Pet findPetById(Long petId);
    
    public List<Pet> findPetsByStatus(String csvStatuses);
    
    public void uploadImage(final InputStream iSImage, String contentType, String name, Long petId);
    
    public PetImage findImageById(final Long imageId);
    
    public List<PetImageLight> getPhotosList(Long petId);
    
    public void downloadImage(String url, String name, Long petId) throws Exception;
}
