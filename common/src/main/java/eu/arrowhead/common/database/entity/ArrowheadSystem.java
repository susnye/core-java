/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.database.entity;

import com.google.common.base.MoreObjects;
import eu.arrowhead.common.messages.ArrowheadSystemDTO;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "arrowhead_system", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"system_name", "address", "port"})})
public class ArrowheadSystem {

  @Id
  @GenericGenerator(name = "table_generator", strategy = "org.hibernate.id.enhanced.TableGenerator")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_generator")
  private Long id;

  @Column(name = "system_name")
  private String systemName;

  private String address;

  private Integer port;

  @Column(name = "authentication_info")
  private String authenticationInfo;

  public ArrowheadSystem() {
  }

  public ArrowheadSystem(String systemName, String address, Integer port, String authenticationInfo) {
    this.systemName = systemName;
    this.address = address;
    this.port = port;
    this.authenticationInfo = authenticationInfo;
  }

  @SuppressWarnings("CopyConstructorMissesField")
  public ArrowheadSystem(ArrowheadSystem system) {
    this.systemName = system.systemName;
    this.address = system.address;
    this.port = system.port;
    this.authenticationInfo = system.authenticationInfo;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSystemName() {
    return systemName;
  }

  public void setSystemName(String systemName) {
    this.systemName = systemName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getAuthenticationInfo() {
    return authenticationInfo;
  }

  public void setAuthenticationInfo(String authenticationInfo) {
    this.authenticationInfo = authenticationInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArrowheadSystem)) {
      return false;
    }
    ArrowheadSystem that = (ArrowheadSystem) o;
    return Objects.equals(systemName, that.systemName) && Objects.equals(address, that.address) && Objects
        .equals(port, that.port);
  }

  @Override
  public int hashCode() {
    return Objects.hash(systemName, address, port);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).add("systemName", systemName).add("address", address)
                      .add("port", port).add("authenticationInfo", authenticationInfo).toString();
  }

  public void partialUpdate(ArrowheadSystem other) {
    this.systemName = other.getSystemName() != null ? other.getSystemName() : this.systemName;
    this.address = other.getAddress() != null ? other.getAddress() : this.address;
    this.port = other.getPort() != null ? other.getPort() : this.port;
    this.authenticationInfo =
        other.getAuthenticationInfo() != null ? other.getAuthenticationInfo() : this.authenticationInfo;
  }

  public static ArrowheadSystemDTO convertToDTO(ArrowheadSystem system, boolean includeId) {
    ArrowheadSystemDTO converted = new ArrowheadSystemDTO(system.getSystemName(), system.getAddress(), system.getPort(),
                                                          system.getAuthenticationInfo());
    if (includeId) {
      converted.setId(system.getId());
    }
    return converted;
  }

  public static ArrowheadSystem convertToEntity(ArrowheadSystemDTO system) {
    ArrowheadSystem converted = new ArrowheadSystem(system.getSystemName(), system.getAddress(), system.getPort(),
                                                    system.getAuthenticationInfo());
    if (system.getId() != null) {
      converted.setId(system.getId());
    }
    return converted;
  }

  public static List<ArrowheadSystemDTO> convertListToDTO(List<ArrowheadSystem> systems, boolean includeId) {
    return systems.stream().map(system -> convertToDTO(system, includeId)).collect(Collectors.toList());
  }

  public static List<ArrowheadSystem> convertListToEntity(List<ArrowheadSystemDTO> systems) {
    return systems.stream().map(ArrowheadSystem::convertToEntity).collect(Collectors.toList());
  }
}
