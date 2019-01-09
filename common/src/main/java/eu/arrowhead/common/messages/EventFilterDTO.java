/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.messages;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.base.MoreObjects;
import eu.arrowhead.common.exception.BadPayloadException;
import eu.arrowhead.common.json.constraint.ZDTInFuture;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

public class EventFilterDTO {

  private Long id;

  @NotBlank
  @Size(max = 255, message = "Event type must be 255 character at max")
  private String eventType;

  @Valid
  @NotNull(message = "Consumer ArrowheadSystem cannot be null")
  private ArrowheadSystemDTO consumer;

  @Valid
  @Size(max = 100, message = "Event filter can only have 100 sources at max")
  private Set<ArrowheadSystemDTO> sources = new HashSet<>();

  private ZonedDateTime startDate;

  @ZDTInFuture(message = "Filter end date must be in the future")
  private ZonedDateTime endDate;

  private Map<String, String> filterMetadata = new HashMap<>();

  private String notifyUri;

  //TODO provide a REST interface to easily switch this
  private Boolean matchMetadata = false;

  public EventFilterDTO() {
  }

  public EventFilterDTO(String eventType, ArrowheadSystemDTO consumer, Set<ArrowheadSystemDTO> sources,
                        ZonedDateTime startDate, ZonedDateTime endDate, Map<String, String> filterMetadata,
                        String notifyUri, boolean matchMetadata) {
    this.eventType = eventType;
    this.consumer = consumer;
    this.sources = sources;
    this.startDate = startDate;
    this.endDate = endDate;
    this.filterMetadata = filterMetadata;
    this.notifyUri = notifyUri;
    this.matchMetadata = matchMetadata;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public ArrowheadSystemDTO getConsumer() {
    return consumer;
  }

  public void setConsumer(ArrowheadSystemDTO consumer) {
    this.consumer = consumer;
  }

  public Set<ArrowheadSystemDTO> getSources() {
    return sources;
  }

  public void setSources(Set<ArrowheadSystemDTO> sources) {
    this.sources = sources;
  }

  public ZonedDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(ZonedDateTime startDate) {
    this.startDate = startDate;
  }

  public ZonedDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(ZonedDateTime endDate) {
    this.endDate = endDate;
  }

  public Map<String, String> getFilterMetadata() {
    return filterMetadata;
  }

  @JsonSetter
  public void setFilterMetadata(Map<String, String> filterMetadata) {
    for (Map.Entry<String, String> entry : filterMetadata.entrySet()) {
      String key = entry.getKey();
      if (key == null || key.trim().isEmpty()) {
        throw new BadPayloadException("EventFilter metadata key can not be blank!");
      }
      String value = entry.getValue();
      if (value == null || value.trim().isEmpty()) {
        throw new BadPayloadException("EventFilter metadata value can not be blank!");
      }
    }
    this.filterMetadata = filterMetadata;
  }

  public String getNotifyUri() {
    return notifyUri;
  }

  public void setNotifyUri(String notifyUri) {
    this.notifyUri = notifyUri;
  }

  public Boolean isMatchMetadata() {
    return matchMetadata;
  }

  public void setMatchMetadata(Boolean matchMetadata) {
    this.matchMetadata = matchMetadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EventFilterDTO)) {
      return false;
    }
    EventFilterDTO that = (EventFilterDTO) o;
    return Objects.equals(eventType, that.eventType) && Objects.equals(consumer, that.consumer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, consumer);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("eventType", eventType).add("consumer", consumer).toString();
  }
}
