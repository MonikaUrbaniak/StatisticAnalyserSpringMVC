package organization.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Sell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "S_Id")
    private int id;
    private String productName;
    @Column(name = "category_id")
    private int categoryId;
    private String sellDate;
    private double buyPrice;
    private double sellPrice;
    private double quantity;
    private String active;
}
