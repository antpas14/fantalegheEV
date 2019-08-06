package org.gcnc.calculate.repository;

import org.gcnc.calculate.entity.LeagueEntity;
import org.gcnc.calculate.entity.RankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface RankRepository extends JpaRepository<RankEntity, Long> {
    public List<RankEntity> findByLeagueEntity(LeagueEntity leagueEntity);
}

