# turbo-octo-adventure
Show how to implement JCE without signing

When using the **actual** Oracle JDK, we can't implement certain parts of the JCE without sending the
Provider classes to be signed by them/Sun/IBM.

Unfortunately, for development and testing, this feedback loop is too slow. This is a simple POC
for jdk1.8.0-311-x32 that shows how to sidestep this restriction using reflection and the Unsafe
classes. It is *highly* likely that this will stop working someday, as the Unsafe classes will
be removed.

Just use the OpenJDk folks! Unless you can't because of work, in which case use this for dev,
and then get Oracle/Sun/IBM to sign the production jars
