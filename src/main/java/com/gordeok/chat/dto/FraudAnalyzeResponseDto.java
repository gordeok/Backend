package com.gordeok.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FraudAnalyzeResponseDto {

    @JsonProperty("rule_triggered")
    private boolean ruleTriggered;

    @JsonProperty("matched_patterns")
    private List<String> matchedPatterns;

    @JsonProperty("llm_risk_level")
    private String llmRiskLevel;

    @JsonProperty("llm_reason")
    private String llmReason;

    private String action;  // none | show_warning_banner | show_danger_banner
}
