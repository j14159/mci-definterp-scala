package definterp

import org.scalatest._

class SimpleTests extends FlatSpec with ShouldMatchers {
  "Constants" should "evaluate to themselves when interpret" in {
    val b = DBoolean(true)
    val i = DInteger(1)

    Interpreter.interpret(List(b)) should be (b)
    Interpreter.interpret(List(i)) should be (i)
  }

  "Use of succ and eq" should "evaluate correctly" in {
    val s = Appl(Lambda("x", Appl(Succ, DVar("x"))), DInteger(1))
    val e1 = Appl(Appl(Eq, DInteger(1)), DInteger(2))
    val e2 = Appl(Appl(Eq, DInteger(5)), DInteger(5))

    Interpreter.interpret(List(s)) should be (DInteger(2))
    Interpreter.interpret(List(e1)) should be (DBoolean(false))
    Interpreter.interpret(List(e2)) should be (DBoolean(true))
  }

  "LetRec and nested LetRec instances" should "evaluate correctly" in {
    val l1 = LetRec("x", DInteger(1), DVar("x"))
    val l2 = LetRec(
      "x", DInteger(4), LetRec(
        "y", DInteger(3), Appl(Appl(Eq, DVar("x")), DVar("y"))))
    val l3 = LetRec(
      "x", Lambda("x", Appl(Succ, DVar("x"))),
      (Appl(DVar("x"), DInteger(41))))

    Interpreter.interpret(List(l1)) should be (DInteger(1))
    Interpreter.interpret(List(l2)) should be (DBoolean(false))
    Interpreter.interpret(List(l3)) should be (DInteger(42))
  }

  "Conditionals" should "evaluate correctly" in {
    val c1 = Cond(DBoolean(true), DInteger(1), DInteger(2))
    val c2 = Cond(Appl(Appl(Eq, DInteger(4)), DInteger(4)), Appl(Succ, DInteger(8)), DBoolean(false))

    Interpreter.interpret(List(c1)) should be (DInteger(1))
    Interpreter.interpret(List(c2)) should be (DInteger(9))
  }
}

