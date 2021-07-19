package com.epam.model.Crud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensitivityJava implements Serializable {
    private Integer dry;
    private Integer heat;
    private Integer cold;
}
