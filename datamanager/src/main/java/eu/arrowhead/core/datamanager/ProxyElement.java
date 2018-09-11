package eu.arrowhead.core.datamanager;

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

