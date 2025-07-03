package organization.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "P_Id")
    private int id;
    private String name;
    @Column(name = "category_id")
    private int categoryId;
    private double buyPrice;
    private double sellPrice;
    private double deliveryQuantity;
    private int tax;
    private double Quantity;
    private String deliveryDate;

}
