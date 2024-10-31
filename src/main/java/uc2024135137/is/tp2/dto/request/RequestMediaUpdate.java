package uc2024135137.is.tp2.dto.request;


import lombok.Data;

import java.util.Date;

@Data
public class RequestMediaUpdate {

    private String title;

    private Date releaseDate;

    private String mediaType;
}
