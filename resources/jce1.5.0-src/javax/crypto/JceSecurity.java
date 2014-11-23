/*
 * @(#)JceSecurity.java	1.50 04/04/14
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.crypto;

import java.lang.ref.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import java.security.Provider.Service;

import sun.security.validator.Validator;

import sun.security.jca.*;
import sun.security.jca.GetInstance.Instance;

/**
 * This class instantiates implementations of JCE engine classes from
 * providers registered with the java.security.Security object.
 *
 * @author Jan Luehe
 * @author Sharon Liu
 * @version 1.50, 04/14/04
 * @since 1.4
 */

final class JceSecurity {
    
    // Are we debugging? -- for developers
    static final boolean debug = false;

    static final SecureRandom RANDOM = new SecureRandom();

    // The defaultPolicy and exemptPolicy will be set up
    // in the static initializer.
    private static CryptoPermissions defaultPolicy = null;
    private static CryptoPermissions exemptPolicy = null;
    
    // Map<Provider,?> of the providers we already have verified
    // value == PROVIDER_VERIFIED is successfully verified
    // value is failure cause Exception in error case
    private final static Map verificationResults = new IdentityHashMap();
    
    // Map<Provider,?> of the providers currently being verified
    private final static Map verifyingProviders = new IdentityHashMap();

    // Set the default value. May be changed in the static initializer.
    private static boolean isRestricted = true;

    // X.509 certificate factory used to setup the certificates
    private static CertificateFactory certificateFactory;
    
    // certificate used to sign the framework and the policy files
    private static X509Certificate jceCertificate;
    
    // validator instance to verify provider JAR files
    private static Validator providerValidator;
    
    // validator instance to verify exempt application JAR files
    // or null if the providerValidator should be used
    private static Validator exemptValidator;
    
    /*
     * Don't let anyone instantiate this. 
     */
    private JceSecurity() {
    }
    
