**wcs.crt** contains the certificate for secure access to the water-collection-service.

To import the certificate into a client truststore, execute the following command:

```keytool -importcert -file wcs.crt -alias wcs -keystore $JDK_HOME/jre/lib/security/cacerts```