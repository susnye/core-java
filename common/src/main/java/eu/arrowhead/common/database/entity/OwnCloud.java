/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.common.database.entity;

import com.google.common.base.MoreObjects;
import eu.arrowhead.common.messages.ArrowheadCloudDTO;
import eu.arrowhead.common.messages.GSDPoll;
import eu.arrowhead.common.messages.ICNProposal;
import eu.arrowhead.common.messages.OwnCloudDTO;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * JPA entity class for storing <tt>OwnCloud</tt> information in the database. The <i>cloud_id</i> column must be unique. <p> The database table
 * belonging to this class stores the information about the Local Cloud, meaning this table should only have 1 entry at all times. The information in
 * this table is used during Global Service Discovery, Inter-Cloud Negotiations (by the Gatekeeper) and token generation (by the Authorization).
 *
 * @author Umlauf Zoltán
 * @see ArrowheadCloud
 * @see GSDPoll
 * @see ICNProposal
 */
@Entity
@Table(name = "own_cloud", uniqueConstraints = {@UniqueConstraint(columnNames = {"cloud_id"})})
public class OwnCloud implements Serializable {

  @Id
  @JoinColumn(name = "cloud_id")
  @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ArrowheadCloud cloud;

  public OwnCloud() {
  }

  public OwnCloud(ArrowheadCloud cloud) {
    this.cloud = cloud;
  }

  public ArrowheadCloud getCloud() {
    return cloud;
  }

  public void setCloud(ArrowheadCloud cloud) {
    this.cloud = cloud;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OwnCloud)) {
      return false;
    }
    OwnCloud ownCloud = (OwnCloud) o;
    return Objects.equals(cloud, ownCloud.cloud);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cloud);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("cloud", cloud).toString();
  }

  public static OwnCloudDTO convertToDTO(OwnCloud cloud, boolean includeId) {
    ArrowheadCloudDTO convertedCloud = ArrowheadCloud.convertToDTO(cloud.getCloud(), includeId);
    return new OwnCloudDTO(convertedCloud);
  }

  public static OwnCloud convertToEntity(OwnCloudDTO cloud) {
    ArrowheadCloud convertedCloud = ArrowheadCloud.convertToEntity(cloud.getCloud());
    return new OwnCloud(convertedCloud);
  }
}
