/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.database;

import eu.arrowhead.common.messages.ArrowheadServiceDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "arrowhead_service", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"service_definition", "interface"})})
public class ArrowheadService {

  @Id
  @GenericGenerator(name = "table_generator", strategy = "org.hibernate.id.enhanced.TableGenerator")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_generator")
  private Long id;

  @Column(name = "service_definition")
  private String serviceDefinition;

  @Column(name = "interface")
  private String serviceInterface;

  public ArrowheadService() {
  }

  public ArrowheadService(String serviceDefinition, String serviceInterface) {
    this.serviceDefinition = serviceDefinition;
    this.serviceInterface = serviceInterface;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getServiceDefinition() {
    return serviceDefinition;
  }

  public void setServiceDefinition(String serviceDefinition) {
    this.serviceDefinition = serviceDefinition;
  }

  public String getServiceInterface() {
    return serviceInterface;
  }

  public void setServiceInterface(String serviceInterface) {
    this.serviceInterface = serviceInterface;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArrowheadService)) {
      return false;
    }
    ArrowheadService that = (ArrowheadService) o;
    return Objects.equals(serviceDefinition, that.serviceDefinition) && Objects
        .equals(serviceInterface, that.serviceInterface);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceDefinition, serviceInterface);
  }

  public static Optional<ArrowheadServiceDTO> convertToDTO(List<ArrowheadService> services,
                                                           Map<String, String> serviceMetadata) {
    if (services.isEmpty()) {
      return Optional.empty();
    }

    String serviceDef = services.get(0).getServiceDefinition();
    Set<String> interfaces = new HashSet<>();
    for (ArrowheadService service : services) {
      if (!service.getServiceDefinition().equals(serviceDef)) {
        throw new AssertionError(
            "ArrowheadService entity to DTO conversion failed because of serviceDefinition differences!");
      }
      interfaces.add(service.getServiceInterface());
    }

    return Optional.of(new ArrowheadServiceDTO(serviceDef, interfaces, serviceMetadata));
  }

  public static List<ArrowheadService> convertToEntity(ArrowheadServiceDTO service) {
    List<ArrowheadService> services = new ArrayList<>();
    for (String serviceInterface : service.getInterfaces()) {
      services.add(new ArrowheadService(service.getServiceDefinition(), serviceInterface));
    }
    if (services.isEmpty()) {
      services.add(new ArrowheadService(service.getServiceDefinition(), null));
    }
    return services;
  }

}