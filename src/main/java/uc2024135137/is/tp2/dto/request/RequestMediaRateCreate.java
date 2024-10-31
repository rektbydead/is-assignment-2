package uc2024135137.is.tp2.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RequestMediaRateCreate {

    private Long mediaId;

    private Long userId;

    @Min(0) @Max(10)
    private Double rate;
}
