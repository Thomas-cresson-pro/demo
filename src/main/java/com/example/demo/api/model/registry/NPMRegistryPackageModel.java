package com.example.demo.api.model.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NPMRegistryPackageModel {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("_rev")
    private String revision;
    private String name;
    private String description;
    @JsonProperty("dist-tags")
    private Map<String, String> distTags;
    private Map<String, Version> versions;
    private List<Author> maintainers;
    private Map<String, LocalDateTime> time;
    private Author author;
    private Map<String, Boolean> users;
    private String license;
    private List<String> keywords;

}
