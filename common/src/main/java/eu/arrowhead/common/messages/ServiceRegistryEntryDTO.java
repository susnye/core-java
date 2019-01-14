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

public class ServiceRegistryEntryDTO {

  private Long id;

  @Valid
  @NotNull(message = "Provided ArrowheadService cannot be null")
  private ArrowheadServiceDTO providedService;

  @Valid
  @NotNull(message = "Provider ArrowheadSystem cannot be null")
  private ArrowheadSystemDTO provider;

  @Size(max = 255, message = "Service URI must be 255 character at max")
  private String serviceURI;

  private Boolean udp = false;

  @LDTInFuture(message = "End of validity date must be in the future")
  private LocalDateTime endOfValidity;

  private Integer version = 1;

  public ServiceRegistryEntryDTO() {
  }

  public ServiceRegistryEntryDTO(ArrowheadServiceDTO providedService, ArrowheadSystemDTO provider, String serviceURI) {
    this.providedService = providedService;
    this.provider = provider;
    this.serviceURI = serviceURI;
  }

  public ServiceRegistryEntryDTO(ArrowheadServiceDTO providedService, ArrowheadSystemDTO provider, String serviceURI,
                                 boolean udp, LocalDateTime endOfValidity, int version) {
    this.providedService = providedService;
    this.provider = provider;
    this.serviceURI = serviceURI;
    this.udp = udp;
    this.endOfValidity = endOfValidity;
    this.version = version;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ArrowheadServiceDTO getProvidedService() {
    return providedService;
  }

  public void setProvidedService(ArrowheadServiceDTO providedService) {
    this.providedService = providedService;
  }

  public ArrowheadSystemDTO getProvider() {
    return provider;
  }

  public void setProvider(ArrowheadSystemDTO provider) {
    this.provider = provider;
  }

  public String getServiceURI() {
    return serviceURI;
  }

  public void setServiceURI(String serviceURI) {
    this.serviceURI = serviceURI;
  }

  public Boolean isUdp() {
    return udp;
  }

  public void setUdp(Boolean udp) {
    this.udp = udp;
  }

  public LocalDateTime getEndOfValidity() {
    return endOfValidity;
  }

  public void setEndOfValidity(LocalDateTime endOfValidity) {
    this.endOfValidity = endOfValidity;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ServiceRegistryEntryDTO)) {
      return false;
    }
    ServiceRegistryEntryDTO that = (ServiceRegistryEntryDTO) o;
    return Objects.equals(providedService, that.providedService) && Objects.equals(provider, that.provider) && Objects
        .equals(serviceURI, that.serviceURI) && Objects.equals(version, that.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(providedService, provider, serviceURI, version);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("providedService", providedService).add("provider", provider)
                      .add("serviceURI", serviceURI).add("version", version).toString();
  }
}