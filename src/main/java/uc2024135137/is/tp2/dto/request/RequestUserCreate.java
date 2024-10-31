package uc2024135137.is.tp2.dto.request;


import lombok.Data;
import uc2024135137.is.tp2.model.User;

@Data
public class RequestUserCreate {

    private String name;

    private Short age;

    private String gender;
}
