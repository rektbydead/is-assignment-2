package uc2024135137.is.tp2.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("MediaRate")
public class MediaRate {

    @Id
    private Long id;

    @Column("mediaId")
    private Long mediaId;

    @Column("userId")
    private Long userId;

    @Column
    private Double rating;
}
