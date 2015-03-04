# akka-cluster-load-balancing
Example producer consumer(s) application that shows how akka routing can be used to balance the load of the nodes in akka cluster.

Description avaible at TODO

Scripts that start nodes assume that akka-cluster-load-balancing-assembly-1.0.jar is present in their directory. 

producer.cmd - starts producer node.
consumer.cmd - starts consumer node.
run_with_adaptive.cmd - starts 1 producer node and 4 consumer nodes. Routes messages to consumers via adaptive load balancing router.
run_with_round_robin.cmd - starts 1 producer node and 4 consumer nodes. Routes messages to consumers via round robin router. Ends quickly with out of memory error on slowest consumer node. 