    static {
/* FIRST CA CERTIFICATE TRUSTED TO SIGN JCE PROVIDERS
Owner: CN=JCE Code Signing CA, OU=Java Software Code Signing, 
O=Sun Microsystems Inc, L=Palo Alto, ST=CA, C=US
Issuer: CN=JCE Code Signing CA, OU=Java Software Code Signing, 
O=Sun Microsystems Inc, L=Palo Alto, ST=CA, C=US
Serial number: 10
Valid from: Wed Apr 25 00:00:00 PDT 2001 until: Sat Apr 25 00:00:00 PDT 2020
Certificate fingerprints:
         MD5:  66:25:5A:78:3E:1A:CA:06:C1:43:A6:15:AE:BE:A5:92
         SHA1: 57:37:D1:E1:16:2F:F6:FE:26:B9:87:88:D2:86:DA:66:7F:98:54:3C
*/
	final String CACERT1 =
"-----BEGIN CERTIFICATE-----\n" +
"MIIDwDCCA36gAwIBAgIBEDALBgcqhkjOOAQDBQAwgZAxCzAJBgNVBAYTAlVTMQswCQYDVQQIEwJD" +
"QTESMBAGA1UEBxMJUGFsbyBBbHRvMR0wGwYDVQQKExRTdW4gTWljcm9zeXN0ZW1zIEluYzEjMCEG" +
"A1UECxMaSmF2YSBTb2Z0d2FyZSBDb2RlIFNpZ25pbmcxHDAaBgNVBAMTE0pDRSBDb2RlIFNpZ25p" +
"bmcgQ0EwHhcNMDEwNDI1MDcwMDAwWhcNMjAwNDI1MDcwMDAwWjCBkDELMAkGA1UEBhMCVVMxCzAJ" +
"BgNVBAgTAkNBMRIwEAYDVQQHEwlQYWxvIEFsdG8xHTAbBgNVBAoTFFN1biBNaWNyb3N5c3RlbXMg" +
"SW5jMSMwIQYDVQQLExpKYXZhIFNvZnR3YXJlIENvZGUgU2lnbmluZzEcMBoGA1UEAxMTSkNFIENv" +
"ZGUgU2lnbmluZyBDQTCCAbcwggEsBgcqhkjOOAQBMIIBHwKBgQDrrzcEHspRHmldsPKP9rVJH8ak" +
"mQXXKb90t2r1Gdge5Bv4CgGamP9wq+JKVoZsU7P84ciBjDHwxPOwi+ZwBuz3aWjbg0xyKYkpNhdc" +
"O0oHoCACKkaXUR1wyAgYC84Mbpt29wXj5/vTYXnhYJokjQaVgzxRIOEwzzhXgqYacg3O0wIVAIQl" +
"ReG6ualiq3noWzC4iWsb/3t1AoGBAKvJdHt07+5CtWpTTTvdkAZyaJEPC6Qpdi5VO9WuTWVcfio6" +
"BKZnptBxqqXXt+LBcg2k0aoeklRMIAAJorAJQRkzALLDXK5C+LGLynyW2BB/N0Rbqsx4yNdydjdr" +
"QJmoVWb6qAMei0oRAmnLTLglBhygd9LJrNI96QoQ+nZwt/vcA4GEAAKBgC0JmFysuJzHmX7uIBkq" +
"NJD516urrt1rcpUNZvjvJ49Esu0oRMf+r7CmJ28AZ0WCWweoVlY70ilRYV5pOdcudHcSzxlK9S3I" +
"y3JhxE5v+kdDPxS7+rwYZijC2WaLei0vwmCSSxT+WD4hf2hivmxISfmgS16FnRkQ+RVFURtx1PcL" +
"o2YwZDARBglghkgBhvhCAQEEBAMCAAcwDwYDVR0TAQH/BAUwAwEB/zAfBgNVHSMEGDAWgBRl4vSG" +
"ydNO8JFOWKJq9dh4WprBpjAdBgNVHQ4EFgQUZeL0hsnTTvCRTliiavXYeFqawaYwCwYHKoZIzjgE" +
"AwUAAy8AMCwCFCr3zzyXXfl4tgjXQbTZDUVM5LScAhRFzXVpDiH6HdazKbLp9zMdM/38SQ==" +
"\n-----END CERTIFICATE-----";

/* SECOND CA CERTIFICATE TRUSTED TO SIGN JCE PROVIDERS
Owner: CN=JCE Code Signing CA, OU=IBM Code Signing, O=IBM Corporation, C=US
Issuer: CN=JCE Code Signing CA, OU=IBM Code Signing, O=IBM Corporation, C=US
Serial number: 40216811
Valid from: Wed Feb 04 13:45:53 PST 2004 until: Tue May 26 13:45:53 PDT 2020
Certificate fingerprints:
         MD5:  54:20:F8:E9:FA:65:28:DE:87:7A:60:74:3D:44:77:9E
         SHA1: 5F:DB:A9:62:8E:35:09:D0:DB:10:04:58:2D:62:11:B8:1A:AE:56:44
*/
	final String CACERT2 =
"-----BEGIN CERTIFICATE-----\n" +
"MIIDUTCCAw2gAwIBAgIEQCFoETALBgcqhkjOOAQDBQAwYDELMAkGA1UEBhMCVVMxGDAWBgNVBAoT" +
"D0lCTSBDb3Jwb3JhdGlvbjEZMBcGA1UECxMQSUJNIENvZGUgU2lnbmluZzEcMBoGA1UEAxMTSkNF" +
"IENvZGUgU2lnbmluZyBDQTAeFw0wNDAyMDQyMTQ1NTNaFw0yMDA1MjYyMDQ1NTNaMGAxCzAJBgNV" +
"BAYTAlVTMRgwFgYDVQQKEw9JQk0gQ29ycG9yYXRpb24xGTAXBgNVBAsTEElCTSBDb2RlIFNpZ25p" +
"bmcxHDAaBgNVBAMTE0pDRSBDb2RlIFNpZ25pbmcgQ0EwggG4MIIBLAYHKoZIzjgEATCCAR8CgYEA" +
"/X9TgR11EilS30qcLuzk5/YRt1I870QAwx4/gLZRJmlFXUAiUftZPY1Y+r/F9bow9subVWzXgTuA" +
"HTRv8mZgt2uZUKWkn5/oBHsQIsJPu6nX/rfGG/g7V+fGqKYVDwT7g/bTxR7DAjVUE1oWkTL2dfOu" +
"K2HXKu/yIgMZndFIAccCFQCXYFCPFSMLzLKSuYKi64QL8Fgc9QKBgQD34aCF1ps93su8q1w2uFe5" +
"eZSvu/o66oL5V0wLPQeCZ1FZV4661FlP5nEHEIGAtEkWcSPoTCgWE7fPCTKMyKbhPBZ6i1R8jSjg" +
"o64eK7OmdZFuo38L+iE1YvH7YnoBJDvMpPG+qFGQiaiD3+Fa5Z8GkotmXoB7VSVkAUw7/s9JKgOB" +
"hQACgYEA6msAx98QO7l0NafhbWaCTfdbVnHCJkUncj1REGL/s9wQyftRE9Sti6glbl3JeNJbJ9MT" +
"QUcUBnzLgjhexgthoEyDLZTMjC6EkDqPQgppUtN0JnekFH0qcUGIiXemLWKaoViYbWzPzqjqut3o" +
"oRBEjIRCwbgfK7S8s110YICNQlSjUzBRMB0GA1UdDgQWBBR+PU1NzBBZuvmuQj3lyVdaUgt+hzAf" +
"BgNVHSMEGDAWgBR+PU1NzBBZuvmuQj3lyVdaUgt+hzAPBgNVHRMBAf8EBTADAQH/MAsGByqGSM44" +
"BAMFAAMxADAuAhUAi5ncRzk0NqFYt4yWsnlcVBPt+zsCFQCM9M0mv0t9iodsOOHJhqUrW1QjAA==" +
"\n-----END CERTIFICATE-----";

/* RSA CERTIFICATE USED TO SIGN ALL FOUR JCE JAR FILES
Owner: CN=Sun Microsystems Inc, OU=Java Software Code Signing, 
O=Sun Microsystems Inc
Issuer: CN=JCE Code Signing CA, OU=Java Software Code Signing, 
O=Sun Microsystems Inc, L=Palo Alto, ST=CA, C=US
Serial number: 15d
Valid from: Fri Oct 25 12:05:10 PDT 2002 until: Mon Oct 29 11:05:10 PST 2007
Certificate fingerprints:
         MD5:  63:AA:AA:43:78:9F:86:BC:AA:78:40:36:2D:6E:66:6D
         SHA1: 9D:F7:A6:11:C5:C1:F5:5D:61:01:F2:1D:A2:22:D7:58:79:6A:52:23
*/
	final String JCECERT =
"-----BEGIN CERTIFICATE-----\n" +
"MIICnjCCAlugAwIBAgICAV0wCwYHKoZIzjgEAwUAMIGQMQswCQYDVQQGEwJVUzELMAkGA1UECBMC" +
"Q0ExEjAQBgNVBAcTCVBhbG8gQWx0bzEdMBsGA1UEChMUU3VuIE1pY3Jvc3lzdGVtcyBJbmMxIzAh" +
"BgNVBAsTGkphdmEgU29mdHdhcmUgQ29kZSBTaWduaW5nMRwwGgYDVQQDExNKQ0UgQ29kZSBTaWdu" +
"aW5nIENBMB4XDTAyMTAyNTE5MDUxMFoXDTA3MTAyOTE5MDUxMFowYzEdMBsGA1UEChMUU3VuIE1p" +
"Y3Jvc3lzdGVtcyBJbmMxIzAhBgNVBAsTGkphdmEgU29mdHdhcmUgQ29kZSBTaWduaW5nMR0wGwYD" +
"VQQDExRTdW4gTWljcm9zeXN0ZW1zIEluYzCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA16bK" +
"o6tC3OHFDNfPXLKXMCMtIyeubNnsEtlvrH34HhfF+ZmpSliLCvQ15ms705vy4XgZUbZ3mgSOlLRM" +
"AGRo6596ePhc+0Z6yeKhbb3LZ8iz97ZIptkHGOshj9cfcSRPYmorUug9OsybMdIfQXazxT9mZJ9Y" +
"x5IDw6xak7kVbpUCAwEAAaOBiDCBhTARBglghkgBhvhCAQEEBAMCBBAwDgYDVR0PAQH/BAQDAgXg" +
"MB0GA1UdDgQWBBRI319jCbhc9DWJVltXgfrMybHNjzAfBgNVHSMEGDAWgBRl4vSGydNO8JFOWKJq" +
"9dh4WprBpjAgBgNVHREEGTAXgRV5dS1jaGluZy5wZW5nQHN1bi5jb20wCwYHKoZIzjgEAwUAAzAA" +
"MC0CFFmXXV97KWezNwPFiAJt5IWpPGVqAhUAggVpunP/Bo8BOcwIoUpgYYQ7ruY=" +
"\n-----END CERTIFICATE-----";

	try {
	    AccessController.doPrivileged(new PrivilegedExceptionAction() {
	        public Object run() throws Exception {
		    certificateFactory = CertificateFactory.getInstance("X.509");
		    jceCertificate = parseCertificate(JCECERT);
		    X509Certificate[] providerCaCerts = new X509Certificate[] {
			parseCertificate(CACERT1),
			parseCertificate(CACERT2),
			// as an optimization, add our EE certs to the trusted
			// certs. this saves us the signature verification
			jceCertificate,
		    };
		    providerValidator = Validator.getInstance(
		    				Validator.TYPE_SIMPLE,
						Validator.VAR_JCE_SIGNING, 
						Arrays.asList(providerCaCerts));
		    // Trust the JCE provider CAs for exempt JCE applications.
		    // If this changes, setup the validator for exempt apps here
		    exemptValidator = null;
		    testSignatures(providerCaCerts[0]);
		    setupJurisdictionPolicies();
		    certificateFactory = null;
		    return null;
		}
	    });

	    isRestricted = defaultPolicy.implies(CryptoAllPermission.INSTANCE)? 
	                false : true;
	} catch (Exception e) {
	    SecurityException se = new 
		SecurityException("Cannot set up certs for trusted CAs");
	    se.initCause(e);
	    throw se;
	    
	}
    }
    
