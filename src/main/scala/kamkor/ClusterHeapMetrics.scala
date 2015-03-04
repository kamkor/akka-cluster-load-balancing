package kamkor

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{ Files, Paths, StandardOpenOption }
import java.text.SimpleDateFormat

import scala.annotation.migration
import scala.collection.immutable.TreeMap

/**
 * Creates metrics directory and in that directory file heap_<name>_metricstime.csv.
 * Collects heap metrics and logs them to that file.
 *
 * @param name of metrics
 */
class ClusterHeapMetrics(name: String) {

  private[this] val metricsDir = new File("metrics")
  metricsDir.mkdir()

  private[this] val metricsTime = new SimpleDateFormat("hhmmss").format(System.currentTimeMillis)
  private[this] val metricsPath = Paths.get(metricsDir.getName, s"heap_${name}_${metricsTime}.csv")
  metricsPath.toFile().createNewFile()

  /** nodeAddress -> sequence of collected heap use on node */
  private[this] var nodesHeapUse: TreeMap[String, Seq[Long]] = TreeMap.empty

  /** Updates heap use statistics for node with nodeAddress */
  def updateHeapUse(nodeAddress: String, usedMB: Long): Unit = {
    val updatedHeapUse = nodesHeapUse.getOrElse(nodeAddress, Seq.empty) :+ usedMB
    nodesHeapUse += nodeAddress -> updatedHeapUse
  }

  /**
   * Logs to file current average heap use in mb for each consumer:
   * currentTimeMillis,consumer_1_avgheapuse,consumer_2_avgheapuse,..,consumer_n_avgheapuse
   *
   * - Logs only when each consumer has at least one heap use statistic.
   * - Consumers are sorted by their node address.
   */
  def logHeapUse(): Unit = {
      val nodesHeapUseAvgs = calculateNodesHeapUseAvgs
      val log = System.currentTimeMillis + "," + nodesHeapUseAvgs.mkString(",") + "%n".format()
      Files.write(metricsPath, log.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND)
      resetHeapUse()
    //}
  }

  private[this] def calculateNodesHeapUseAvgs =
    nodesHeapUse.values map(nHeapUse => if (nHeapUse.isEmpty) 0 else nHeapUse.sum / nHeapUse.length)

  private[this] def resetHeapUse(): Unit =
    nodesHeapUse = nodesHeapUse.map { case (nodeAddress, heapUse) => nodeAddress -> Seq.empty }

}
