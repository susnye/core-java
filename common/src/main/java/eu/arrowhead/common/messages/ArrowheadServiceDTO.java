/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;
import eu.arrowhead.common.Utils;
import eu.arrowhead.common.json.constraint.SENotBlank;
import eu.arrowhead.common.json.support.ArrowheadServiceSupport;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

public class ArrowheadServiceDTO {

  @NotBlank
  @Size(max = 255, message = "Service serviceDefinition must be 255 character at max")
  private String serviceDefinition;

  @JsonInclude(Include.NON_EMPTY)
  @Size(max = 100, message = "Service can only have 100 interfaces at max")
  private Set<@SENotBlank String> interfaces = new HashSet<>();

  @JsonInclude(Include.NON_EMPTY)
  @Size(max = 100, message = "Service can only have 100 serviceMetadata key-value pairs at max")
  private Map<@SENotBlank String, @SENotBlank String> serviceMetadata = new HashMap<>();

  public ArrowheadServiceDTO() {
  }

  /**
   * Constructor with all the fields of the ArrowheadService class.
   *
   * @param serviceDefinition A descriptive name for the service
   * @param interfaces The set of interfaces that can be used to consume this service (helps interoperability between
   *     ArrowheadSystems). Concrete meaning of what is an interface is service specific (e.g. JSON, I2C)
   * @param serviceMetadata Arbitrary additional serviceMetadata belonging to the service, stored as key-value pairs.
   */
  public ArrowheadServiceDTO(String serviceDefinition, Set<String> interfaces, Map<String, String> serviceMetadata) {
    this.serviceDefinition = serviceDefinition;
    this.interfaces = interfaces;
    this.serviceMetadata = serviceMetadata;
  }

  public ArrowheadServiceDTO(ArrowheadServiceSupport service) {
    this.serviceDefinition = service.getServiceGroup() + "_" + service.getServiceDefinition();
    this.interfaces = new HashSet<>(service.getInterfaces());
    this.serviceMetadata = service.getServiceMetadata();
  }

  public String getServiceDefinition() {
    return serviceDefinition;
  }

  public void setServiceDefinition(String serviceDefinition) {
    this.serviceDefinition = serviceDefinition;
  }

  public Set<String> getInterfaces() {
    return interfaces;
  }

  public void setInterfaces(Set<String> interfaces) {
    this.interfaces = interfaces;
  }

  public Map<String, String> getServiceMetadata() {
    return serviceMetadata;
  }

  public void setServiceMetadata(Map<String, String> serviceMetadata) {
    this.serviceMetadata = serviceMetadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArrowheadServiceDTO)) {
      return false;
    }
    ArrowheadServiceDTO that = (ArrowheadServiceDTO) o;

    if (!Objects.equals(serviceDefinition, that.serviceDefinition)) {
      return false;
    }

    //2 services can be equal if they have at least 1 common interface
    if (interfaces == null || that.interfaces == null) {
      return true;
    } else {
      Set<String> intersection = new HashSet<>(interfaces);
      intersection.retainAll(that.interfaces);
      return !intersection.isEmpty();
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(serviceDefinition);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("serviceDefinition", serviceDefinition).toString();
  }

  public void partialUpdate(ArrowheadServiceDTO other) {
    this.serviceDefinition =
        other.getServiceDefinition() != null ? other.getServiceDefinition() : this.serviceDefinition;
    //Making deep copies of the collections with the help of JSON (de)serialization
    this.interfaces = other.getInterfaces().isEmpty() ? this.interfaces : Utils
        .fromJson(Utils.toPrettyJson(null, other.getInterfaces()), Set.class);
    this.serviceMetadata = other.getServiceMetadata().isEmpty() ? this.serviceMetadata : Utils
        .fromJson(Utils.toPrettyJson(null, other.getServiceMetadata()), Map.class);
  }
}