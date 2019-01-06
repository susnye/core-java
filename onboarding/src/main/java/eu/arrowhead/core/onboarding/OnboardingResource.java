/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.core.onboarding;

import eu.arrowhead.common.database.SystemRegistryEntry;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.BadPayloadException;
import eu.arrowhead.common.misc.SecurityUtils;
import eu.arrowhead.core.onboarding.model.OnboardingResponse;
import eu.arrowhead.core.onboarding.model.ServiceEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author ZSM
 */
@Path("/onboarding")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OnboardingResource {

  public static final String REQUEST_PATH = "/request";

  private final OnboardingService onboardingService;

  public OnboardingResource() {
    onboardingService = new OnboardingService();
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response ping() {
    return Response.status(Response.Status.OK).entity("This is the Onboarding Arrowhead Core System.").build();
  }

  @POST
  @Path(REQUEST_PATH)
  @Operation(summary = "Onboarding with certificate (as byte array)", responses = {@ApiResponse(content =
  @Content(schema =
  @Schema(implementation = OnboardingResponse.class)))})
  public Response request(final String name, final byte[] certificate) throws ArrowheadException
  {
    final X509Certificate cert = onboardingService.extractCertificate(certificate);
    final Response response;

    if(onboardingService.isCertificateAllowed() && onboardingService.isCertificateValid(cert))
    {
      response = createResponse(name, cert);
    }
    else
    {
      response = Response.status(Status.FORBIDDEN).entity(OnboardingResponse.failure()).build();
    }

    return response;
  }
  @POST
  @Path(REQUEST_PATH)
  @Operation(summary = "Onboarding with certificate (as InputStream)", responses = {@ApiResponse(content =
  @Content(schema =
  @Schema(implementation = OnboardingResponse.class)))})
  public Response request(final String name, final InputStream certificate) throws ArrowheadException
  {
    final X509Certificate cert = onboardingService.extractCertificate(certificate);
    final Response response;

    if(onboardingService.isCertificateAllowed() && onboardingService.isCertificateValid(cert))
    {
      response = createResponse(name, cert);
    }
    else
    {
      response = Response.status(Status.FORBIDDEN).entity(OnboardingResponse.failure()).build();
    }

    return response;
  }

  @POST
  @Path(REQUEST_PATH)
  @Operation(summary = "Onboarding with shared key", responses = {@ApiResponse(content = @Content(schema =
  @Schema(implementation = OnboardingResponse.class)))})
  public Response request(final String name, final String sharedKey) throws ArrowheadException
  {
    final Response response;

    if(onboardingService.isSharedKeyAllowed() && onboardingService.isKeyValid(sharedKey))
    {
      response = createResponse(name);
    }
    else
    {
      response = Response.status(Status.FORBIDDEN).entity(OnboardingResponse.failure()).build();
    }

    return response;
  }

  @POST
  @Path(REQUEST_PATH)
  @Operation(summary = "Onboarding without credentials", responses = {@ApiResponse(content = @Content(schema =
  @Schema(implementation = OnboardingResponse.class)))})
  public Response request(final String name) throws ArrowheadException
  {
    final Response response;

    if(onboardingService.isUnknownAllowed())
    {
      response = createResponse(name);
    }
    else
    {
      response = Response.status(Status.FORBIDDEN).entity(OnboardingResponse.failure()).build();
    }

    return response;
  }

  private Response createResponse(final String name)
  {
    final byte[] onboardingCertificate = onboardingService.createCertificate(name);
    final ServiceEndpoint[] endpoints = onboardingService.getEndpoints();
    final OnboardingResponse returnValue = OnboardingResponse.success(onboardingCertificate, endpoints);
    return Response.status(Status.OK).entity(returnValue).build();
  }

  private Response createResponse(final String name, final Certificate providedCertificate)
  {
    final byte[] onboardingCertificate = onboardingService.createCertificate(name, providedCertificate);
    final ServiceEndpoint[] endpoints = onboardingService.getEndpoints();
    final OnboardingResponse returnValue = OnboardingResponse.success(onboardingCertificate, endpoints);
    return Response.status(Status.OK).entity(returnValue).build();
  }
}