package eu.arrowhead.core.choreographer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PlanDto {

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Steps")
  private List<PlanStepDto> steps;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<PlanStepDto> getSteps() {
    return steps;
  }

  public void setSteps(List<PlanStepDto> steps) {
    this.steps = steps;
  }
}