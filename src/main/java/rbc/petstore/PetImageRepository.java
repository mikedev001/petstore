package rbc.petstore;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PetImageRepository extends JpaRepository<PetImage, Long> {

    List<PetImage> findPetImageByPetId(@Param("petId") Long petId);
}
