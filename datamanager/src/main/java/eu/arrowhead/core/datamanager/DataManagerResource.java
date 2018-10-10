/*
 *  Copyright (c) 2018 Jens Eliasson, Luleå University of Technology
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.core.datamanager;

import eu.arrowhead.common.Utility;
import eu.arrowhead.common.exception.BadPayloadException;
import eu.arrowhead.common.messages.SenMLMessage;
import eu.arrowhead.common.messages.SigMLMessage;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.log4j.Logger;

/**
 * This is the REST resource for the DataManager Core System.
 */
@Path("datamanager")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataManagerResource {

  private static final Logger log = Logger.getLogger(DataManagerResource.class.getName());

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getIt() {
    log.info("datamanager");
    return "This is the DataManager Arrowhead Core System.";
  }


  /* Historian Service */
  @POST
  @Path("historian")
  public Response storeData(@Valid SenMLMessage sml, @Context ContainerRequestContext requestContext) {
    int statusCode = 0;
    log.info("storage returned with status code: " + 0);
    return Response.status(Status.OK).build();
  }

  @GET
  @Path("historian")
  public String getInfio(@Context ContainerRequestContext requestContext) {
    return "Datamanager::Historian";
  }


  @GET
  @Path("historian/{consumerName}")
  public Response getData(@PathParam("consumerName") String consumerName) {
    int statusCode = 0;
    System.out.println("getData returned with status code: " + statusCode);
    return Response.status(Status.OK).build();
  }

  @PUT
  @Path("historian/{consumerName}")
  public Response PutData(@PathParam("consumerName") String consumerName, @Valid SenMLMessage sml) {
    boolean statusCode = DataManagerService.createEndpoint(consumerName);
    statusCode = DataManagerService.updateEndpoint(consumerName, sml);
    System.out.println("putData returned with status code: " + statusCode + " from: "); // + sml.getBn() + " at: " + sml.getBt());

    return Response.status(Status.OK).build();
  }


  /* Proxy Service */
  @GET
  @Path("proxy")
  public String getInfio() {
    return "DataManager::Proxy";
  }

  @POST
  @Path("proxy")
  public Response proxy(@Context ContainerRequestContext requestContext) {
    int statusCode = 0;
    log.info("Proxy returned with status code: " + 0);
    return Response.status(Status.OK).build();
  }


  @GET
  @Path("proxy/{consumerName}")
  public Response proxyGet(@PathParam("consumerName") String consumerName) {
    int statusCode = 0;
    ProxyElement pe = ProxyService.getEndpoint(consumerName);
    if (pe == null) {
      System.out.println("proxy GET to consumerName: " + consumerName + " not found");
      //System.out.println("proxyGet returned with NULL data");
      return Response.status(Status.NOT_FOUND).build();
    }
//System.out.println("pe.sml: "+ pe.msg + "\t"+pe.msg.toString());

    System.out.println("getData returned with data: " /*+ pe.msg.toString()*/);
    //return Response.status(Status.OK).entity(/*pe.msg*/"{\"p\":0}").build();
    //return Response.status(Status.OK).build(); //entity(/*pe.msg*/"{\"p\":0}").build();
    return Response.status(Status.OK).entity(pe.msg).build();
  }


  @PUT
  @Path("proxy/{consumerName}")
  public Response proxyPut(@PathParam("consumerName") String consumerName, @Valid SigMLMessage sml) {
    ProxyElement pe = ProxyService.getEndpoint(consumerName);
    if (pe == null) {
      System.out.println("consumerName: " + consumerName + " not found, creating");
      pe = new ProxyElement(consumerName);
      ProxyService.addEndpoint(pe);
    }

    boolean statusCode = ProxyService.updateEndpoint(consumerName, sml.msg);
    System.out.println("putData returned with status code: " + statusCode + " from: "); // + sigml.get(0).getBn() + " at: " + sml.get(0).getBt());

    String jsonret = "{\"res\": 0}";
    return Response.ok(jsonret, MediaType.APPLICATION_JSON).build();
  }

  @PUT
  @Path("proxy/{consumerName}")
  public Response proxyPut(@PathParam("consumerName") String consumerName, @Valid List<SenMLMessage> sml) {
    ProxyElement pe = ProxyService.getEndpoint(consumerName);
    if (pe == null) {
      System.out.println("consumerName: " + consumerName + " not found, creating");
      pe = new ProxyElement(consumerName);
      ProxyService.addEndpoint(pe);
    }

    //System.out.println("sml: "+ sml + "\t"+sml.toString());
    boolean statusCode = ProxyService.updateEndpoint(consumerName, sml);
    System.out.println("putData returned with status code: " + statusCode + " from: "); // + sigml.get(0).getBn() + " at: " + sml.get(0).getBt());

    String jsonret = "{\"res\": 0}";
    return Response.ok(jsonret, MediaType.APPLICATION_JSON).build();
    //return Response.status(Status.OK).build();
  }

}