    /**
     * Parse a PEM encoded X.509 certificate.
     */
    private static X509Certificate parseCertificate(String s) throws Exception {
	InputStream in = new ByteArrayInputStream(s.getBytes("UTF8"));
	return (X509Certificate)certificateFactory.generateCertificate(in);
    }
    
    static Instance getInstance(String type, Class clazz, String algorithm, 
	    String provider) throws NoSuchAlgorithmException, 
	    NoSuchProviderException {
	Service s = GetInstance.getService(type, algorithm, provider);
	Exception ve = getVerificationResult(s.getProvider());
	if (ve != null) {
	    String msg = "JCE cannot authenticate the provider " + provider;
	    throw (NoSuchProviderException)
	    			new NoSuchProviderException(msg).initCause(ve);
	}
	return GetInstance.getInstance(s, clazz);
    }

    static Instance getInstance(String type, Class clazz, String algorithm, 
	    Provider provider) throws NoSuchAlgorithmException {
	Service s = GetInstance.getService(type, algorithm, provider);
	Exception ve = JceSecurity.getVerificationResult(provider);
	if (ve != null) {
	    String msg = "JCE cannot authenticate the provider "
	    	+ provider.getName();
	    throw new SecurityException(msg, ve);
	}
	return GetInstance.getInstance(s, clazz);
    }
    
