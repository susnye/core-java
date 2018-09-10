/*
 *  Copyright (c) 2018 Jens Eliasson, Luleå University of Technology
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.common.messages;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SenMLMessage{

  @Valid
  @NotNull
  private String bn;

  public SenMLMessage() {

  }

  public void setBn(String bn) {
    this.bn = bn;
  }

  public String getBn() {
    return bn;
  }

}
