              Java(TM) Cryptography Extension for the
  Java(TM) 2 Platform, Standard Edition Development Kit, v5.0
                         Source Release


----------------------------------------------------------------------
INTRODUCTION
----------------------------------------------------------------------
Thank you for downloading the source distribution for the Java(TM)
Cryptography Extension (JCE). This source code was used to generate
the JCE-related jar files provided in Sun Microsystems' Java 2 
Platform, Standard Edition Development Kit, v 5.0 distribution.

----------------------------------------------------------------------
UNDERSTANDING THE IMPORT/EXPORT ISSUES
----------------------------------------------------------------------
While there has been some relaxation in the US export requirements,
there are many restrictions still in place for strong encryption and
pluggable products. 

Due to the import control restrictions of some countries, the JCE
jurisdiction policy files shipped with the JDK(TM) 5.0 allow "strong"
but limited cryptography to be used. An "unlimited strength" version 
of these files -- indicating no restrictions on cryptographic 
strengths -- is available for those living in eligible countries.
(Most countries are eligible.)

If you develop an implementation based on this source code it will
require US export approval. You are advised to consult your 
export/import control counsel or attorney to determine the exact 
requirements.

----------------------------------------------------------------------
PREPARATION FOR BUILDING THE RELEASE
----------------------------------------------------------------------
To comply with export regulations, JCE jar files must be signed and
the signer must be trusted by JCE. In addition, the JCE jurisdiction 
policy files must be signed by the same signer who signed the JCE 
framework jar file.

Therefore, JCE performs signer checks at runtime before it can be 
used. The following requirements must be met in order to pass JCE's
signer check:
1. JCE framework and the two jurisdiction policy files must be 
signed by the same signer whose certificate matches the embedded 
certificate in javax.crypto.JceSecurity class.
2. SunJCE provider must be signed by a signer whose certificate 
matches the embedded certificate in com.sun.crypto.provider.SunJCE 
class.

Due to security reasons, the private key that Sun used to sign JCE
is not included in this source distribution. Thus, to build your own 
version of JCE, you will need to go through the following steps:
1. Generate a key pair (public/private key).
2. Decide on a Certification Authority (CA) and apply for an X.509 
   certificate from your CA for the public key generated in step #1.
3. Replace the embedded certificate, e.g. specified under variable 
   "JCECERT", in the following JCE source files:
        o src/share/classes/javax/crypto/JceSecurity.java
        o src/share/classes/com/sun/crypto/provider/SunJCE.java
   with the public key certificate issued by your CA (in step #2).
4. Use the key pair generated in step #1 to sign the JCE jar files.

----------------------------------------------------------------------
BUILDING THE RELEASE
----------------------------------------------------------------------
You should be able to compile the entire source tree using javac. 
Note: this source code depends heavily on the sun.* hierarchy for 
many of its support classes since it's considered part of JDK 5.0.

        Here is a list of components included in this source 
        distribution and their associated directories:

        o JCE framework:
                src/share/classes/javax/crypto/interfaces/
                src/share/classes/javax/crypto/spec/
                src/share/classes/javax/crypto/
        
        o SunJCE provider:
                src/share/classes/com/sun/crypto/provider/
        
        o Strong jurisdiction policy files:
          (no compilation required)
                src/policy/withlimits/local/
                src/policy/withlimits/US_export/
        
        o Unlimited jurisdiction policy files:
          (no compilation required)
                src/policy/nolimits/local/
                src/policy/nolimits/US_export/

For export reasons, Sun Microsystems' JCE implementation has been 
obfuscated. There are a number of benefits to obfuscating the 
library. If you choose to obfuscate, there are many obfuscation 
tools available. Sun uses the DashoPro 2.3 (Build 12) tool from 
preEmptive Solutions (www.preemptive.com). If you also choose to 
use DashoPro, be sure to use at least this release or later.

----------------------------------------------------------------------
PACKAGING THE RELEASE
----------------------------------------------------------------------
After the source files are compiled, the related files for each 
component must be packaged inside a jar file and signed with your
key pair using jarsigner or other code signing tool.

Here are detailed instructions on how to package the jar files for 
each component (in our examples, all the generated class files are
in the directory "build"):

        o JCE framework:
          jce.jar
          =======
          cd build
          jar -cvf jce.jar javax
          jarsigner -keystore <your keystore> jce.jar <your alias>
        
        o SunJCE provider: 
          sunjce_provider.jar
          ===================
          cd build
          jar -cvf sunjce_provider.jar com
          jarsigner -keystore <your keystore> sunjce_provider.jar
                  <your alias>
        
        o Strong jurisdiction policy files:
          local_policy.jar 
          ================
          cd src/policy/withlimits/local/
          jar -cmf ../../../LIMITED local_policy.jar *.policy
          jarsigner -keystore <your keystore> local_policy.jar 
                  <your alias>
                        
          US_export_policy.jar
          ====================
          cd src/policy/withlimits/US_export/
          jar -cmf ../../../LIMITED US_export_policy.jar *.policy
          jarsigner -keystore <your keystore> US_export_policy.jar
                  <your alias>
                        
        o Unlimited jurisdiction policy files:
          local_policy.jar
          ================
          cd src/policy/nolimits/local/
          jar -cmf ../../../UNLIMITED local_policy.jar *.policy
          jarsigner -keystore <your keystore> local_policy.jar 
                  <your alias>
                        
          US_export_policy.jar
          ====================
          cd src/policy/nolimits/US_export/
          jar -cmf ../../../UNLIMITED US_export_policy.jar *.policy
          jarsigner -keystore <your keystore> US_export_policy.jar 
                  <your alias>

For more information on packaging, please refer to the following 
Web pages:
        http://java.sun.com/j2se/1.5.0/docs/guide/jar/index.html
        http://java.sun.com/docs/books/tutorial/jar/
        http://java.sun.com/j2se/1.5.0/runtime.html#jar