    static Instance getInstance(String type, Class clazz, String algorithm) 
	    throws NoSuchAlgorithmException {
	List services = GetInstance.getServices(type, algorithm);
	NoSuchAlgorithmException failure = null;
	for (Iterator t = services.iterator(); t.hasNext(); ) {
	    Service s = (Service)t.next();
	    if (canUseProvider(s.getProvider()) == false) {
		// allow only signed providers
		continue;
	    }
	    try {
		Instance instance = GetInstance.getInstance(s, clazz);
		return instance;
	    } catch (NoSuchAlgorithmException e) {
		failure = e;
	    }
	}
	throw new NoSuchAlgorithmException("Algorithm " + algorithm
		+ " not available", failure);
    }

    /**
     * Verify if the JAR at URL codeBase is a signed exempt application
     * JAR file.
     *
     * @return the JarFile on success
     * @throws Exception on error
     */
    static JarFile verifyExemptJar(URL codeBase) throws Exception {
	if (exemptValidator != null) {
	    try {
		JarVerifier jv = new JarVerifier(codeBase, exemptValidator);
		jv.verify();
		return jv.getJarFile();
	    } catch (Exception e) {
		// ignore, also try provider CA certs
	    }
	}
	return verifyProviderJar(codeBase);
    }
    
    /**
     * Verify if the JAR at URL codeBase is a signed provider JAR file.
     *
     * @return the JarFile on success
     * @throws Exception on error
     */
    static JarFile verifyProviderJar(URL codeBase) throws Exception {
	// Verify the provider JAR file and all
	// supporting JAR files if there are any.
	JarVerifier jv = new JarVerifier(codeBase, providerValidator);
	jv.verify();
	return jv.getJarFile();
    }
    
    private final static Object PROVIDER_VERIFIED = Boolean.TRUE;
    
