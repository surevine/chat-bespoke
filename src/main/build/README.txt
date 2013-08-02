#############################################################################################
#
# For any issues installing this software or configuring it for your environment
# please contact:
#
# Surevine and ask for Support on +44 (0) 845 468 1066 or e-mail us on support@surevine.com
#
#############################################################################################
  
#
# Chat Environment Installation
#

# LDAP

An LDAP should be configured to hold user details e.g. OpenLDAP. Both Openfire and CAS
are configured to work with the LDAP instance.

# Database

The Openfire server persists its configuration to a database. By default this uses an
embedded database (Java in memory) but can also use a standalone database such as 
Oracle, MySQL etc. To install Openfire to use a standalone database follow the 
instructions provided on the Openfire website.

# Apache

Apache is configured to handle the SSL authentication and also uses mod_jk to proxy 
calls directly to Tomcat. There is a single proxy pass directive that is used to route
XMPP calls to Openfire:

ProxyPass /proxy http://server:port/http-bind/

# Openfire

An Openfire XMPP server should be configured to route messages between clients. By default
the Openfire administration server runs on port 9090 so once installed Openfire can be
configured usig the administration console via a web browser at http://server:9090/. The
administration server will run through a sequence of screens that will detail the 
configuration of the Openfire server against LDAP and the backing database. Both configurations
can be tested within the administration console to ensure the configuation is correct before
proceeding.

# Tomcat

Tomcat is used as the Servlet container and runs both the Chat application and the CAS
single sign on service. Instructions on how to configure the CAS are found on the CAS website. 
To configure the Chat application a number of pre-installation steps need to made. These are 
detailed below.

#
# Installation guidelines for the Chat application.
#

A number of pre-installation tasks need to be run on the chat client Web ARchive (WAR)
file.

Firstly unzip the build zip file. This should extract into a directory called 'build'
and contain the WAR file, a build.properties file, a build.sh file and this README.txt
file.

> unzip <FILE NAME>.zip

Using a tool like dos2unix convert the files, build.properties and build.sh to unix.

> dos2unix build.properties
> dos2unix build.sh

Rename the WAR file to chat.war.

> mv <FILE NAME>.war chat.war

Edit the build.properties file to contain the correct values that will be substituted
into the WAR file at the necessary points during the script execution.

Execute the build.sh file. This will extract the necessary files from the WAR file and
undertake the value substitution from the build.properties file.

Once complete the WAR file can be deployed into Tomcat as usual.

#
# Transitional guidelines for the Password Expiry filter
#

The password expiry filter is now bundled with the chat war. 
However the location of the transitional properties file has changed.
It should now be in the chat.war WEB-INF/classes directory in: com/surevine/auth/passwordexpiry

Named passwordexpiry.properties

com.surevine.auth.passwordexpiry.admin.username=<read-only-ldap-user>
com.surevine.auth.passwordexpiry.admin.password=<password>
com.surevine.auth.passwordexpiry.ldap.basecontext=dc=test,dc=org,dc=uk
com.surevine.auth.passwordexpiry.ldap.url=ldap://localhost/
com.surevine.auth.passwordexpiry.passwordchange.url=https://<ip-address>/umt_changepassword  
