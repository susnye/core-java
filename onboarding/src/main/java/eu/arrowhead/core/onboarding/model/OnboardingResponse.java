package eu.arrowhead.core.onboarding.model;

public class OnboardingResponse {

  private final boolean success;
  private final byte[] onboardingCertificate;
  private final ServiceEndpoint[] services;

  private OnboardingResponse(boolean success, byte[] certificate, ServiceEndpoint[] services) {
    this.success = success;
    this.onboardingCertificate = certificate;
    this.services = services;
  }

  public static OnboardingResponse success(byte[] onboardingCertificate, ServiceEndpoint[] services)
  {
    return new OnboardingResponse(true, onboardingCertificate, services);
  }

  public static OnboardingResponse failure()
  {
    return new OnboardingResponse(false, null, null);
  }

  public boolean isSuccess() {
    return success;
  }

  public byte[] getOnboardingCertificate() {
    return onboardingCertificate;
  }

  public ServiceEndpoint[] getServices() {
    return services;
  }
}
