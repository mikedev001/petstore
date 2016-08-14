package rbc.petstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PetstoreServiceImplTest {

    @Mock
    private PetstoreRepository petstoreRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private PetstoreService petstoreService;
    
    @Before
    public void setUp() throws Exception {
        petstoreService = new PetstoreServiceImpl(petstoreRepository, tagRepository, categoryRepository);
    }
    
    @Test
    public void test1() throws Exception {
        final Pet savedPet = stubRepositoryToReturnPetOnSave();
        final Pet pet = createPet();
        final Pet returnedPet = petstoreService.savePet(pet);
        verify(petstoreRepository, times(1)).save(pet);
        assertEquals("", savedPet, returnedPet);
    }

    @Test
    public void test2() throws Exception {
        stubRepositoryToReturnExistingPet();
        try {
            petstoreService.savePet(createPet());
            fail("Expected exception");
        } catch (PetAlreadyExistsException ignored) {
        }
        verify(petstoreRepository, never()).save(any(Pet.class));
    }

    @Test
    public void test3() throws Exception {
        stubRepositoryToReturnExistingPets(10L);
        Collection<Pet> list = petstoreService.getPetList();
        assertNotNull(list);
        assertEquals(10, list.size());
        verify(petstoreRepository, times(1)).findAll();
    }

    @Test
    public void test4() throws Exception {
        stubRepositoryToReturnExistingPets(0L);
        Collection<Pet> list = petstoreService.getPetList();
        assertNotNull(list);
        assertTrue(list.isEmpty());
        verify(petstoreRepository, times(1)).findAll();
    }
    
    @Test
    public void test5() throws Exception {
        stubRepositoryToReturnExistingPetByStatus(15L);
        Collection<Pet> list = petstoreService.findPetsByStatus("sold");
        assertNotNull(list);
        assertEquals(15, list.size());
    }

    public  Pet createPet() {
        ArrayList urls = new ArrayList();
        urls.add("http://photourl");
        return new Pet(0L, "Medor",urls,"sold");
    }

    public  List<Pet> createPetList(Long count) {
        List<Pet> petList = new ArrayList<Pet>();
        ArrayList urls;
        for (Long i = 0L; i < count; i++) {
            urls = new ArrayList();
            urls.add("http://photourl"+i);
            petList.add(new Pet(i, "Medor"+i,urls,"sold"));
        }
        return petList;
    }
    
    private Pet stubRepositoryToReturnPetOnSave() {
        Pet pet = createPet();
        when(petstoreRepository.save(any(Pet.class))).thenReturn(pet);
        return pet;
    }
     
    private void stubRepositoryToReturnExistingPet() {
        final Pet pet = createPet();
        when(petstoreRepository.findOne(pet.getId())).thenReturn(pet);
    }
     
    private void stubRepositoryToReturnExistingPets(Long count) {
        when(petstoreRepository.findAll()).thenReturn(createPetList(count));
    }
    
    private void stubRepositoryToReturnExistingPetByStatus(Long count) {
        when(petstoreRepository.findPetsByStatuses(Matchers.anyListOf(String.class))).thenReturn(createPetList(count));
    }
}
