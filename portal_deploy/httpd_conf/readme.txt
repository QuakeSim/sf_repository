This directory contains configuration files for configure httpd on portal servers to work on port 80 and redirect http requests to QuakeSim portal server running on port 8080. These files are copied from configuration on gf13.ucs.indiana.edu. Use them as templates and apply modifications to fit your server configuration.

File included in this directory: a sample httpd.conf, a sample mod_jk.conf,
and a sample workers.properties.

Before apply these cofigurations, you need to install the mod_jk module to the
module directory of the httpd on your server. Go to
http://tomcat.apache.org/connectors-doc/webserver_howto/apache.html for
details.
