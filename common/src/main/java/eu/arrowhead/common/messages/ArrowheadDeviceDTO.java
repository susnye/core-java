/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.messages;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

public class ArrowheadDeviceDTO {

  private Long id;

  @NotBlank
  @Size(max = 255, message = "System name must be 255 character at max")
  private String deviceName;

  public ArrowheadDeviceDTO() {
  }

  public ArrowheadDeviceDTO(String deviceName) {
    this.deviceName = deviceName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArrowheadDeviceDTO)) {
      return false;
    }
    ArrowheadDeviceDTO that = (ArrowheadDeviceDTO) o;
    return Objects.equals(deviceName, that.deviceName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceName);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("deviceName", deviceName).toString();
  }
}
