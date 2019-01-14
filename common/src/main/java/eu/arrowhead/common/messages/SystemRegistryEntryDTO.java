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

public class SystemRegistryEntryDTO {

  private Long id;

  @Valid
  @NotNull(message = "Provided ArrowheadSystem cannot be null")
  private ArrowheadSystemDTO providedSystem;

  @Valid
  @NotNull(message = "Provider ArrowheadDevice cannot be null")
  private ArrowheadDeviceDTO providerDevice;

  @Size(max = 255, message = "Service URI must be 255 character at max")
  private String serviceURI;

  @LDTInFuture(message = "End of validity date cannot be in the past")
  private LocalDateTime endOfValidity;

  public SystemRegistryEntryDTO() {
  }

  public SystemRegistryEntryDTO(ArrowheadSystemDTO providedSystem, ArrowheadDeviceDTO providerDevice, String serviceURI,
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

  public ArrowheadSystemDTO getProvidedSystem() {
    return providedSystem;
  }

  public void setProvidedSystem(ArrowheadSystemDTO providedSystem) {
    this.providedSystem = providedSystem;
  }

  public ArrowheadDeviceDTO getProviderDevice() {
    return providerDevice;
  }

  public void setProviderDevice(ArrowheadDeviceDTO providerDevice) {
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
    SystemRegistryEntryDTO other = (SystemRegistryEntryDTO) obj;

    return Objects.equals(this.providerDevice, other.providerDevice) && Objects
        .equals(this.providedSystem, other.providedSystem)
        && Objects.equals(this.serviceURI, other.serviceURI) && Objects.equals(this.endOfValidity, other.endOfValidity);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("providedSystem", providedSystem)
                      .add("providerDevice", providerDevice).add("serviceURI", serviceURI)
                      .add("endOfValidity", endOfValidity).toString();
  }
}
