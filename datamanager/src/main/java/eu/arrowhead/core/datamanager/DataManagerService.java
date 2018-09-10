/*
 *  Copyright (c) 2018 Jens Eliasson, Luleå University of Technology
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.core.datamanager;

import eu.arrowhead.common.DatabaseManager;
import eu.arrowhead.common.Utility;
import eu.arrowhead.common.database.ArrowheadSystem;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.messages.SenMLMessage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

final class DataManagerService {

  private static final Logger log = Logger.getLogger(DataManagerResource.class.getName());
  private static final DatabaseManager dm = DatabaseManager.getInstance();

  private static List<String> endpoints = new ArrayList<>();

  static boolean createEndpoint(String name) {
    Iterator<String> epi = endpoints.iterator();
    boolean found = false;

    while (epi.hasNext()) {
      String currentep = epi.next();
      System.out.println("Found endpoint: " + currentep);
      if (name.equals(currentep)) {
        found = true;
        return false;
      }
    }

    endpoints.add(name);
    return true;
  }

  static boolean updateEndpoint(String name, SenMLMessage msg) {
    return false;
  }

  static boolean deleteEndpoint(String name) { //XXX: do not support this now right now
    return false;
  }

}
