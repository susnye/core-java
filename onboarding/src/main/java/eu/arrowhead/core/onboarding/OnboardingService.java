/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.core.onboarding;

import eu.arrowhead.common.DatabaseManager;
import eu.arrowhead.common.Utility;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.BadPayloadException;
import eu.arrowhead.common.misc.TypeSafeProperties;
import eu.arrowhead.core.onboarding.model.ServiceEndpoint;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class OnboardingService {

	private static final String PROPERTY_UNKNOWN_ENABLED = "enable_unknown";
	private static final String PROPERTY_SHARED_KEY_ENABLED = "enable_shared_key";
	private static final String PROPERTY_CERTIFICATE_ENABLED = "enable_certificate";
	private static final String PROPERTY_SHARED_KEY = "shared_key";

	private final DatabaseManager databaseManager;
	private final TypeSafeProperties props;
	private final CertificateFactory cf;

  public OnboardingService() {
  	try {
			databaseManager = DatabaseManager.getInstance();
			props = Utility.getProp();
			cf = CertificateFactory.getInstance("X.509");
		}
  	catch (CertificateException ce)
		{
			throw new ArrowheadException(ce.getMessage(), ce);
		}
	}

	public boolean isUnknownAllowed() {
  	return props.getBooleanProperty(PROPERTY_UNKNOWN_ENABLED, false);
	}

	public boolean isSharedKeyAllowed() {
		return props.getBooleanProperty(PROPERTY_SHARED_KEY_ENABLED, false);
	}

	public boolean isCertificateAllowed() {
		return props.getBooleanProperty(PROPERTY_CERTIFICATE_ENABLED, false);
	}

	public byte[] createCertificate(final String name) {
		return null;
  }

	public byte[] createCertificate(final String name, final Certificate providedCertificate) {
  	return null;
	}

	public ServiceEndpoint[] getEndpoints() {
  	return null;
	}

	public boolean isKeyValid(final String providedKey) {
  	final String sharedKey = props.getProperty(PROPERTY_SHARED_KEY, null);

  	if(sharedKey == null || providedKey == null)
		{
			return false;
		}

  	return sharedKey.equals(providedKey);
	}

	public boolean isCertificateValid(final X509Certificate cert) {
  	return false;
	}

	public X509Certificate extractCertificate(final byte[] certificate) {
		final X509Certificate cert;
		try(final ByteArrayInputStream bais = new ByteArrayInputStream(certificate))
		{
			return extractCertificate(bais);
		} catch (IOException e) {
			throw new BadPayloadException(e.getMessage(), e);
		}
	}

	public X509Certificate extractCertificate(InputStream certificate) {
		final X509Certificate cert;
		try {
			cert = (X509Certificate) cf.generateCertificate(certificate);
			cert.checkValidity();
		} catch (CertificateException e) {
			throw new BadPayloadException(e.getMessage(), e);
		}
		return cert;
	}
}
