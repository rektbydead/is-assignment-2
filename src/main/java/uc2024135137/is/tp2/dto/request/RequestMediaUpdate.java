package uc2024135137.is.tp2.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import uc2024135137.is.tp2.model.Media;

import java.util.Date;

@Data
public class RequestMediaUpdate {

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date releaseDate;

    private Media.Type mediaType;
}
