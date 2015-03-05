package kamkor.metrics

import org.scalatest._

class ClusterHeapMetricsSpec extends WordSpec with Matchers {

  "ClusterHeapMetrics" must {

    "calculate average nodes heap use sorted by their addresses" in {
      val metrics = new ClusterHeapMetrics()
      metrics.updateHeapUse("2", 100L)
      metrics.updateHeapUse("2", 200L)
      metrics.updateHeapUse("1", 10L)
      metrics.updateHeapUse("1", 20L)
      metrics.calculateNodesHeapUseAvgs should equal(Iterable(15L, 150L))
    }

    "return 0 average nodes heap use after clear was called" in {
      val metrics = new ClusterHeapMetrics()
      metrics.updateHeapUse("2", 100L)
      metrics.updateHeapUse("1", 10L)
      metrics.clear()
      metrics.calculateNodesHeapUseAvgs should equal(Iterable(0L, 0L))
    }

    "return 0 average heap use for nodes without heap use stats" in {
      val metrics = new ClusterHeapMetrics()
      metrics.updateHeapUse("2", 100L)
      metrics.updateHeapUse("1", 10L)
      metrics.clear()
      metrics.updateHeapUse("1", 10L)
      metrics.calculateNodesHeapUseAvgs should equal(Iterable(10L, 0L))
    }

  }

}