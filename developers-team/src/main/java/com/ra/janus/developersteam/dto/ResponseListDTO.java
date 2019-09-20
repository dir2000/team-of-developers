package com.ra.janus.developersteam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseListDTO<T>{
    private int resultCode;
    private String message;
    List<T> response;
}
