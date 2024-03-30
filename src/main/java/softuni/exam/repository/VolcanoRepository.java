package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Volcano;

import java.util.Optional;
import java.util.Set;

@Repository
public interface VolcanoRepository extends JpaRepository<Volcano,Long> {

    Optional<Volcano> findByName(String name);

    @Query(value = "SELECT v FROM Volcano v WHERE v.elevation > 3000 AND v.isActive=true AND v.lastEruption IS NOT NULL order by v.elevation desc ")
    Set<Volcano> findAllWhereElevationIsBiggerThan3000();
}
