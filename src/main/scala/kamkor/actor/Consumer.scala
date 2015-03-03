package kamkor.actor

import akka.actor.{ Actor, Props, UnboundedStash }
import scala.concurrent.duration.DurationInt

class Consumer(val processingTimeMillis: Int) extends Actor with UnboundedStash {

  import context.dispatcher

  def receive: Receive = {
    case data: Array[Int] => {
      context.become(processing, discardOld = false)
      context.system.scheduler.scheduleOnce(processingTimeMillis.millis, self, "endOfProcessing")
    }
  }

  private[this] def processing: Receive = {
    case data: Array[Int] => stash()
    case "endOfProcessing" => {
      unstashAll()
      context.unbecome()
    }
  }

}

object Consumer {

  def props(processingTimeMillis: Int): Props = Props(new Consumer(processingTimeMillis))

}
