/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.database.entity;

import com.google.common.base.MoreObjects;
import eu.arrowhead.common.messages.ArrowheadCloudDTO;
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
import org.hibernate.annotations.Type;

@Entity
@Table(name = "arrowhead_cloud", uniqueConstraints = {@UniqueConstraint(columnNames = {"operator", "cloud_name"})})
public class ArrowheadCloud {

  @Id
  @GenericGenerator(name = "table_generator", strategy = "org.hibernate.id.enhanced.TableGenerator")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_generator")
  private Long id;

  private String operator;

  @Column(name = "cloud_name")
  private String cloudName;

  private String address;

  private Integer port;

  @Column(name = "gatekeeper_service_uri")
  private String gatekeeperServiceURI;

  @Column(name = "authentication_info")
  private String authenticationInfo;

  @Column(name = "is_secure")
  @Type(type = "yes_no")
  private Boolean secure = false;

  public ArrowheadCloud() {
  }

  public ArrowheadCloud(String operator, String cloudName, String address, Integer port, String gatekeeperServiceURI,
                        String authenticationInfo, Boolean secure) {
    this.operator = operator;
    this.cloudName = cloudName;
    this.address = address;
    this.port = port;
    this.gatekeeperServiceURI = gatekeeperServiceURI;
    this.authenticationInfo = authenticationInfo;
    this.secure = secure;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getCloudName() {
    return cloudName;
  }

  public void setCloudName(String cloudName) {
    this.cloudName = cloudName;
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

  public String getGatekeeperServiceURI() {
    return gatekeeperServiceURI;
  }

  public void setGatekeeperServiceURI(String gatekeeperServiceURI) {
    this.gatekeeperServiceURI = gatekeeperServiceURI;
  }

  public String getAuthenticationInfo() {
    return authenticationInfo;
  }

  public void setAuthenticationInfo(String authenticationInfo) {
    this.authenticationInfo = authenticationInfo;
  }

  public Boolean isSecure() {
    return secure;
  }

  public void setSecure(Boolean secure) {
    this.secure = secure;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArrowheadCloud)) {
      return false;
    }
    ArrowheadCloud that = (ArrowheadCloud) o;
    return Objects.equals(operator, that.operator) && Objects.equals(cloudName, that.cloudName) && Objects
        .equals(address, that.address) && Objects.equals(port, that.port) && Objects
        .equals(gatekeeperServiceURI, that.gatekeeperServiceURI) && Objects.equals(secure, that.secure);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operator, cloudName, address, port, gatekeeperServiceURI, secure);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("operator", operator).add("cloudName", cloudName)
                      .add("address", address).add("port", port).add("gatekeeperServiceURI", gatekeeperServiceURI)
                      .add("secure", secure).toString();
  }

  public void partialUpdate(ArrowheadCloud other) {
    this.operator = other.getOperator() != null ? other.getOperator() : this.operator;
    this.cloudName = other.getCloudName() != null ? other.getCloudName() : this.cloudName;
    this.address = other.getAddress() != null ? other.getAddress() : this.address;
    this.port = other.getPort() != null ? other.getPort() : this.port;
    this.gatekeeperServiceURI =
        other.getGatekeeperServiceURI() != null ? other.getGatekeeperServiceURI() : this.gatekeeperServiceURI;
    this.authenticationInfo =
        other.getAuthenticationInfo() != null ? other.getAuthenticationInfo() : this.authenticationInfo;
    this.secure = other.isSecure() != null ? other.isSecure() : this.secure;
  }

  public static ArrowheadCloudDTO convertToDTO(ArrowheadCloud cloud, boolean includeId) {
    ArrowheadCloudDTO converted = new ArrowheadCloudDTO(cloud.getOperator(), cloud.getCloudName(), cloud.getAddress(),
                                                        cloud.getPort(), cloud.getGatekeeperServiceURI(),
                                                        cloud.getAuthenticationInfo(), cloud.isSecure());
    if (includeId) {
      converted.setId(cloud.getId());
    }
    return converted;
  }

  public static ArrowheadCloud convertToEntity(ArrowheadCloudDTO cloud) {
    ArrowheadCloud converted = new ArrowheadCloud(cloud.getOperator(), cloud.getCloudName(), cloud.getAddress(),
                                                  cloud.getPort(), cloud.getGatekeeperServiceURI(),
                                                  cloud.getAuthenticationInfo(), cloud.isSecure());
    if (cloud.getId() != null) {
      converted.setId(cloud.getId());
    }
    return converted;
  }

  public static List<ArrowheadCloudDTO> convertListToDTO(List<ArrowheadCloud> clouds, boolean includeId) {
    return clouds.stream().map(cloud -> convertToDTO(cloud, includeId)).collect(Collectors.toList());
  }

  public static List<ArrowheadCloud> convertListToEntity(List<ArrowheadCloudDTO> clouds) {
    return clouds.stream().map(ArrowheadCloud::convertToEntity).collect(Collectors.toList());
  }
}