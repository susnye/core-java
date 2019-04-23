package eu.arrowhead.core.choreographer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PlanStepDto {
  @JsonProperty("Name")
  private String name;

  @JsonProperty("NextSteps")
  private List<String> nextSteps;

  @JsonProperty("Services")
  private List<String> services;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getNextSteps() {
    return nextSteps;
  }

  public void setNextSteps(List<String> nextSteps) {
    this.nextSteps = nextSteps;
  }

  public List<String> getServices() {
    return services;
  }

  public void setServices(List<String> services) {
    this.services = services;
  }
}
