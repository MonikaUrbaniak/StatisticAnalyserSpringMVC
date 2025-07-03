package organization.entity;

import jakarta.persistence.*;
        import lombok.Getter;

@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "C_Id")
    private int id;
    private String name;
    private String active;

}