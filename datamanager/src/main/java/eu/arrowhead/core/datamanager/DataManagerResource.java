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
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
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

  @POST
  @Path("storage")
  public Response storeData(@Valid SenMLMessage sml, @Context ContainerRequestContext requestContext) {
    int statusCode = 0;
    log.info("storage returned with status code: " + 0);
    return Response.status(Status.OK).build();
  }


  @GET
  @Path("storage/{consumerName}")
  public Response getData(@PathParam("consumerName") String consumerName) {
    int statusCode = 0;
    System.out.println("getData returned with status code: " + statusCode);
    return Response.status(Status.OK).build();
  }

  @PUT
  @Path("storage/{consumerName}")
  public Response PutData(@PathParam("consumerName") String consumerName, @Valid SenMLMessage sml) {
    int statusCode = 0;
    System.out.println("putData returned with status code: " + statusCode + " from: " + sml.getBn() + " at: "+sml.getBt());
    return Response.status(Status.OK).build();
  }


}
