package dentaira.cobaco.server.test

import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class FutureTest extends AnyFunSuite {


  test("f1") {
    val f = future("in future").map { s =>
      println("1")
      throw new IllegalStateException("in map")
      s
    } recover {
      case t => {
        println("2")
        Future.successful("recovered")
      }
    }
    Await.result(f, Duration.Inf)
    println(f)
  }

  def future(str: String): Future[String] = {
    println("0")
//    throw new IllegalStateException("in future")
  val f: String => String = s => s.toUpperCase
    Future(body = {
//      throw new IllegalStateException("in future")
      str.toUpperCase
    })
  }

  test("f2") {
    val f: String => String = s => s.toUpperCase
    print(f("ttt"))
  }
}
