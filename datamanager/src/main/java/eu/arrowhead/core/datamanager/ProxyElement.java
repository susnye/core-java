/*
 *  Copyright (c) 2018 Jens Eliasson, Luleå University of Technology
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.core.datamanager;

//import eu.arrowhead.common.messages.SigMLMessage;
import eu.arrowhead.common.messages.SenMLMessage;

public class ProxyElement {

  public String name = null;
  public SenMLMessage msg = null;

  public ProxyElement(String name) {
    this.name = name;
    this.msg = null;
  }

  public ProxyElement(String name, SenMLMessage msg) {
    this.name = name;
    this.msg = msg;
  }

}

