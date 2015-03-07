# akka-cluster-load-balancing
Example producer consumer(s) application that shows how Akka routing can be used to balance the load of the nodes in an Akka Cluster.

Description available at [blog.kamkor.me](http://blog.kamkor.me/Akka-Cluster-Load-Balancing/)

The easiest way to run the example application is to download [akka-cluster-load-balancing-assembly-1.0.zip](https://github.com/kamkor/akka-cluster-load-balancing/releases/tag/master). It contains windows scripts that can run the application with the same setup as in this post. Customize the scripts to change:

* node hostname and port;
* seed node(s);
* producer message send interval;
* consumer message processing time;
* memory settings of node;
* metrics log interval (-Dproducer.metrics-interval-seconds);
* message size (-Dproducer.data-array-size), if message size is too big, make sure to also update akka maximum-frame-size, receive-buffer-size, send-buffer-size.

Alternatively fork the project, perhaps modify it and use sbt assembly command to build the jar. Then use included windows scripts to execute that jar. The scripts assume that the jar and the sigar directory are present in their directory.
