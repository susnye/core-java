package eu.arrowhead.core.datamanager;

import eu.arrowhead.common.ArrowheadMain;
import eu.arrowhead.common.misc.CoreSystem;
import java.util.HashSet;
import java.util.Set;

public class DataManagerMain extends ArrowheadMain {

  private DataManagerMain(String[] args) {
    Set<Class<?>> classes = new HashSet<>();
    String[] packages = {"eu.arrowhead.common.exception", "eu.arrowhead.common.json", "eu.arrowhead.common.filter"};
    init(CoreSystem.CHOREOGRAPHER, args, classes, packages);
    listenForInput();
  }

  public static void main(String[] args) {
    new DataManagerMain(args);
  }

}
