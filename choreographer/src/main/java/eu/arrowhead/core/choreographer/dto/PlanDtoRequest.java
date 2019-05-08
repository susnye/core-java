package eu.arrowhead.core.choreographer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PlanDtoRequest {

  @JsonProperty("name")
  private String name;

  @JsonProperty("steps")
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