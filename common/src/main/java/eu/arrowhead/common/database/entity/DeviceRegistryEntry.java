/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.database.entity;

import com.google.common.base.MoreObjects;
import eu.arrowhead.common.messages.ArrowheadDeviceDTO;
import eu.arrowhead.common.messages.DeviceRegistryEntryDTO;
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
@Table(name = "device_registry", uniqueConstraints = {@UniqueConstraint(columnNames = {"arrowhead_device_id"})})
public class DeviceRegistryEntry {

  @Id
  @GenericGenerator(name = "table_generator", strategy = "org.hibernate.id.enhanced.TableGenerator")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_generator")
  private Long id;

  @JoinColumn(name = "arrowhead_device_id")
  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ArrowheadDevice providedDevice;

  @Column(name = "mac_address")
  private String macAddress;

  @Column(name = "end_of_validity")
  private LocalDateTime endOfValidity;

  public DeviceRegistryEntry() {
    super();
  }

  public DeviceRegistryEntry(ArrowheadDevice providedDevice, String macAddress, LocalDateTime endOfValidity) {
    this.providedDevice = providedDevice;
    this.macAddress = macAddress;
    this.endOfValidity = endOfValidity;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ArrowheadDevice getProvidedDevice() {
    return providedDevice;
  }

  public void setProvidedDevice(ArrowheadDevice providedDevice) {
    this.providedDevice = providedDevice;
  }

  public String getMacAddress() {
    return macAddress;
  }

  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  public LocalDateTime getEndOfValidity() {
    return endOfValidity;
  }

  public void setEndOfValidity(LocalDateTime endOfValidity) {
    this.endOfValidity = endOfValidity;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("providedDevice", providedDevice)
                      .add("macAddress", macAddress).add("endOfValidity", endOfValidity).toString();
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
    DeviceRegistryEntry other = (DeviceRegistryEntry) obj;

    return Objects.equals(this.providedDevice, other.providedDevice) && Objects
        .equals(this.macAddress, other.macAddress) && Objects.equals(this.endOfValidity, other.endOfValidity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(providedDevice, macAddress, endOfValidity);
  }

  public static DeviceRegistryEntryDTO convertToDTO(DeviceRegistryEntry entry, boolean includeId) {
    ArrowheadDeviceDTO convertedDevice = ArrowheadDevice.convertToDTO(entry.getProvidedDevice(), includeId);
    DeviceRegistryEntryDTO converted = new DeviceRegistryEntryDTO(convertedDevice, entry.getMacAddress(),
                                                                  entry.getEndOfValidity());
    if (includeId) {
      converted.setId(entry.getId());
    }
    return converted;
  }

  public static DeviceRegistryEntry convertToEntity(DeviceRegistryEntryDTO entry) {
    ArrowheadDevice convertedDevice = ArrowheadDevice.convertToEntity(entry.getProvidedDevice());
    DeviceRegistryEntry converted = new DeviceRegistryEntry(convertedDevice, entry.getMacAddress(),
                                                            entry.getEndOfValidity());
    if (entry.getId() != null) {
      converted.setId(entry.getId());
    }
    return converted;
  }
}
