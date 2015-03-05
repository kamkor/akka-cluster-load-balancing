package kamkor.metrics

import org.scalatest._

class ClusterHeapMetricsSpec extends WordSpec with Matchers {

  "ClusterHeapMetrics" must {

    "calculate average nodes heap use sorted by their addresses" in {
      val heapMetrics = new ClusterHeapMetrics()
      heapMetrics.update("2", 100L)
      heapMetrics.update("2", 200L)
      heapMetrics.update("1", 10L)
      heapMetrics.update("1", 20L)
      heapMetrics.calculateAverages should equal(Iterable(15L, 150L))
    }

    "return 0 average nodes heap use after clear was called" in {
      val metrics = new ClusterHeapMetrics()
      metrics.update("2", 100L)
      metrics.update("1", 10L)
      metrics.clear()
      metrics.calculateAverages should equal(Iterable(0L, 0L))
    }

    "return 0 average heap use for nodes without heap use stats" in {
      val metrics = new ClusterHeapMetrics()
      metrics.update("2", 100L)
      metrics.update("1", 10L)
      metrics.clear()
      metrics.update("1", 10L)
      metrics.calculateAverages should equal(Iterable(10L, 0L))
    }

  }

}