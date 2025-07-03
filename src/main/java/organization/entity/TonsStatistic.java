package organization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class TonsStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TS_Id")
    private Integer id;
    private double currentTons;
    private double purchaseTons ;
    private double sellTons;
    private double endTons;
    @OneToOne(mappedBy = "tonsStatistic")
    private GasStatistic gasStatistic;
}
