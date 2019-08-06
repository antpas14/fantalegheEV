package org.gcnc.calculate.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@Table(name = "rank")
public class RankEntity {
    @Id
    @SequenceGenerator(name = "seq", sequenceName = "RANK_SEQ", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id")
    private LeagueEntity leagueEntity;
    @Column(name = "points")
    private int points;
    @Column(name = "ev_points")
    private Double evPoints;
    @Column(name = "name")
    private String team;
}
