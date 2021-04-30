package ga.palomox.cutepigeons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ga.palomox.cutepigeons.model.Pigeon;

@Repository
public interface IPigeonRepository extends JpaRepository<Pigeon, Integer>{

}
