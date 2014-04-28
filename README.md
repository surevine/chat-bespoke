Chat Bespoke
============
This is a web based XMPP chat client with basic security labelling within the XEP-0258 spec (using hardcoded security labels rather than discovering the catalog). It also includes CAS based authentication and a number of specific fixes.

It's likely to only be of use to Surevine and it's customers - the more generic chat application (which this project depends on) is here:

https://github.com/surevine/chat

Building
--------
To build the chat bespoke package run the following:

	mvn clean package
	
Installation
------------
This is bespoke software with a number of complex external dependencies, and so you should have been supplied separate installation instructions specific to your environment.