    /*
     * Verify that the provider JAR files are signed properly, which
     * means the signer's certificate can be traced back to a 
     * JCE trusted CA.
     * Return null if ok, failure Exception if verification failed.
     */
    static synchronized Exception getVerificationResult(Provider p) {
	Object o = verificationResults.get(p);
	if (o == PROVIDER_VERIFIED) {
	    return null;
	} else if (o != null) {
	    return (Exception)o;
	}
	if (verifyingProviders.get(p) != null) {
	    // this method is static synchronized, must be recursion
	    // return failure now but do not save the result
	    return new NoSuchProviderException("Recursion during verification");
	}
	try {
	    verifyingProviders.put(p, Boolean.FALSE);
	    URL providerURL = getCodeBase(p.getClass());
	    verifyProviderJar(providerURL);
	    // Verified ok, cache result
	    verificationResults.put(p, PROVIDER_VERIFIED);
	    return null;
	} catch (Exception e) {
	    verificationResults.put(p, e);
	    return e;
	} finally {
	    verifyingProviders.remove(p);
	}
    }
    
    // return whether this provider is properly signed and can be used by JCE
    static boolean canUseProvider(Provider p) {
	return getVerificationResult(p) == null;
    }
    
    // dummy object to represent null
    private static final URL NULL_URL;
    
