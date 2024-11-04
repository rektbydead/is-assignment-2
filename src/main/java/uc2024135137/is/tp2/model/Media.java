package uc2024135137.is.tp2.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;
import java.util.Date;


@Data
public class Media {

    @Id
    private Long id;

    @Column
    private String title;

    @Column("releaseDate")
    private LocalDate releaseDate;

    @Column("mediaType")
    private String mediaType;

    @Column("totalRating")
    private Double totalRating;

    @Column("numberOfRates")
    private Integer numberOfRates;

    public Media() {
        this.totalRating = (double) 0;
        this.numberOfRates = 0;
    }

    public Double getAverageRating() {
        return totalRating == 0 && numberOfRates== 0 ? 0 : totalRating/numberOfRates;
    }

    @Version
    private Long version;

    public enum Type {
        MOVIE("movie"),
        TV_SHOW("tv-show");

        final String value;
        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
