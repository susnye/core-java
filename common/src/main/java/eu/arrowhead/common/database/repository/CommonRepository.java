package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.DatabaseManager;
import eu.arrowhead.common.database.entity.ArrowheadCloud;
import eu.arrowhead.common.database.entity.ArrowheadService;
import eu.arrowhead.common.database.entity.ArrowheadSystem;
import eu.arrowhead.common.exception.DataNotFoundException;
import eu.arrowhead.common.messages.ArrowheadCloudDTO;
import eu.arrowhead.common.messages.ArrowheadServiceDTO;
import eu.arrowhead.common.messages.ArrowheadSystemDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

public class CommonRepository {

  //No IoC :(
  private static final DatabaseManager dm = DatabaseManager.getInstance();
  private static final Logger log = Logger.getLogger(CommonRepository.class.getName());
  private static final HashMap<String, Object> restrictionMap = new HashMap<>();

  public static List<ArrowheadCloudDTO> getAllClouds() {
    List<ArrowheadCloud> clouds = dm.getAll(ArrowheadCloud.class, null);
    log.info("getAllClouds returns " + clouds.size() + " clouds");
    return ArrowheadCloud.convertListToDTO(clouds, true);
  }

  public static ArrowheadCloudDTO getCloudById(long id) {
    ArrowheadCloud cloud = dm.get(ArrowheadCloud.class, id).orElseThrow(
        () -> new DataNotFoundException("ArrowheadCloud entry not found with id: " + id));
    return ArrowheadCloud.convertToDTO(cloud, true);
  }

  public static ArrowheadCloudDTO getCloudByOperatorAndName(String operator, String cloudName) {
    if (operator == null || cloudName == null) {
      throw new AssertionError("ArrowheadCloud bean validation failed");
    }
    restrictionMap.clear();
    restrictionMap.put("operator", operator);
    restrictionMap.put("cloudName", cloudName);
    ArrowheadCloud cloud = dm.get(ArrowheadCloud.class, restrictionMap);
    return ArrowheadCloud.convertToDTO(cloud, true);
  }

  public static List<ArrowheadCloudDTO> saveClouds(List<ArrowheadCloudDTO> clouds) {

  }

  public static ArrowheadCloudDTO updateCloud(long id, ArrowheadCloudDTO updatedCloud) {

  }

  public static void deleteCloud(long id) {
    dm.get(ArrowheadCloud.class, id).map(cloud -> {
      dm.delete(cloud);
      log.info("deleteCloud deleted ArrowheadCloud with id " + id);
    }).<DataNotFoundException>orElseThrow(() -> {
      log.info("deleteCloud found no ArrowheadCloud with id " + id);
      throw new DataNotFoundException("ArrowheadCloud entry not found with id: " + id);
    });
  }

  public static List<ArrowheadServiceDTO> getAllServices() {
    List<ArrowheadService> services = dm.getAll(ArrowheadService.class, null);
    log.info("getAllServices returns " + services.size() + " services");
    return ArrowheadService.convertListToDTO(services);
  }

  public static ArrowheadServiceDTO getServiceById(long id) {
    ArrowheadService service = dm.get(ArrowheadService.class, id).orElseThrow(
        () -> new DataNotFoundException("ArrowheadService entry not found with id: " + id));
    return ArrowheadService.convertToDTO(Collections.singletonList(service))
                           .orElseThrow(() -> new AssertionError("ArrowheadService conversion failed."));
  }

  public static List<ArrowheadServiceDTO> getServicesByDefinition(String serviceDefinition) {
    if (serviceDefinition == null) {
      throw new AssertionError("ArrowheadService bean validation failed");
    }
    restrictionMap.clear();
    restrictionMap.put("serviceDefinition", serviceDefinition);
    List<ArrowheadService> services = dm.getAll(ArrowheadService.class, restrictionMap);
    return ArrowheadService.convertListToDTO(services);
  }

  public static ArrowheadServiceDTO saveService(ArrowheadServiceDTO service) {
    List<ArrowheadService> services = ArrowheadService.convertToEntity(service);

    List<ArrowheadService> savedServices = new ArrayList<>();
    for (ArrowheadService service : serviceList) {
      restrictionMap.clear();
      restrictionMap.put("serviceDefinition", service.getServiceDefinition());
      ArrowheadService retrievedService = dm.get(ArrowheadService.class, restrictionMap);
      if (retrievedService == null) {
        dm.save(service);
        savedServices.add(service);
      }
    }
  }

  public static ArrowheadServiceDTO updateService(long id, ArrowheadServiceDTO updatedService) {

  }

  public static void deleteService(long id) {
    dm.get(ArrowheadService.class, id).map(service -> {
      dm.delete(service);
      log.info("deleteService deleted ArrowheadService with id " + id);
    }).<DataNotFoundException>orElseThrow(() -> {
      log.info("deleteService found no ArrowheadService with id " + id);
      throw new DataNotFoundException("ArrowheadService entry not found with id: " + id);
    });
  }

  public static List<ArrowheadSystemDTO> getAllSystems() {
    List<ArrowheadSystem> systems = dm.getAll(ArrowheadSystem.class, null);
    log.info("getAllSystems returns " + systems.size() + " systems");
    return ArrowheadSystem.convertListToDTO(systems, true);
  }

  public static ArrowheadSystemDTO getSystemById(long id) {
    ArrowheadSystem system = dm.get(ArrowheadSystem.class, id).orElseThrow(
        () -> new DataNotFoundException("ArrowheadSystem entry not found with id: " + id));
    return ArrowheadSystem.convertToDTO(system, true);
  }

  public static List<ArrowheadSystemDTO> getSystemsByName(String systemName) {
    if (systemName == null) {
      throw new AssertionError("ArrowheadSystem bean validation failed");
    }
    restrictionMap.clear();
    restrictionMap.put("systemName", systemName);
    List<ArrowheadSystem> systems = dm.getAll(ArrowheadSystem.class, restrictionMap);
    return ArrowheadSystem.convertListToDTO(systems, true);
  }

  public static List<ArrowheadSystemDTO> saveSystems(List<ArrowheadSystemDTO> systems) {

  }

  public static ArrowheadSystemDTO updateSystem(long id, ArrowheadSystemDTO updatedSystem) {

  }

  public static void deleteSystem(long id) {
    dm.get(ArrowheadSystem.class, id).map(system -> {
      dm.delete(system);
      log.info("deleteSystem deleted ArrowheadSystem with id " + id);
    }).<DataNotFoundException>orElseThrow(() -> {
      log.info("deleteSystem found no ArrowheadSystem with id " + id);
      throw new DataNotFoundException("ArrowheadSystem entry not found with id: " + id);
    });
  }

}
