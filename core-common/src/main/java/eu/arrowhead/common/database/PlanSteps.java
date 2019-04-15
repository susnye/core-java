package eu.arrowhead.common.database;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

  public PlanSteps(String name, Plans planId) {
    this.name = name;
    this.planId = planId;
  }
}
