package uc2024135137.is.tp2.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("users")
public class User {

    @Id
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private Short age;

    @Column("numberOfRatedMedia")
    private Long numberOfRatedMedia;

    @Column("gender")
    private String gender;

    @Version
    private Long version;
}
