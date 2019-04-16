package eu.arrowhead.common.database;

import java.util.Set;
import javax.persistence.Entity;
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
  private Long id;

  private String name;

  @Valid
  @JoinColumn(name = "plan_id", nullable = false)
  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Plans planId;

  @ManyToMany
  @JoinTable(
      name = "plan_step_service",
      joinColumns = @JoinColumn(name = "plan_step_id"),
      inverseJoinColumns = @JoinColumn(name = "service_id")
  )
  private Set<ServiceRegistryEntry> usedServices;

  @ManyToMany
  @JoinTable(
      name = "next_steps",
      joinColumns = @JoinColumn(name = "plan_step_id"),
      inverseJoinColumns = @JoinColumn(name = "next_step_id")
  )
  private Set<PlanSteps> nextSteps;

  @ManyToMany(mappedBy = "nextSteps")
  private Set<PlanSteps> planStep;

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

  public Plans getPlanId() {
    return planId;
  }

  public void setPlanId(Plans planId) {
    this.planId = planId;
  }

  public PlanSteps() {}

  public PlanSteps(String name, Plans planId) {
    this.name = name;
    this.planId = planId;
  }
}
