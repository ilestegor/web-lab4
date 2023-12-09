package ilestegor.lab4.repository;

import ilestegor.lab4.entity.DotsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DotsRepository extends CrudRepository<DotsEntity, Long> {

}
