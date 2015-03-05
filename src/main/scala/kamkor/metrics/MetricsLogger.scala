package kamkor.metrics

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{ Files, Paths, StandardOpenOption }
import java.text.SimpleDateFormat

/**
 * @constructor Creates metrics directory and in that directory file heap_<name>_metricstime.csv.
 * @param name of metrics
 */
class MetricsLogger(name: String) {

  private[this] val metricsDir = new File("metrics")
  metricsDir.mkdir()

  private[this] val metricsTime = new SimpleDateFormat("hhmmss").format(System.currentTimeMillis)
  private[this] val metricsPath = Paths.get(metricsDir.getName, s"heap_${name}_${metricsTime}.csv")
  metricsPath.toFile().createNewFile()

  /**
   * appends timestamp,metrics.mkString(",") to metrics file
   * @param metrics
   */
  def log(metrics: Iterable[Long]) {
    val log = System.currentTimeMillis + "," + metrics.mkString(",") + "%n".format()
    Files.write(metricsPath, log.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND)
  }

}
