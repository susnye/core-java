/*
 * This work is part of the Productive 4.0 innovation project, which receives grants from the
 * European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 * (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 * national funding authorities from involved countries.
 */

package eu.arrowhead.core.onboarding;

import eu.arrowhead.common.Utility;
import eu.arrowhead.common.exception.BadPayloadException;
import eu.arrowhead.common.misc.CoreSystem;
import eu.arrowhead.common.misc.CoreSystemService;
import eu.arrowhead.common.misc.TypeSafeProperties;
import eu.arrowhead.core.certificate_authority.model.CertificateSigningRequest;
import eu.arrowhead.core.certificate_authority.model.CertificateSigningResponse;
import eu.arrowhead.core.onboarding.model.ServiceEndpoint;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import javax.security.auth.x500.X500Principal;
import javax.ws.rs.core.Response;
import sun.security.pkcs10.PKCS10;
import sun.security.x509.X500Name;

public class OnboardingService {

    // TODO CA does not expose a service ?!
    private static final String PROPERTY_CA_IP = "ca_url";
    private static final String PROPERTY_UNKNOWN_ENABLED = "enable_unknown";
    private static final String PROPERTY_SHARED_KEY_ENABLED = "enable_shared_key";
    private static final String PROPERTY_CERTIFICATE_ENABLED = "enable_certificate";
    private static final String PROPERTY_SHARED_KEY = "shared_key";

    private final TypeSafeProperties props;
    private final String caUri;

    public OnboardingService() {
        props = Utility.getProp();
        caUri = Utility.getUri(getCaIp(), CoreSystem.CERTIFICATE_AUTHORITY.getInsecurePort(), "ca", false, false);
    }

    public String getCaIp() {
        return props.getProperty(PROPERTY_CA_IP, "127.0.0.1");
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

    public KeyPair generateKeyPair(final String algorithm, int keySize) throws NoSuchAlgorithmException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public CertificateSigningResponse createAndSignCertificate(final String name, final KeyPair keyPair)
        throws IOException, NoSuchAlgorithmException, InvalidKeyException, CertificateException, SignatureException {
        final String cloudName = getCloudname(caUri);
        final String encodedCert = prepareAndCreateCSR(name + "." + cloudName, keyPair);
        return sendCsr(new CertificateSigningRequest(encodedCert));
    }

    private String getCloudname(final String caUri) {
        Response caResponse = Utility.sendRequest(caUri, "GET", null);
        return caResponse.readEntity(String.class);
    }

    private String prepareAndCreateCSR(final String name, final KeyPair keyPair)
        throws IOException, NoSuchAlgorithmException, InvalidKeyException, CertificateException, SignatureException {
        final X500Name x500Name = new X500Name("CN=" + name);
        final Signature sig = Signature.getInstance("SHA256WithRSA");
        sig.initSign(keyPair.getPrivate());

        return createCSR(x500Name, sig, keyPair.getPublic());
    }

    private CertificateSigningResponse sendCsr(final CertificateSigningRequest csr) {
        final Response caResponse = Utility.sendRequest(caUri, "POST", csr);
        return caResponse.readEntity(CertificateSigningResponse.class);
    }

    private String createCSR(final X500Name x500name, final Signature signature, final PublicKey publicKey)
        throws CertificateException, SignatureException, IOException {
        final PKCS10 pkcs10 = new PKCS10(publicKey);
        pkcs10.encodeAndSign(x500name, signature);
        return Base64.getEncoder().encodeToString(pkcs10.getEncoded());
    }

    public CertificateSigningResponse signCertificate(final PKCS10 providedCsr) {
        final String encodedCert = Base64.getEncoder().encodeToString(providedCsr.getEncoded());
        return sendCsr(new CertificateSigningRequest(encodedCert));
    }

    public ServiceEndpoint[] getEndpoints() throws URISyntaxException {

        final List<ServiceEndpoint> endpoints = new ArrayList<>();

        final Optional<String[]> deviceRegistry = Utility
            .getServiceInfo(CoreSystemService.DEVICE_REG_SERVICE.getServiceDef());
        final Optional<String[]> systemRegistry = Utility
            .getServiceInfo(CoreSystemService.SYS_REG_SERVICE.getServiceDef());
        final Optional<String[]> serviceRegistry = Utility.getServiceInfo("ServiceRegistry");

        if (deviceRegistry.isPresent()) {
            endpoints.add(new ServiceEndpoint(CoreSystem.DEVICE_REGISTRY, new URI(deviceRegistry.get()[0])));
        }

        if (systemRegistry.isPresent()) {
            endpoints.add(new ServiceEndpoint(CoreSystem.SYSTEM_REGISTRY, new URI(systemRegistry.get()[0])));
        }

        if (serviceRegistry.isPresent()) {
            endpoints.add(new ServiceEndpoint(CoreSystem.SERVICE_REGISTRY_SQL, new URI(deviceRegistry.get()[0])));
        }

        return endpoints.toArray(new ServiceEndpoint[0]);
    }

    public boolean isKeyValid(final String providedKey) {
        final String sharedKey = props.getProperty(PROPERTY_SHARED_KEY, null);

        if (sharedKey == null || providedKey == null) {
            return false;
        }

        return sharedKey.equals(providedKey);
    }

    public boolean isCertificateValid(final X509Certificate cert) {
        try {
            cert.checkValidity();
            cert.verify(cert.getPublicKey());
            return true;
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
            return false;
        }
    }

    public PKCS10 extractPKCS10(final String certificate) {
        try {
            return new PKCS10(certificate.getBytes());
        } catch (IOException | SignatureException | NoSuchAlgorithmException e) {
            throw new BadPayloadException(e.getMessage(), e);
        }
    }
}