    static {
	try {
	    NULL_URL = new URL("http://null.sun.com/");
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }
    
    // reference to a Map we use as a cache for codebases
    private static final Map codeBaseCacheRef = new WeakHashMap();
    
    /*
     * Retuns the CodeBase for the given class.
     */
    static URL getCodeBase(final Class clazz) {
	URL url = (URL)codeBaseCacheRef.get(clazz);
	if (url == null) {
            url = (URL)AccessController.doPrivileged(new PrivilegedAction() {
		public Object run() {
		    ProtectionDomain pd = clazz.getProtectionDomain();
		    if (pd != null) {
			CodeSource cs = pd.getCodeSource();
			if (cs != null) {
			    return cs.getLocation();
			}
		    }
		    return NULL_URL;
		}
	    });
	    codeBaseCacheRef.put(clazz, url);
	}
	return (url == NULL_URL) ? null : url;
    }

    private static void setupJurisdictionPolicies() throws Exception {
	String javaHomeDir = System.getProperty("java.home");
       	String sep = File.separator;
	String pathToPolicyJar = javaHomeDir + sep + "lib" + sep +
            "security" + sep;

	File exportJar = new File(pathToPolicyJar, "US_export_policy.jar");
	File importJar = new File(pathToPolicyJar, "local_policy.jar");
        URL jceCipherURL = ClassLoader.getSystemResource
		("javax/crypto/Cipher.class");

	if ((jceCipherURL == null) || !exportJar.exists() || !importJar.exists()) {
	    throw new SecurityException
	    			("Cannot locate policy or framework files!");
	}
	
	URL exportPolicyURL = exportJar.toURI().toURL();
        URL importPolicyURL = importJar.toURI().toURL();
        
        if (debug) {
	    System.out.println("export policy at:" + exportPolicyURL);
	    System.out.println("import policy at:" + importPolicyURL);
	    System.out.println("jce Cipher class at:" + jceCipherURL);
        }
	
	// Enforce the signer restraint, i.e. signer of JCE framework
	// jar should also be the signer of the two jurisdiction policy
	// jar files.
	List signers = JarVerifier.getSignersOfJarEntry(jceCipherURL);
	for (Iterator t = signers.iterator(); t.hasNext(); ) {
	    X509Certificate[] chain = (X509Certificate[])t.next();
	    if (chain[0].equals(jceCertificate)) {
		// use signers as a success flag
		signers = null;
		break;
	    }
	}
	if (signers != null) {
	    throw new SecurityException("Jurisdiction policy files are " +
					"not signed by trusted signers!");
	}

	// Read jurisdiction policies.
	CryptoPermissions defaultExport = new CryptoPermissions();
	CryptoPermissions exemptExport = new CryptoPermissions();
	loadPolicies(exportJar, defaultExport, exemptExport, jceCertificate);

	CryptoPermissions defaultImport = new CryptoPermissions();
	CryptoPermissions exemptImport = new CryptoPermissions();
	loadPolicies(importJar, defaultImport, exemptImport, jceCertificate);

	// Merge the export and import policies for default applications.
	if (defaultExport.isEmpty() || defaultImport.isEmpty()) {
	    throw new SecurityException("Missing mandatory jurisdiction " +
					"policy files");
	}
	defaultPolicy = defaultExport.getMinimum(defaultImport);

	// Merge the export and import policies for exempt applications.
	if (exemptExport.isEmpty())  {
	    exemptPolicy = exemptImport.isEmpty() ? null : exemptImport;
	} else {
	    exemptPolicy = exemptExport.getMinimum(exemptImport);
	}
    }
    
    /**
     * Load the policies from the specified file. Also checks that the policies
     * are signed by the certificate <code>signer</code>.
     */
    private static void loadPolicies(File jarPathName,
				     CryptoPermissions defaultPolicy,
				     CryptoPermissions exemptPolicy,
				     X509Certificate signer)
	throws Exception {

	JarFile jf = new JarFile(jarPathName);

	Enumeration entries = jf.entries();
	while (entries.hasMoreElements()) {
	    JarEntry je = (JarEntry)entries.nextElement();
	    InputStream is = null;
	    try {
	        if (je.getName().startsWith("default_")) {
		    is = jf.getInputStream(je);
		    defaultPolicy.load(is);
	        } else if (je.getName().startsWith("exempt_")) {
		    is = jf.getInputStream(je);
		    exemptPolicy.load(is);
	        } else {
		    continue;
		}
	    } finally {
		if (is != null) {
		    is.close();
		}
	    }
	    Certificate[] certChains = je.getCertificates();
	    List paths = JarVerifier.convertCertsToChains(certChains);
	    boolean found = false;
	    for (Iterator t = paths.iterator(); t.hasNext(); ) {
		X509Certificate[] path = (X509Certificate[])t.next();
		X509Certificate cert = path[0];
		if (cert.equals(signer)) {
		    found = true;
		    break;
		}
	    }
	    if (found == false) {
		throw new SecurityException("Jurisdiction policy files are " +
					    "not signed by trusted signers!");
	    }
	}
	jf = null;
    }

    static CryptoPermissions getDefaultPolicy() {
	return defaultPolicy;
    }

    static CryptoPermissions getExemptPolicy() {
	return exemptPolicy;
    }

    static boolean isRestricted() {
	return isRestricted;
    }

    /**
     * Retrieve some system information, hashed.
     */
    private static byte[] getSystemEntropy() {
	final MessageDigest md;

	try {
    	    md = MessageDigest.getInstance("SHA");
    	} catch (NoSuchAlgorithmException nsae) {
    	    throw new InternalError("internal error: SHA-1 not available.");
	}

	// The current time in millis
	byte b = (byte)System.currentTimeMillis();
	md.update(b);

	try {
	    // System properties can change from machine to machine
	    String s;
	    Properties p = System.getProperties();
	    Enumeration e = p.propertyNames();
	    while (e.hasMoreElements()) {
		s =(String)e.nextElement();
		md.update(s.getBytes());
		md.update(p.getProperty(s).getBytes());
	    }
	    
	    md.update
		(InetAddress.getLocalHost().toString().getBytes());
	    
	    // The temporary dir
	    File f = new File(p.getProperty("java.io.tmpdir"));
	    String[] sa = f.list();
	    for(int i = 0; i < sa.length; i++)
		md.update(sa[i].getBytes());
	    
	} catch (Exception ex) {
	    md.update((byte)ex.hashCode());
	}
	
	// get Runtime memory stats
	Runtime rt = Runtime.getRuntime();
	byte[] memBytes = longToByteArray(rt.totalMemory());
	md.update(memBytes, 0, memBytes.length);
	memBytes = longToByteArray(rt.freeMemory());
	md.update(memBytes, 0, memBytes.length);
	
	return md.digest();
    }

    /**
     * Helper function to convert a long into a byte array (least significant
     * byte first).
     */
    private static byte[] longToByteArray(long l) {
	byte[] retVal = new byte[8];
 
	for (int i=0; i<8; i++) {
	    retVal[i] = (byte) l;
	    l >>= 8;
	}
 
	return retVal;
    }

    private static void testSignatures(X509Certificate providerCert)
	    throws Exception {
/* DSA CERTIFICATE
Owner: CN=JCE Development, OU=Java Software, O=Sun Microsystems, L=Cupertino, 
ST=CA, C=US
Issuer: CN=JCE Development, OU=Java Software, O=Sun Microsystems, L=Cupertino, 
ST=CA, C=US
Serial number: 37f939e5
Valid from: Mon Oct 04 16:36:05 PDT 1999 until: Tue Oct 03 16:36:05 PDT 2000
Certificate fingerprints:
         MD5:  F2:F9:D1:18:F3:CC:E5:67:39:48:2C:15:98:03:ED:28
         SHA1: 28:E5:32:F3:FD:AF:8E:01:97:66:47:61:92:90:F9:8D:1B:E4:59:D5
*/
	String DSA =
"-----BEGIN CERTIFICATE-----\n" +
"MIIDLDCCAukCBDf5OeUwCwYHKoZIzjgEAwUAMHsxCzAJBgNVBAYTAlVTMQswCQYDVQQIEwJDQTES" +
"MBAGA1UEBxMJQ3VwZXJ0aW5vMRkwFwYDVQQKExBTdW4gTWljcm9zeXN0ZW1zMRYwFAYDVQQLEw1K" +
"YXZhIFNvZnR3YXJlMRgwFgYDVQQDEw9KQ0UgRGV2ZWxvcG1lbnQwHhcNOTkxMDA0MjMzNjA1WhcN" +
"MDAxMDAzMjMzNjA1WjB7MQswCQYDVQQGEwJVUzELMAkGA1UECBMCQ0ExEjAQBgNVBAcTCUN1cGVy" +
"dGlubzEZMBcGA1UEChMQU3VuIE1pY3Jvc3lzdGVtczEWMBQGA1UECxMNSmF2YSBTb2Z0d2FyZTEY" +
"MBYGA1UEAxMPSkNFIERldmVsb3BtZW50MIIBuDCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIp" +
"Ut9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdr" +
"mVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iID" +
"GZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC" +
"+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWR" +
"bqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYUAAoGBAOGs" +
"R8waR5aiuOk1yBLemRlVCY+APJv3xqmPRxWAF6nwV2xrFUB8ghSEMFcHywoe4vBDvkGSoAFzeB5j" +
"y5wjDiFsN5AFPEVRfveS4NNZ1dgRdHbbh3h5O1dZE4MAKQwQfUoh9Oa3aahlB+orRzKOHLlGDpbN" +
"RQLST5BClvohramCMAsGByqGSM44BAMFAAMwADAtAhRF46T3nS+inP9TA1pLd3LIV0NNDQIVAIaf" +
"i+1/+JKxu0rcoXWMFSxNaRb3\n" +
"-----END CERTIFICATE-----";

/* SELF SIGNED RSA CERTIFICATE 1
Owner: CN=JCE Development, OU=Java Software, O=Sun Microsystems, L=Cupertino, 
ST=CA, C=US
Issuer: CN=JCE Development, OU=Java Software, O=Sun Microsystems, L=Cupertino, 
ST=CA, C=US
Serial number: 1
Valid from: Thu Oct 31 15:27:44 GMT 2002 until: Wed Oct 31 15:27:44 GMT 2007
Certificate fingerprints:
         MD5:  62:D2:99:B7:5C:20:A7:9D:B1:4A:64:06:8D:31:B8:70
         SHA1: 53:B9:A1:1F:D5:9F:53:27:99:5D:6A:DF:E0:D3:59:9B:67:8B:5C:7F
*/
	final String RSA1 = 
"-----BEGIN CERTIFICATE-----\n" +
"MIIB4DCCAYoCAQEwDQYJKoZIhvcNAQEEBQAwezELMAkGA1UEBhMCVVMxCzAJBgNVBAgTAkNBMRIw" +
"EAYDVQQHEwlDdXBlcnRpbm8xGTAXBgNVBAoTEFN1biBNaWNyb3N5c3RlbXMxFjAUBgNVBAsTDUph" +
"dmEgU29mdHdhcmUxGDAWBgNVBAMTD0pDRSBEZXZlbG9wbWVudDAeFw0wMjEwMzExNTI3NDRaFw0w" +
"NzEwMzExNTI3NDRaMHsxCzAJBgNVBAYTAlVTMQswCQYDVQQIEwJDQTESMBAGA1UEBxMJQ3VwZXJ0" +
"aW5vMRkwFwYDVQQKExBTdW4gTWljcm9zeXN0ZW1zMRYwFAYDVQQLEw1KYXZhIFNvZnR3YXJlMRgw" +
"FgYDVQQDEw9KQ0UgRGV2ZWxvcG1lbnQwXDANBgkqhkiG9w0BAQEFAANLADBIAkEAo/4CddEOa3M6" +
"v9JFAhnBYgTq54Y30++F8yzCK9EeYaG3AzvzZqNshDy579647p0cOM/4VO6rU2PgbzgKXPcs8wID" +
"AQABMA0GCSqGSIb3DQEBBAUAA0EACqPlFmVdKdYSCTNltXKQnBqss9GNjbnB+CitvWrwN+oOK8qQ" +
"pvV+5LB6LruvRy6zCedCV95Z2kXKg/Fnj0gvsg==\n" +
"-----END CERTIFICATE-----";

/* SELF SIGNED RSA CERTIFICATE 2
Owner: CN=JCE Development, OU=Java Software, O=Sun Microsystems, L=Cupertino, 
ST=CA, C=US
Issuer: CN=JCE Development, OU=Java Software, O=Sun Microsystems, L=Cupertino,
ST=CA, C=US
Serial number: 2
Valid from: Thu Oct 31 15:27:44 GMT 2002 until: Wed Oct 31 15:27:44 GMT 2007
Certificate fingerprints:
         MD5:  A5:18:04:0E:29:DB:2B:24:19:FE:0F:12:E8:0C:A7:CC
         SHA1: A9:3B:F5:AE:AC:92:5B:0B:EC:7E:FD:E6:59:28:07:F2:2C:2C:8A:2D
*/	 
	final String RSA2 = 
"-----BEGIN CERTIFICATE-----\n" +
"MIIB4DCCAYoCAQIwDQYJKoZIhvcNAQEEBQAwezELMAkGA1UEBhMCVVMxCzAJBgNVBAgTAkNBMRIw" +
"EAYDVQQHEwlDdXBlcnRpbm8xGTAXBgNVBAoTEFN1biBNaWNyb3N5c3RlbXMxFjAUBgNVBAsTDUph" +
"dmEgU29mdHdhcmUxGDAWBgNVBAMTD0pDRSBEZXZlbG9wbWVudDAeFw0wMjEwMzExNTI3NDRaFw0w" +
"NzEwMzExNTI3NDRaMHsxCzAJBgNVBAYTAlVTMQswCQYDVQQIEwJDQTESMBAGA1UEBxMJQ3VwZXJ0" +
"aW5vMRkwFwYDVQQKExBTdW4gTWljcm9zeXN0ZW1zMRYwFAYDVQQLEw1KYXZhIFNvZnR3YXJlMRgw" +
"FgYDVQQDEw9KQ0UgRGV2ZWxvcG1lbnQwXDANBgkqhkiG9w0BAQEFAANLADBIAkEAr1OSXaOzpnVo" +
"qL2LqS5+HLy1kVvBwiM/E5iYT9eZaghE8qvF+4fETipWUNTWCQzHR4cDJGJOl9Nm77tELhES4QID" +
"AQABMA0GCSqGSIb3DQEBBAUAA0EAL+WcVFyj+iXlEVNVQbNOOUlWmlmXGiNKKXnIdNcc1ZUyi+JW" +
"0zmlfZ7iU/eRYhEEJBwdrUoyiGOGLo7pi6JzAA==\n" +
"-----END CERTIFICATE-----";
	 
	final int NUM_TESTS = 12;
	byte[] randomBytes = getSystemEntropy(); 
	int random = (randomBytes[0] & 0xff)
	           | (randomBytes[1] & 0xff) << 8
	           | (randomBytes[2] & 0xff) << 16
	           |  randomBytes[3]         << 24;

	X509Certificate[] certs = new X509Certificate[] {
	    providerCert,
	    parseCertificate(DSA),
	    parseCertificate(RSA1),
	    parseCertificate(RSA2),
	};
	
	PublicKey[] publicKeys = new PublicKey[4];
	publicKeys[0] = providerCert.getPublicKey();
	publicKeys[1] = publicKeys[0];
	publicKeys[2] = certs[2].getPublicKey();
	publicKeys[3] = publicKeys[2];
	
	boolean[] expectedResult = new boolean[] {
	    true,
	    false,
	    true,
	    false,
	};

	for (int i = 0; i < NUM_TESTS; i++) {
	    int k = random & 3;
	    random >>= 2;
	    boolean result;
	    try {
		certs[k].verify(publicKeys[k]);
		result = true;
	    } catch (SignatureException e) {
		result = false;
	    }
	    if (result != expectedResult[k]) {
		throw new SecurityException("Signature classes have " +
					    "been tampered with");
	    }
	}
    }
}
