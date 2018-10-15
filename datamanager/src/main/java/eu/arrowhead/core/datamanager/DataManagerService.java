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
import eu.arrowhead.common.misc.TypeSafeProperties;
import eu.arrowhead.common.Utility;
import eu.arrowhead.core.datamanager.ArrowheadSystem;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.messages.SigMLMessage;
import eu.arrowhead.common.messages.SenMLMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.ServiceConfigurationError;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/*import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;*/
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

final class DataManagerService {
  private static final Logger log = Logger.getLogger(DataManagerResource.class.getName());
  //private static final DatabaseManager dm = DatabaseManager.getInstance();
  //private static SessionFactory factory;
  //private static TypeSafeProperties prop;
  private static Connection connection = null;
  private static String dbAddress;
  private static String dbUser;
  private static String dbPassword;

  private static List<String> endpoints = new ArrayList<>();


  static boolean Init(TypeSafeProperties props){
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Where is your MySQL JDBC Driver?");
      e.printStackTrace();
      return false;
    }

    System.out.println("MySQL JDBC Driver Registered!");
    try {
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dmhistorian","dmuser", "dmpassword");
    } catch (SQLException e) {
      System.out.println("Connection Failed! Check output console");
      e.printStackTrace();
      return false;
    }

    if (connection != null) {
      //System.out.println("You made it, take control your database now!");
    } else {
      System.out.println("Failed to make connection!\nDatabase is offline");
      return false;
    }  

    return true;
  }

  /**
   * @fn static int lookupEndpoint(String name)
   * @brief Returns the database id of a system
   *
   */
  static int lookupEndpoint(String name) {
    /*Session session = dm.getSessionFactory.openSession();
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
	if(ahsys.getName().equals(name) == true) {
	  return ahsys.getId();
	}

      }

    } catch (HibernateException e) {
      if (tx != null) {
	tx.rollback();
      }
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } 
*/
    return -1; //not found
  }


  static boolean createEndpoint(String name) {
    Iterator<String> epi = endpoints.iterator();
    boolean found = false;

    while (epi.hasNext()) {
      String currentep = epi.next();
      if (name.equals(currentep)) {
	System.out.println("Found endpoint: " + currentep);
	found = true;
	return false;
      }
    }

    endpoints.add(name);
    return true;
  }

  static boolean updateEndpoint(String name, SigMLMessage msg) {
    return updateEndpoint(name, msg.sml);
  }

  static boolean updateEndpoint(String name, Vector<SenMLMessage> msg) {
    int id = lookupEndpoint(name);
    System.out.println("Got id of: " + id);

    return false;
  }

  static SenMLMessage fetchEndpoint(String name) {
    return null;
  }

  static boolean deleteEndpoint(String name) { //XXX: do not support this now right now
    return false;
  }

}
