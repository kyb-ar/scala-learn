// Scala program to print Hello World
import sys.process._
import java.io.InputStream
import java.io.OutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.BufferedWriter
import java.io.BufferedReader
import scala.concurrent.SyncVar

object Hello {
  val inputString = new SyncVar[String]

  private def outputFn(stdOut: InputStream): Unit = {
    val reader =
      new BufferedReader(new InputStreamReader(stdOut))
    val buffer: StringBuilder = new StringBuilder()
    try {
      var line = reader.readLine()
      while (line != null) {
        println(line)
        line = reader.readLine()
      }
    } catch {
      case exc: Throwable =>
        stdOut.close()
    }
  }

  private def inputFn(stdin: OutputStream): Unit = {
    val writer =
      new BufferedWriter(new OutputStreamWriter(stdin))
    try {
      var input = inputString.take()

      while (true) {
        writer.write(input)
        writer.flush()

        input = inputString.take()
      }
    } catch {
      case exc: Throwable =>
        stdin.close()
    }
  }

  private def errorFn(stdErr: InputStream): Unit = {
    val reader = new BufferedReader(new InputStreamReader(stdErr))
    try {
      var line = reader.readLine()
      while (line != null) {
        println(line)
        line = reader.readLine()
      }
      stdErr.close()
    } catch {
      case exc: Throwable =>
        stdErr.close()
    }
  }

  def main(args: Array[String]) {
    val procIO = new ProcessIO(inputFn(_), outputFn(_), errorFn(_))
    "cat".run(procIO)
    inputString.put("One\n")
    inputString.put("Two")
    inputString.put("Three\n")
  }
}
