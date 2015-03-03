package kamkor

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import kamkor.actor.{ ClusterListener, Producer }

object ProducerApp {

  def main(args: Array[String]) {
    val config = ConfigFactory.load()
    val system = ActorSystem("ClusterSystem", config)

    val metricsIntervalSeconds = config.getInt("producer.metrics-interval-seconds")
    system.actorOf(ClusterListener.props(metricsIntervalSeconds))

    val sendIntervalMillis = config.getInt("producer.send-interval-millis")
    val dataArraySize = config.getInt("producer.data-array-size")
    val producer = system.actorOf(Producer.props(sendIntervalMillis, dataArraySize), name = "producer")
  }

}
