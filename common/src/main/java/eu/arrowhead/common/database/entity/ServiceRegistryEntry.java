/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.database.entity;

import com.google.common.base.MoreObjects;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.messages.ArrowheadServiceDTO;
import eu.arrowhead.common.messages.ArrowheadSystemDTO;
import eu.arrowhead.common.messages.ServiceRegistryEntryDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "service_registry", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"arrowhead_service_id", "provider_system_id"})})
public class ServiceRegistryEntry {

  @Id
  @GenericGenerator(name = "table_generator", strategy = "org.hibernate.id.enhanced.TableGenerator")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_generator")
  private Long id;

  @JoinColumn(name = "arrowhead_service_id")
  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ArrowheadService providedService;

  @JoinColumn(name = "provider_system_id")
  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ArrowheadSystem provider;

  @Column(name = "service_uri")
  private String serviceURI;

  @Type(type = "yes_no")
  private Boolean udp = false;

  @Column(name = "end_of_validity")
  private LocalDateTime endOfValidity;

  private Integer version = 1;

  //Takes the providedService (DTO) metadata map
  private String metadata;

  public ServiceRegistryEntry() {
  }

  public ServiceRegistryEntry(ArrowheadService providedService, ArrowheadSystem provider, String serviceURI) {
    this.providedService = providedService;
    this.provider = provider;
    this.serviceURI = serviceURI;
  }

  public ServiceRegistryEntry(ArrowheadService providedService, ArrowheadSystem provider, String serviceURI,
                              boolean udp, LocalDateTime endOfValidity, int version) {
    this.providedService = providedService;
    this.provider = provider;
    this.serviceURI = serviceURI;
    this.udp = udp;
    this.endOfValidity = endOfValidity;
    this.version = version;
  }

  public ServiceRegistryEntry(ArrowheadService providedService, ArrowheadSystem provider, String serviceURI,
                              Boolean udp, LocalDateTime endOfValidity, Integer version, String metadata) {
    this.providedService = providedService;
    this.provider = provider;
    this.serviceURI = serviceURI;
    this.udp = udp;
    this.endOfValidity = endOfValidity;
    this.version = version;
    this.metadata = metadata;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ArrowheadService getProvidedService() {
    return providedService;
  }

  public void setProvidedService(ArrowheadService providedService) {
    this.providedService = providedService;
  }

  public ArrowheadSystem getProvider() {
    return provider;
  }

  public void setProvider(ArrowheadSystem provider) {
    this.provider = provider;
  }

  public String getServiceURI() {
    return serviceURI;
  }

  public void setServiceURI(String serviceURI) {
    this.serviceURI = serviceURI;
  }

  public Boolean isUdp() {
    return udp;
  }

  public void setUdp(Boolean udp) {
    this.udp = udp;
  }

  public LocalDateTime getEndOfValidity() {
    return endOfValidity;
  }

  public void setEndOfValidity(LocalDateTime endOfValidity) {
    this.endOfValidity = endOfValidity;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getMetadata() {
    return metadata;
  }

  public void setMetadata(String metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ServiceRegistryEntry)) {
      return false;
    }
    ServiceRegistryEntry that = (ServiceRegistryEntry) o;
    return Objects.equals(providedService, that.providedService) && Objects.equals(provider, that.provider) && Objects
        .equals(serviceURI, that.serviceURI) && Objects.equals(version, that.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(providedService, provider, serviceURI, version);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("providedService", providedService).add("provider", provider)
                      .add("serviceURI", serviceURI).add("version", version).toString();
  }

  public static ServiceRegistryEntryDTO convertToDTO(List<ServiceRegistryEntry> entries, boolean includeId) {
    List<ArrowheadService> services = new ArrayList<>();
    for (ServiceRegistryEntry entry : entries) {
      services.add(entry.getProvidedService());
    }

    ServiceRegistryEntry firstEntry = entries.get(0);
    ArrowheadSystemDTO convertedProvider = ArrowheadSystem.convertToDTO(firstEntry.getProvider(), includeId);
    ArrowheadServiceDTO convertedService = ArrowheadService.convertToDTO(services).orElseThrow(
        () -> new ArrowheadException("ArrowheadService conversion returned empty object."));

    String metadata = firstEntry.getMetadata();
    if (metadata != null && metadata.trim().length() > 0) {
      String[] parts = metadata.split(",");
      convertedService.getServiceMetadata().clear();
      for (String part : parts) {
        String[] pair = part.split("=");
        convertedService.getServiceMetadata().put(pair[0], pair[1]);
      }
    }

    return new ServiceRegistryEntryDTO(convertedService, convertedProvider, firstEntry.getServiceURI(),
                                       firstEntry.isUdp(), firstEntry.getEndOfValidity(), firstEntry.getVersion());
  }

  public static List<ServiceRegistryEntry> convertToEntity(ServiceRegistryEntryDTO entry) {
    ArrowheadSystem convertedProvider = ArrowheadSystem.convertToEntity(entry.getProvider());
    List<ArrowheadService> services = ArrowheadService.convertToEntity(entry.getProvidedService());
    String metadata = null;
    if (entry.getProvidedService().getServiceMetadata() != null && !entry.getProvidedService().getServiceMetadata()
                                                                         .isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (Map.Entry<String, String> metadataEntry : entry.getProvidedService().getServiceMetadata().entrySet()) {
        sb.append(metadataEntry.getKey()).append("=").append(metadataEntry.getValue()).append(",");
      }
      metadata = sb.toString().substring(0, sb.length() - 1);
    }

    List<ServiceRegistryEntry> convertedEntries = new ArrayList<>();
    for (ArrowheadService service : services) {
      ServiceRegistryEntry convertedEntry = new ServiceRegistryEntry(service, convertedProvider, entry.getServiceURI(),
                                                                     entry.isUdp(), entry.getEndOfValidity(),
                                                                     entry.getVersion(), metadata);
      convertedEntries.add(convertedEntry);
    }
    return convertedEntries;
  }
}