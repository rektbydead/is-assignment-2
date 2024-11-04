package uc2024135137.is.tp2.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import uc2024135137.is.tp2.model.Media;

import java.time.LocalDate;
import java.util.Date;

@Data
public class RequestMediaCreate {

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate releaseDate;

    private Media.Type mediaType;
}
