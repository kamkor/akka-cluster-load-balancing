package kamkor.metrics

import scala.collection.immutable.TreeMap

/**
 * Collects heap metrics.
 */
class ClusterHeapMetrics() {

  /** nodeAddress -> sequence of collected heap use on node */
  private[this] var nodesHeapUse: TreeMap[String, Seq[Long]] = TreeMap.empty

  /** Updates heap use statistics for node with nodeAddress */
  def updateHeapUse(nodeAddress: String, usedMB: Long): Unit = {
    val updatedHeapUse = nodesHeapUse.getOrElse(nodeAddress, Seq.empty) :+ usedMB
    nodesHeapUse += nodeAddress -> updatedHeapUse
  }

  /**
   * Nodes are sorted by their node address. Nodes with no metrics receive 0 for avg heap use.
   *
   * @return current average heap use in mb for each node in the metrics:
   * node_1_avgheapuse,node_2_avgheapuse,..,node_n_avgheapuse
   */
  def calculateNodesHeapUseAvgs: Iterable[Long] =
    nodesHeapUse.values map (nHeapUse => if (nHeapUse.isEmpty) 0 else nHeapUse.sum / nHeapUse.length)

  def clear(): Unit =
    nodesHeapUse = nodesHeapUse map { case (nodeAddress, heapUse) => nodeAddress -> Seq.empty }

}
