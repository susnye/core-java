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
import eu.arrowhead.common.exception.BadPayloadException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrchestrationStoreDTO implements Comparable<OrchestrationStoreDTO> {

  private Long id;

  @Valid
  @NotNull
  private ArrowheadServiceDTO service;

  @Valid
  @NotNull
  private ArrowheadSystemDTO consumer;

  @Valid
  @NotNull
  private ArrowheadSystemDTO providerSystem;

  @Valid
  private ArrowheadCloudDTO providerCloud;

  @Min(1)
  private Integer priority = 0;

  private Boolean defaultEntry = false;

  private String name;

  private LocalDateTime lastUpdated;

  private String instruction;

  @JsonInclude(Include.NON_EMPTY)
  private Map<String, String> attributes = new HashMap<>();

  //Only to convert ServiceRegistryEntries to Store entries without data loss
  private String serviceURI;

  public OrchestrationStoreDTO() {
  }

  public OrchestrationStoreDTO(ArrowheadServiceDTO service, ArrowheadSystemDTO consumer,
                               ArrowheadSystemDTO providerSystem, ArrowheadCloudDTO providerCloud, int priority) {
    this.service = service;
    this.consumer = consumer;
    this.providerSystem = providerSystem;
    this.providerCloud = providerCloud;
    this.priority = priority;
  }

  public OrchestrationStoreDTO(ArrowheadServiceDTO service, ArrowheadSystemDTO consumer,
                               ArrowheadSystemDTO providerSystem, ArrowheadCloudDTO providerCloud, Integer priority,
                               Boolean defaultEntry, String name, LocalDateTime lastUpdated, String instruction,
                               Map<String, String> attributes) {
    this.service = service;
    this.consumer = consumer;
    this.providerSystem = providerSystem;
    this.providerCloud = providerCloud;
    this.priority = priority;
    this.defaultEntry = defaultEntry;
    this.name = name;
    this.lastUpdated = lastUpdated;
    this.instruction = instruction;
    this.attributes = attributes;
  }

  public OrchestrationStoreDTO(ArrowheadServiceDTO service, ArrowheadSystemDTO consumer,
                               ArrowheadSystemDTO providerSystem, ArrowheadCloudDTO providerCloud, int priority,
                               boolean defaultEntry, String name, LocalDateTime lastUpdated, String instruction,
                               Map<String, String> attributes, String serviceURI) {
    this.service = service;
    this.consumer = consumer;
    this.providerSystem = providerSystem;
    this.providerCloud = providerCloud;
    this.priority = priority;
    this.defaultEntry = defaultEntry;
    this.name = name;
    this.lastUpdated = lastUpdated;
    this.instruction = instruction;
    this.attributes = attributes;
    this.serviceURI = serviceURI;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ArrowheadServiceDTO getService() {
    return service;
  }

  public void setService(ArrowheadServiceDTO service) {
    this.service = service;
  }

  public ArrowheadSystemDTO getConsumer() {
    return consumer;
  }

  public void setConsumer(ArrowheadSystemDTO consumer) {
    this.consumer = consumer;
  }

  public ArrowheadSystemDTO getProviderSystem() {
    return providerSystem;
  }

  public void setProviderSystem(ArrowheadSystemDTO providerSystem) {
    this.providerSystem = providerSystem;
  }

  public ArrowheadCloudDTO getProviderCloud() {
    return providerCloud;
  }

  public void setProviderCloud(ArrowheadCloudDTO providerCloud) {
    this.providerCloud = providerCloud;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public Boolean isDefaultEntry() {
    return defaultEntry;
  }

  public void setDefaultEntry(Boolean defaultEntry) {
    this.defaultEntry = defaultEntry;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(LocalDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getInstruction() {
    return instruction;
  }

  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  public String getServiceURI() {
    return serviceURI;
  }

  public void setServiceURI(String serviceURI) {
    this.serviceURI = serviceURI;
  }

  /**
   * Note: This class has a natural ordering that is inconsistent with equals(). <p> The field <i>priority</i> is
   * used to sort instances of this class in a collection. Priority is non-negative.
   * If this.priority < other.priority that means <i>this</i> is more ahead in a collection than
   * <i>other</i> and therefore has a higher priority. This means priority = 0 is the highest priority for a Store
   * entry.
   */
  @Override
  public int compareTo(OrchestrationStoreDTO other) {
    return this.priority - other.priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OrchestrationStoreDTO)) {
      return false;
    }
    OrchestrationStoreDTO that = (OrchestrationStoreDTO) o;
    return Objects.equals(service, that.service) && Objects.equals(consumer, that.consumer) && Objects
        .equals(providerSystem, that.providerSystem) && Objects.equals(providerCloud, that.providerCloud) && Objects
        .equals(priority, that.priority) && Objects.equals(defaultEntry, that.defaultEntry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(service, consumer, providerSystem, providerCloud, priority, defaultEntry);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("service", service).add("consumer", consumer)
                      .add("providerSystem", providerSystem).add("providerCloud", providerCloud)
                      .add("priority", priority).add("defaultEntry", defaultEntry).toString();
  }

  public void validateCrossParameterConstraints() {
    if (defaultEntry && providerCloud != null) {
      throw new BadPayloadException("Default store entries can only have intra-cloud providers!");
    }
  }
}
