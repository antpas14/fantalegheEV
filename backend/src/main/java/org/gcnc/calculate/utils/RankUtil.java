package org.gcnc.calculate.utils;

import org.gcnc.calculate.entity.LeagueEntity;
import org.gcnc.calculate.entity.RankEntity;
import org.gcnc.calculate.model.Rank;

public class RankUtil {
    public static Rank buildFor(RankEntity rankEntity) {
        return Rank.builder()
                .evPoints(rankEntity.getEvPoints())
                .points(rankEntity.getPoints())
                .team(rankEntity.getTeam()).build();
    }

    public static RankEntity buildFor(Rank rank, LeagueEntity leagueEntity) {
        return RankEntity.builder()
                .evPoints(rank.getEvPoints())
                .points(rank.getPoints())
                .team(rank.getTeam())
                .leagueEntity(leagueEntity)
                .build();
    }
}
