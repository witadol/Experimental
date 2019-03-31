import java.io.FileNotFoundException

import scala.collection.immutable.ListMap
import scala.collection.mutable.ArrayBuffer

object initial {

  def main(args: Array[String]): Unit = {
    try {
      val file_name = "/Users/victor/experi.csv"
      val crime_records = load_records(file_name)
      val grouped_records = group_records(crime_records)
      print_top_5(grouped_records)

    } catch{
      case e: FileNotFoundException => println(s"Can't open ${e.getMessage}")
    }
  }


  def load_records(file_name: String): ArrayBuffer[Map[String, String]] = {
    val valid_crimes = ArrayBuffer[Map[String, String]]()
    val bufferedSource = io.Source.fromFile(file_name)

    try {

      val file_iterator = bufferedSource.getLines
      val header_line = file_iterator.next()
      val header_cols = header_line.split(",").map(_.trim)

      for (line <- file_iterator) {
        val cols = line.split(",").map(_.trim)
        val record = header_cols.zip(cols).toMap
        if (record("Crime ID").nonEmpty) {
          valid_crimes += record
        }
      }

    } finally {
      bufferedSource.close
    }
    valid_crimes
  }

  def group_records(crime_records: ArrayBuffer[Map[String, String]]): ListMap[(String, String), ArrayBuffer[Map[String, String]]] = {
    val grouped_records = crime_records.groupBy(record => (record("Latitude"), record("Longitude")))
    val ordered_records = ListMap(grouped_records.toSeq.sortBy(_._2.size)(Ordering[Int].reverse):_*)
    ordered_records
  }

  def print_top_5(records: ListMap[(String, String), ArrayBuffer[Map[String, String]]]): Unit = {

    for (record <- records.slice(0,5)) {
      println("-"*35)
      println(record._1+":"+record._2.size)
      for (theft <- record._2) {
        println(theft("Crime type"))
      }

    }
    println("-"*35)
  }

//  def load_data_record()
}