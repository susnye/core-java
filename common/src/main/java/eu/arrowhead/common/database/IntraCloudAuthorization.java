/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.database;

import com.google.common.base.MoreObjects;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.messages.ArrowheadServiceDTO;
import eu.arrowhead.common.messages.ArrowheadSystemDTO;
import eu.arrowhead.common.messages.IntraCloudAuthorizationDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
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

/**
 * JPA entity class for storing intra-cloud (within the cloud) authorization rights in the database. The <i>consumer_system_id</i>,
 * <i>provider_system_id</i> and <i>arrowhead_service_id</i> columns must be unique together. <p> The table contains foreign keys to {@link
 * ArrowheadSystem} and {@link ArrowheadService}. A particular Consumer System/Provider System/Arrowhead Service trio is authorized if there is a
 * database entry for it in this table. The existence of the database entry means the given Consumer System is authorized to consume the given
 * Arrowhead Serice from the given Provider System inside the Local Cloud. The reverse of it is not authorized.
 *
 * @author Umlauf Zoltán
 */
@Entity
@Table(name = "intra_cloud_authorization", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"consumer_system_id", "provider_system_id", "arrowhead_service_id"})})
public class IntraCloudAuthorization {

  @Id
  @GenericGenerator(name = "table_generator", strategy = "org.hibernate.id.enhanced.TableGenerator")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_generator")
  private Long id;

  @JoinColumn(name = "consumer_system_id")
  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ArrowheadSystem consumer;

  @JoinColumn(name = "provider_system_id")
  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ArrowheadSystem provider;

  @JoinColumn(name = "arrowhead_service_id")
  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ArrowheadService service;

  public IntraCloudAuthorization() {
  }

  public IntraCloudAuthorization(ArrowheadSystem consumer, ArrowheadSystem provider, ArrowheadService service) {
    this.consumer = consumer;
    this.provider = provider;
    this.service = service;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ArrowheadSystem getConsumer() {
    return consumer;
  }

  public void setConsumer(ArrowheadSystem consumer) {
    this.consumer = consumer;
  }

  public ArrowheadSystem getProvider() {
    return provider;
  }

  public void setProvider(ArrowheadSystem providers) {
    this.provider = providers;
  }

  public ArrowheadService getService() {
    return service;
  }

  public void setService(ArrowheadService service) {
    this.service = service;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof IntraCloudAuthorization)) {
      return false;
    }
    IntraCloudAuthorization that = (IntraCloudAuthorization) o;
    return Objects.equals(consumer, that.consumer) && Objects.equals(provider, that.provider) && Objects.equals(service, that.service);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consumer, provider, service);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("consumer", consumer).add("provider", provider).add("service", service).toString();
  }

  public static IntraCloudAuthorizationDTO convertToDTO(List<IntraCloudAuthorization> entries, boolean includeId) {
    List<ArrowheadService> services = new ArrayList<>();
    for (IntraCloudAuthorization entry : entries) {
      services.add(entry.getService());
    }

    ArrowheadSystemDTO convertedConsumer = ArrowheadSystem.convertToDTO(entries.get(0).getConsumer(), includeId);
    ArrowheadSystemDTO convertedProvider = ArrowheadSystem.convertToDTO(entries.get(0).getProvider(), includeId);
    ArrowheadServiceDTO convertedService = ArrowheadService.convertToDTO(services, null).orElseThrow(
        () -> new ArrowheadException("ArrowheadService conversion returned empty object."));
    return new IntraCloudAuthorizationDTO(convertedConsumer, convertedProvider, convertedService);
  }

  public static List<IntraCloudAuthorization> convertToEntity(IntraCloudAuthorizationDTO entry) {
    ArrowheadSystem convertedConsumer = ArrowheadSystem.convertToEntity(entry.getConsumer());
    ArrowheadSystem convertedProvider = ArrowheadSystem.convertToEntity(entry.getProvider());
    List<ArrowheadService> services = ArrowheadService.convertToEntity(entry.getService());
    List<IntraCloudAuthorization> convertedEntries = new ArrayList<>();
    for (ArrowheadService service : services) {
      IntraCloudAuthorization convertedEntry = new IntraCloudAuthorization(convertedConsumer, convertedProvider,
                                                                           service);
      if (entry.getId() != null) {
        convertedEntry.setId(entry.getId());
      }
      convertedEntries.add(convertedEntry);
    }
    return convertedEntries;
  }
}
