# Java-Client
Java client for RESTFUL web service

## Location
http://modintro.com/java/

## Screenshot
![Screenshot](https://github.com/cspen/Java-Client/blob/master/jc-screenshot.png)

## What the Application Does
The application connects to a restful web service via
the Internet to perform CRUD operations on a database
table. 

The database table rows are displayed in a JTable. The
table can be updated, have new rows added, have rows
deleted, and sort by column. The entire application is
coded in Java Swing using JAXB to unmarshall the XML
returned from the web server.

## Related Project - HTML Client
An HTML/CSS/Javascript version of this application is
available at https://github.com/cspen/RESTful-App

## Security
Because of security the JAR file needed to be signed. However
I do not posses an official security certificate and therefore
had to self-sign the certificate.

I had to give the application all-permissions in order to use JAXB.
I didn't realize creatig new instances of JAXB required a higher security than
sandbox.

## Notes and Additional Thoughts
This application was easier to build in Java than HTML/Javascript.
Most of the work is already done for you by the JTable and various
Swing components.

JTable doesn't provide a way to intercept updates made to the model.
I implemented an Observer/Observable design pattern to allow for
input sanitation and validation via the server before modifying the
local data model.

I discovered when using Java Web Start that both jnlp file and
the JAR file manifest must have the same security setting or else
the application will throw an AccessControlException when using
JAXB.
