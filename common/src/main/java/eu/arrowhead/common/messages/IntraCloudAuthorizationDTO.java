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

public class IntraCloudAuthorizationDTO {

  private Long id;

  @Valid
  @NotNull
  private ArrowheadSystemDTO consumer;

  @Valid
  @NotNull
  private ArrowheadSystemDTO provider;

  @Valid
  @NotNull
  private ArrowheadServiceDTO service;

  public IntraCloudAuthorizationDTO() {
  }

  public IntraCloudAuthorizationDTO(ArrowheadSystemDTO consumer, ArrowheadSystemDTO provider,
                                    ArrowheadServiceDTO service) {
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

  public ArrowheadSystemDTO getConsumer() {
    return consumer;
  }

  public void setConsumer(ArrowheadSystemDTO consumer) {
    this.consumer = consumer;
  }

  public ArrowheadSystemDTO getProvider() {
    return provider;
  }

  public void setProvider(ArrowheadSystemDTO providers) {
    this.provider = providers;
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
    if (!(o instanceof IntraCloudAuthorizationDTO)) {
      return false;
    }
    IntraCloudAuthorizationDTO that = (IntraCloudAuthorizationDTO) o;
    return Objects.equals(consumer, that.consumer) && Objects.equals(provider, that.provider) && Objects
        .equals(service, that.service);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consumer, provider, service);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("consumer", consumer).add("provider", provider).add("service", service)
                      .toString();
  }
}
