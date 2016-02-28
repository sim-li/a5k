package de.bht.lischka.adminTool5k.extractors
import de.bht.lischka.adminTool5k.ModelX.MemoryUsage

object MemoryUsageMatcher {
  // @TODO: Introduce type system hirarchy of matchers
  // The only values that change here are the identifiers plus the type
  def unapply(identifierAndvalue: (String, String)): Option[Long] = identifierAndvalue match {
    case ("MEM", value) => memoryUsage(value)
    case _ => None
  }

  def memoryUsage(memColumn: String): Option[Long] = {
    val MemoryUsageAndUnit = """([0-9]+)([KkMmBbGgTt]?)[+-]*""".r
    val MemoryUsageAndUnit(mem: String, unit: String) = memColumn
    val multiplicator = unit match {
      case ""  => 1L
      case "B" => 1L
      case "K" => 1024L
      case "M" => 1024L * 1024
      case "G" => 1024L * 1024 * 1024
      case "T" => 1024L * 1024 * 1024 * 1024 //@TODO: CHECK if G and T symbol are really commmon in top
    }
    Option(mem.toLong).map(m => m * multiplicator)
  }
}
