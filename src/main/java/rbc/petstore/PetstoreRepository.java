package rbc.petstore;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetstoreRepository extends JpaRepository<Pet, Long> {
    @Query("select p from Pet p where upper(p.status) in :statuses")
    List<Pet> findPetsByStatuses(@Param("statuses") List<String> statuses);
}
