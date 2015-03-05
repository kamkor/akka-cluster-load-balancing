package kamkor.actor

import scala.collection.immutable.HashSet
import scala.concurrent.duration.DurationInt
import akka.actor.{ Actor, Props }
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.metrics.{ ClusterMetricsChanged, ClusterMetricsExtension, NodeMetrics }
import akka.cluster.metrics.StandardMetrics.HeapMemory
import kamkor.{ ConsumerApp }
import kamkor.metrics.{ ClusterHeapMetrics, MetricsLogger }

class ClusterListener(metricsIntervalSeconds: Int) extends Actor {

  import context.dispatcher
  context.system.scheduler.schedule(
    metricsIntervalSeconds.seconds, metricsIntervalSeconds.seconds, self, "logConsumersHeapUse")

  private[this] val cluster = Cluster(context.system)
  private[this] val metricsLogger =
    new MetricsLogger(name = cluster.selfAddress.port.getOrElse(0).toString())
  private[this] val clusterHeapMetrics = new ClusterHeapMetrics()

  private var consumers: Set[String] = HashSet.empty

  override def preStart(): Unit = {
    ClusterMetricsExtension(context.system).subscribe(self)
    cluster.subscribe(self, classOf[MemberUp])
  }
  override def postStop(): Unit = {
    ClusterMetricsExtension(context.system).unsubscribe(self)
    Cluster(context.system).unsubscribe(self)
  }

  def receive: Receive = {
    case MemberUp(m) if m.roles.contains(ConsumerApp.clusterRole) =>
      consumers += m.address.hostPort
    case ClusterMetricsChanged(clusterMetrics) =>
      clusterMetrics
        .filter(nm => consumers.contains(nm.address.hostPort))
        .foreach(updateHeapUse(_))
    case "logConsumersHeapUse" => {
      metricsLogger.log(clusterHeapMetrics.calculateNodesHeapUseAvgs)
      clusterHeapMetrics.clear()
    }
  }

  private[this] def updateHeapUse(nodeMetrics: NodeMetrics) {
    nodeMetrics match {
      case HeapMemory(address, timestamp, used, committed, max) => {
        val usedMB = Math.round(used.doubleValue / 1024 / 1024)
        clusterHeapMetrics.updateHeapUse(address.hostPort, usedMB)
      }
      case _ => // no heap info
    }
  }

}

object ClusterListener {

  def props(metricsIntervalSeconds: Int): Props = Props(new ClusterListener(metricsIntervalSeconds: Int))

}
