package eu.arrowhead.core.choreographer.api;

import eu.arrowhead.common.DatabaseManager;
import eu.arrowhead.common.database.PlanSteps;
import eu.arrowhead.common.database.Plans;
import eu.arrowhead.common.database.ServiceRegistryEntry;
import eu.arrowhead.core.choreographer.ChoreographerResource;
import eu.arrowhead.core.choreographer.dto.PlanDto;
import eu.arrowhead.core.choreographer.dto.PlanStepDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

@Path("choreographer/plan")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChoreographerPlanApi {

  private static final Logger log = Logger.getLogger(ChoreographerResource.class.getName());
  private static final DatabaseManager dm = DatabaseManager.getInstance();

  @POST
  public Response addPlan(PlanDto request) {

    Plans planEntity = new Plans();
    planEntity.setName(request.getName());

    Map<String, PlanSteps> stepEntityMap = new HashMap<>();
    for (PlanStepDto step : request.getSteps()) {
      PlanSteps stepEntity = new PlanSteps();
      stepEntity.setName(step.getName());
      stepEntity.setPlan(planEntity);
      // or this, but it doesn't work ((H)ikariCP is rly bad omg)
      // planEntity.getPlanSteps().add(stepEntity);

      stepEntityMap.put(step.getName(), stepEntity);
    }

    Set<String> serviceNames = new HashSet<>();
    Map<String, ServiceRegistryEntry> serviceMap = new HashMap<>();

    for (PlanStepDto planRequestStep : request.getSteps()) {
      serviceNames.addAll(planRequestStep.getServices());
    }

    List<ServiceRegistryEntry> services = dm.query(ServiceRegistryEntry.class, criteria ->
        criteria.createCriteria("providedService")
                .add(Restrictions.in("serviceDefinition", serviceNames)));

    for (ServiceRegistryEntry serviceEntity : services) {
      serviceMap.put(serviceEntity.getProvidedService().getServiceDefinition(), serviceEntity);
    }

    for (PlanStepDto step : request.getSteps()) {
      String stepName = step.getName();
      PlanSteps stepEntity = stepEntityMap.get(stepName);

      for (String name : step.getServices()) {
        ServiceRegistryEntry serviceRegistryEntry = serviceMap.get(name);
        stepEntity.getUsedServices().add(serviceRegistryEntry);
      }

      if (step.getNextSteps() != null) {
        for (String nextStepName : step.getNextSteps()) {
          PlanSteps nextStepEntity = stepEntityMap.get(nextStepName);
          stepEntity.getNextSteps().add(nextStepEntity);
        }
      }
    }

    List<Object> allEntities = new ArrayList<>();
    allEntities.add(planEntity);
    allEntities.addAll(stepEntityMap.values());

    dm.save(allEntities.toArray());

    return Response.status(Status.CREATED).build();
  }

  @GET
  @Path("{id}")
  public Response getPlan(@PathParam("id") long id) {
    Optional<Plans> planEntityOpt =  dm.get(Plans.class, id);

    if (!planEntityOpt.isPresent()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Plans planEntity = planEntityOpt.get();

    PlanDto response = new PlanDto();

    response.setName(planEntity.getName());
    List<PlanStepDto> steps = new ArrayList<>();

    for (PlanSteps stepEntity : planEntity.getPlanSteps()) {
      PlanStepDto step = new PlanStepDto();

      step.setName(stepEntity.getName());
      if (!stepEntity.getNextSteps().isEmpty()) {
        step.setNextSteps(stepEntity.getNextSteps().stream().map(PlanSteps::getName).collect(Collectors.toList()));
      }
      step.setServices(stepEntity.getUsedServices().stream().map(x -> x.getProvidedService().getServiceDefinition()).collect(
          Collectors.toList()));

      steps.add(step);
    }

    response.setSteps(steps);

    return Response.ok(response).build();
  }

  @DELETE
  @Path("{id}")
  public Response deletePlan(@PathParam("id") long id) {

    Optional<Plans> planEntityOpt = dm.get(Plans.class, id);

    if(!planEntityOpt.isPresent()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Plans planEntity = planEntityOpt.get();

    dm.delete(planEntity);

    return Response.ok().build();
  }
}
