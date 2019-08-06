package org.gcnc.calculate.repository;

import org.gcnc.calculate.entity.LeagueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface LeagueRepository extends JpaRepository<LeagueEntity, Long> {
    //List<LeagueEntity> findByLeagueName(String name);
    LeagueEntity findByLeagueName(String name);
}
