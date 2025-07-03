package organization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class GasStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GS_Id")
    private int id;
    private String isBottle;
    private String date;
    private double currentStatus;
    private double purchase;
    private double purchaseNetValue;
    private double sell;
    private double sellNetValue;
    private double endQuantity;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tons", referencedColumnName = "TS_Id")
    private TonsStatistic tonsStatistic;

}