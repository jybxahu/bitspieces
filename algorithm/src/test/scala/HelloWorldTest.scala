import org.scalatest.FunSuite

class HelloWorldTest extends FunSuite {

  test("test hello world") {
    assert("Hello World".eq(new HelloWorld().get))
  }
}
