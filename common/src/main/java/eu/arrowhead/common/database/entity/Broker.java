/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.database.entity;

import com.google.common.base.MoreObjects;
import eu.arrowhead.common.messages.BrokerDTO;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "broker")
public class Broker {

  @Id
  @GenericGenerator(name = "table_generator", strategy = "org.hibernate.id.enhanced.TableGenerator")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_generator")
  private Long id;

  private String address;

  private Integer port;

  @Column(name = "is_secure")
  @Type(type = "yes_no")
  private Boolean secure = false;

  public Broker() {
  }

  public Broker(String address, Integer port, Boolean secure) {
    this.address = address;
    this.port = port;
    this.secure = secure;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    if (!(o instanceof Broker)) {
      return false;
    }
    Broker broker = (Broker) o;
    return Objects.equals(address, broker.address) && Objects.equals(port, broker.port) && Objects.equals(secure, broker.secure);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, port, secure);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("address", address).add("port", port).add("secure", secure).toString();
  }

  public static BrokerDTO convertToDTO(Broker broker, boolean includeId) {
    BrokerDTO converted = new BrokerDTO(broker.getAddress(), broker.getPort(), broker.isSecure());
    if (includeId) {
      converted.setId(broker.getId());
    }
    return converted;
  }

  public static Broker convertToEntity(BrokerDTO broker) {
    Broker converted = new Broker(broker.getAddress(), broker.getPort(), broker.isSecure());
    if (broker.getId() != null) {
      converted.setId(broker.getId());
    }
    return converted;
  }
}
