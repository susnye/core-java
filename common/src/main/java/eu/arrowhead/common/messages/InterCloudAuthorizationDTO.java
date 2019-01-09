/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.messages;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class InterCloudAuthorizationDTO {

  private Long id;

  @Valid
  @NotNull
  private ArrowheadCloudDTO cloud;

  @Valid
  @NotNull
  private ArrowheadServiceDTO service;

  public InterCloudAuthorizationDTO() {
  }

  public InterCloudAuthorizationDTO(ArrowheadCloudDTO cloud, ArrowheadServiceDTO service) {
    this.cloud = cloud;
    this.service = service;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ArrowheadCloudDTO getCloud() {
    return cloud;
  }

  public void setCloud(ArrowheadCloudDTO cloud) {
    this.cloud = cloud;
  }

  public ArrowheadServiceDTO getService() {
    return service;
  }

  public void setService(ArrowheadServiceDTO service) {
    this.service = service;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof InterCloudAuthorizationDTO)) {
      return false;
    }
    InterCloudAuthorizationDTO that = (InterCloudAuthorizationDTO) o;
    return Objects.equals(cloud, that.cloud) && Objects.equals(service, that.service);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cloud, service);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("cloud", cloud).add("service", service).toString();
  }
}
