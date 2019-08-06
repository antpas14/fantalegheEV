package org.gcnc.calculate.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Builder
@Setter
@Getter
@Table(name = "league")
public class LeagueEntity {
    @Id
    private Long id;
    @Column(name = "league_name")
    private String leagueName;
    @Column(name = "last_updated")
    private LocalDate lastUpdated;
}
