package eu.arrowhead.common;

import eu.arrowhead.common.database.PlanSteps;
import eu.arrowhead.common.database.Plans;
import eu.arrowhead.common.database.ServiceRegistryEntry;
import java.util.HashMap;
import java.util.Random;
import org.junit.Ignore;
import org.junit.Test;

public class DatabaseTest {

  private static final DatabaseManager dm = DatabaseManager.getInstance();

  @Ignore
  @Test
  public void addRowsToPlans() {
    Plans plan = new Plans();
    plan.setName("test222");
    dm.save(plan);
    //System.out.println(dm.get(Plans.class, 1));
  }

  @Test
  public void addRowsToPlanSteps() {
    int rand = new Random().nextInt();

    Plans plan = new Plans();
    plan.setName("testplan" + rand);

    PlanSteps planStep = new PlanSteps();
    planStep.setName("TankInstallation" + rand);
    planStep.setPlan(plan);

    PlanSteps planStep2 = new PlanSteps();
    planStep2.setName("Coloring" + rand);
    planStep2.setPlan(plan);

    ServiceRegistryEntry sre = dm.get(ServiceRegistryEntry.class, 125).get();

    planStep.getUsedServices().add(sre);
    planStep.getNextSteps().add(planStep2);

    dm.save(plan, planStep, planStep2);
  }

  @Ignore
  @Test
  public void sittesTest() {
    int rand = new Random().nextInt();

    Plans plan = new Plans();
    plan.setName("testplan" + rand);

    PlanSteps planStep = new PlanSteps();
    planStep.setName("teststep" + rand);
    planStep.setPlan(plan);

    dm.save(plan, planStep);




    HashMap<String, Object> rest = new HashMap<>();
    rest.put("name", "testplan" + rand);

    Plans retrievedPlan = dm.get(Plans.class, rest);

    System.out.println("plan steps begin");
    for (PlanSteps step : retrievedPlan.getPlanSteps()) {
      System.out.println("  " + step.getName());
    }
    System.out.println("plan steps end");
  }
}
