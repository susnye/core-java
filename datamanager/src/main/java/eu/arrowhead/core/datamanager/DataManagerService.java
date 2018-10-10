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
//import eu.arrowhead.common.messages.SigMLMessage;
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
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

public class DataManagerService {
  private static final Logger log = Logger.getLogger(DataManagerResource.class.getName());
  private static final DatabaseManager dm = DatabaseManager.getInstance();
  private static SessionFactory factory;

  private static List<String> endpoints = new ArrayList<>();

  public DataManagerService() throws Exception {
    try {
      factory = new Configuration().configure().buildSessionFactory();
    } catch (Throwable ex) {
      System.err.println("Failed to create sessionFactory object." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }


  /**
   *
   *
   */
  static int lookupEndpoint(String name) {
    Session session = factory.openSession();
    Transaction tx = null;

    try {
      tx = session.beginTransaction();
      Criteria cr = session.createCriteria(ArrowheadSystem.class);

      if (name != null) {
	cr.add(Restrictions.like("name", "%" + name + "%"));
      }

      List<ArrowheadSystem> systems = cr.list();
      for (Iterator iterator = systems.iterator(); iterator.hasNext(); ) {
	ArrowheadSystem ahsys = (ArrowheadSystem) iterator.next();
      
      }

    } catch (HibernateException e) {
      if (tx != null) {
	tx.rollback();
      }

      e.printStackTrace();
    } 

    return -1; //not found
  }


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

  static SenMLMessage fetchEndpoint(String name) {
    return null;
  }

  static boolean deleteEndpoint(String name) { //XXX: do not support this now right now
    return false;
  }

}
