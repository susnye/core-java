/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.database.entity;

import com.google.common.base.MoreObjects;
import eu.arrowhead.common.messages.ArrowheadDeviceDTO;
import eu.arrowhead.common.messages.ArrowheadSystemDTO;
import eu.arrowhead.common.messages.SystemRegistryEntryDTO;
import java.time.LocalDateTime;
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

@Entity
@Table(name = "system_registry", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"arrowhead_system_id", "provider_device_id"})})
public class SystemRegistryEntry {

  @Id
  @GenericGenerator(name = "table_generator", strategy = "org.hibernate.id.enhanced.TableGenerator")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_generator")
  private Long id;

  @JoinColumn(name = "arrowhead_system_id")
  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ArrowheadSystem providedSystem;

  @JoinColumn(name = "provider_device_id")
  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ArrowheadDevice providerDevice;

  @Column(name = "service_uri")
  private String serviceURI;

  @Column(name = "end_of_validity")
  private LocalDateTime endOfValidity;

  public SystemRegistryEntry() {
  }

  public SystemRegistryEntry(ArrowheadSystem providedSystem, ArrowheadDevice providerDevice, String serviceURI,
                             LocalDateTime endOfValidity) {
    this.providedSystem = providedSystem;
    this.providerDevice = providerDevice;
    this.serviceURI = serviceURI;
    this.endOfValidity = endOfValidity;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ArrowheadSystem getProvidedSystem() {
    return providedSystem;
  }

  public void setProvidedSystem(ArrowheadSystem providedSystem) {
    this.providedSystem = providedSystem;
  }

  public ArrowheadDevice getProviderDevice() {
    return providerDevice;
  }

  public void setProviderDevice(ArrowheadDevice providerDevice) {
    this.providerDevice = providerDevice;
  }

  public String getServiceURI() {
    return serviceURI;
  }

  public void setServiceURI(String serviceURI) {
    this.serviceURI = serviceURI;
  }

  public LocalDateTime getEndOfValidity() {
    return endOfValidity;
  }

  public void setEndOfValidity(LocalDateTime endOfValidity) {
    this.endOfValidity = endOfValidity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(providerDevice, providedSystem, serviceURI, endOfValidity);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SystemRegistryEntry other = (SystemRegistryEntry) obj;

    return Objects.equals(this.providerDevice, other.providerDevice) && Objects
        .equals(this.providedSystem, other.providedSystem) && Objects.equals(this.serviceURI, other.serviceURI)
        && Objects.equals(this.endOfValidity, other.endOfValidity);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("providedSystem", providedSystem)
                      .add("providerDevice", providerDevice).add("serviceURI", serviceURI)
                      .add("endOfValidity", endOfValidity).toString();
  }

  public static SystemRegistryEntryDTO convertToDTO(SystemRegistryEntry entry, boolean includeId) {
    ArrowheadSystemDTO convertedSystem = ArrowheadSystem.convertToDTO(entry.getProvidedSystem(), includeId);
    ArrowheadDeviceDTO convertedDevice = ArrowheadDevice.convertToDTO(entry.getProviderDevice(), includeId);
    SystemRegistryEntryDTO converted = new SystemRegistryEntryDTO(convertedSystem, convertedDevice,
                                                                  entry.getServiceURI(), entry.getEndOfValidity());
    if (includeId) {
      converted.setId(entry.getId());
    }
    return converted;
  }

  public static SystemRegistryEntry convertToEntity(SystemRegistryEntryDTO entry) {
    ArrowheadSystem convertedSystem = ArrowheadSystem.convertToEntity(entry.getProvidedSystem());
    ArrowheadDevice convertedDevice = ArrowheadDevice.convertToEntity(entry.getProviderDevice());
    SystemRegistryEntry converted = new SystemRegistryEntry(convertedSystem, convertedDevice, entry.getServiceURI(),
                                                            entry.getEndOfValidity());
    if (entry.getId() != null) {
      converted.setId(entry.getId());
    }
    return converted;
  }
}
