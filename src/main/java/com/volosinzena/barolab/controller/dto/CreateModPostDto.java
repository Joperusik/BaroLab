package com.volosinzena.barolab.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateModPostDto {
    private String title;
    private String content;

    @JsonProperty("external_url")
    private String externalUrl;
}
