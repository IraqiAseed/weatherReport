package com.epam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Document(collection = "farms")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarmJava implements Serializable {
    private int id;
    private String name;
    private String lastname;
    private Integer stationId;
    private String location;
    private String crops;
    private SensitivityJava sensitivity;
    private String email;

}