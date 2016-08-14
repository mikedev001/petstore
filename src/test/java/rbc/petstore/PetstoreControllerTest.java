package rbc.petstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PetstoreControllerTest {

    @Mock
    private PetstoreService petstoreService;
    
    @Mock
    HttpServletResponse httpServletResponse;

    private PetstoreController petstoreController;

    @Before
    public void setUp() throws Exception {
        petstoreController = new PetstoreController(petstoreService);
    }
    
    @Test
    public void test1() throws Exception {
        final Pet savedPet = stubServiceToReturnStoredPet();
        final Pet pet = createPet();
        Pet returnedPet = petstoreController.createPet(pet, httpServletResponse);
        verify(petstoreService, times(1)).savePet(pet);
        assertEquals("", savedPet, returnedPet);
    }

    @Test
    public void test2() throws Exception {
        stubServiceToReturnExistingPets(10L);
        Collection<Pet> pets = petstoreController.listPets(httpServletResponse);
        assertNotNull(pets);
        assertEquals(10, pets.size());
        verify(petstoreService, times(1)).getPetList();
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
    
    private Pet stubServiceToReturnStoredPet() {
        final Pet pet = createPet();
        when(petstoreService.savePet(any(Pet.class))).thenReturn(pet);
        return pet;
    }
    
    private void stubServiceToReturnExistingPets(Long count) {
        when(petstoreService.getPetList()).thenReturn(createPetList(count));
    }
}
