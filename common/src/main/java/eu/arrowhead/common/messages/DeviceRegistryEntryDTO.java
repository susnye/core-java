/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.messages;

import com.google.common.base.MoreObjects;
import eu.arrowhead.common.json.constraint.LDTInFuture;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DeviceRegistryEntryDTO {

  private Long id;

  @Valid
  @NotNull(message = "Provided ArrowheadDevice cannot be null")
  private ArrowheadDeviceDTO providedDevice;

  @Size(max = 255, message = "macAddress must be 255 character at max")
  private String macAddress;

  @LDTInFuture(message = "End of validity date cannot be in the past")
  private LocalDateTime endOfValidity;

  public DeviceRegistryEntryDTO() {
    super();
  }

  public DeviceRegistryEntryDTO(ArrowheadDeviceDTO providedDevice, String macAddress, LocalDateTime endOfValidity) {
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

  public ArrowheadDeviceDTO getProvidedDevice() {
    return providedDevice;
  }

  public void setProvidedDevice(ArrowheadDeviceDTO providedDevice) {
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
    DeviceRegistryEntryDTO other = (DeviceRegistryEntryDTO) obj;

    return Objects.equals(this.providedDevice, other.providedDevice) && Objects
        .equals(this.macAddress, other.macAddress) && Objects.equals(this.endOfValidity, other.endOfValidity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(providedDevice, macAddress, endOfValidity);
  }
}
