package kamkor.actor

import scala.concurrent.duration.DurationInt

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.testkit.{ EventFilter, ImplicitSender, TestKit }

class ConsumerSpec(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  def this() = this(
    ActorSystem("ClusterSystem",
      ConfigFactory.parseString("""        
        akka.loggers = ["akka.testkit.TestEventListener"]
        akka.loglevel = "DEBUG"        
        """)))

  override def afterAll: Unit = TestKit.shutdownActorSystem(system)

  "A Customer actor that processes 1 message for 200 millis" must {
    "log endedProcessing with debug level 5 times within 1-1.3 seconds" in {
      val consumer = system.actorOf(Consumer.props(processingTimeMillis = 200))
      val data: Array[Int] = Array(0, 1, 2)

      // akka scheduling is not 100% accurate http://doc.akka.io/docs/akka/snapshot/scala/scheduler.html
      within(999.millis, 1300.millis) {
        EventFilter.debug(pattern = "endProcessing", occurrences = 5) intercept {
          for (_ <- 0 until 5) {
            consumer ! data
          }
        }
      }
    }
  }

}
