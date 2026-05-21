package com.gordeok.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiPredictResponse {

    private String group;
    private String product;

    @JsonProperty("group_confidence")
    private double groupConfidence;

    @JsonProperty("similarity_score")
    private double similarityScore;

    @JsonProperty("is_confident")
    private boolean isConfident;
}