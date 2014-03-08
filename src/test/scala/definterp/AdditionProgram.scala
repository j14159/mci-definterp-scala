package definterp

import org.scalatest._

class AdditionProgram extends FlatSpec with Matchers {

  val addition = 
    Lambda("x",
      Lambda("y", LetRec("add-until",
        Lambda("xx", 
          Lambda("added",
            Cond(
              Appl(Appl(Eq, DVar("added")), DVar("y")),
              DVar("xx"),
              Appl(Appl(DVar("add-until"), Appl(Succ, DVar("xx"))), Appl(Succ, DVar("added"))))
          )),
      Appl(Appl(DVar("add-until"), DVar("x")), DInteger(0)))))

  val program = List(
    Appl(Appl(addition, DInteger(2)), DInteger(3))
  )

  "A curried addition function only using eq and succ" should "return the correct result" in {
    Interpreter.interpret(program) should be (DInteger(5))
  }

}
