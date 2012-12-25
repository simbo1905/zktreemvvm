
#### ZK MVVM Load On Demand TreeModel

Build and run the code with 

	mvn -Djetty.port=8080 package jetty:run

The code uses Apache Commons VFS (Virtual File System) which is an interface 
abstraction over a file system. This example shows the following JAR file system

	jar:http://repo1.maven.org/maven2/org/apache/commons/commons-vfs2/2.0/commons-vfs2-2.0.jar

but it would be trivial to change the URI used to be a file uri such as: 

	file:///tmp/

See the [commons vfs filesystems](http://commons.apache.org/vfs/filesystems.html) documentation 
for more information. 
 

