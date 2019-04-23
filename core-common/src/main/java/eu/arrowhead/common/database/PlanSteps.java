package eu.arrowhead.common.database;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "plan_steps")
public class PlanSteps {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  //@Valid
  @JoinColumn(name = "plan_id", nullable = false)
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Plans plan;

  @ManyToMany (cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinTable(
      name = "plan_step_service",
      joinColumns = @JoinColumn(name = "plan_step_id"),
      inverseJoinColumns = @JoinColumn(name = "service_id")
  )
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Set<ServiceRegistryEntry> usedServices = new HashSet<>();

  @ManyToMany (cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  @JoinTable(
      name = "next_steps",
      joinColumns = @JoinColumn(name = "plan_step_id"),
      inverseJoinColumns = @JoinColumn(name = "next_step_id")
  )
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Set<PlanSteps> nextSteps = new HashSet<>();

  @ManyToMany(mappedBy = "nextSteps")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Set<PlanSteps> planStep = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Plans getPlan() {
    return plan;
  }

  public void setPlan(Plans planId) {
    this.plan = planId;
  }

  public Set<ServiceRegistryEntry> getUsedServices() {
    return usedServices;
  }

  public void setUsedServices(Set<ServiceRegistryEntry> usedServices) {
    this.usedServices = usedServices;
  }

  public Set<PlanSteps> getNextSteps() {
    return nextSteps;
  }

  public void setNextSteps(Set<PlanSteps> nextSteps) {
    this.nextSteps = nextSteps;
  }

  public Set<PlanSteps> getPlanStep() {
    return planStep;
  }

  public void setPlanStep(Set<PlanSteps> planStep) {
    this.planStep = planStep;
  }

  public PlanSteps() {}

  /**
   * Creating a step of a plan which needs given services.
   * @param name Name of the step in the plan.
   * @param usedServices The services the step needs for operation.
   */
  public PlanSteps(String name, Set<ServiceRegistryEntry> usedServices) {
    this.name = name;
    this.usedServices = usedServices;
  }

  /**
   * Creating a step of a plan with given services and given following steps (which follow the current step).
   * @param name Name of the step in the plan.
   * @param usedServices The services the step needs for operation.
   * @param nextSteps The list of the steps following the current one.
   */
  public PlanSteps(String name, Set<ServiceRegistryEntry> usedServices, Set<PlanSteps> nextSteps) {
    this.name = name;
    this.usedServices = usedServices;
    this.nextSteps = nextSteps;
  }
}
