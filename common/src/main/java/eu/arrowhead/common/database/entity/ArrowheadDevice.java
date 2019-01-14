/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.database.entity;

import com.google.common.base.MoreObjects;
import eu.arrowhead.common.messages.ArrowheadDeviceDTO;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "arrowhead_device")
public class ArrowheadDevice {

  @Id
  @GenericGenerator(name = "table_generator", strategy = "org.hibernate.id.enhanced.TableGenerator")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_generator")
  private Long id;

  @Column(name = "device_name")
  private String deviceName;

  public ArrowheadDevice() {
  }

  public ArrowheadDevice(String deviceName) {
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
    if (!(o instanceof ArrowheadDevice)) {
      return false;
    }
    ArrowheadDevice that = (ArrowheadDevice) o;
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

  public static ArrowheadDeviceDTO convertToDTO(ArrowheadDevice device, boolean includeId) {
    ArrowheadDeviceDTO converted = new ArrowheadDeviceDTO(device.getDeviceName());
    if (includeId) {
      converted.setId(device.getId());
    }
    return converted;
  }

  public static ArrowheadDevice convertToEntity(ArrowheadDeviceDTO device) {
    ArrowheadDevice converted = new ArrowheadDevice(device.getDeviceName());
    if (device.getId() != null) {
      converted.setId(device.getId());
    }
    return converted;
  }
}
