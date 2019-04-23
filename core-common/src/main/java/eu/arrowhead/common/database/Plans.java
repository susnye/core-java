package eu.arrowhead.common.database;

import com.google.common.base.MoreObjects;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "plans")
public class Plans {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "plan", fetch = FetchType.EAGER)
  private Set<PlanSteps> planSteps = new HashSet<>();

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

  public Set<PlanSteps> getPlanSteps() {
    return planSteps;
  }

  public void setPlanSteps(Set<PlanSteps> planSteps) {
    this.planSteps = planSteps;
  }

  public Plans() {}

  public Plans(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", name).toString();
  }

}
