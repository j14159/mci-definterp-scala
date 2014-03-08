package definterp

import scala.util.Try

/**
  * Tried to be as close as possibly to the original meta-circular interpreter
  * in the paper.  Obviously took a few shortcuts provided by Scala but I don't
  * think that violates the spirit of the effort and original.
  */
object Interpreter {
  val startingEnv = BaseEnv

  def interpret(program: Seq[Exp]): Exp = eval(program, startingEnv)

  def eval(program: Seq[Exp], env: Env): Exp = {
    val res = eval(program.head, env)
    program.tail match {
      case h :: t => eval(program.tail, env)
      case _ => res
    }
  }

  def eval(e: Exp, env: Env): Exp = e match {
    case c: Const => c
    case v @ DVar(name) => getEnv(name, env)
    case l: Lambda => Closure(l, env)
    case a: Appl => evAppl(a, env)
    case l: LetRec => evLetRec(l, env)
    case c: Cond => evCond(c, env)
  }

  def evLetRec(l: LetRec, env: Env): Exp = eval(l.body, Env(l.declaredVar, l.exp, env))

  def evCond(c: Cond, env: Env): Exp = c match {
    case Cond(prem, conc, alt) =>
      eval(prem, env) match {
        case DBoolean(true) => eval(conc, env)
        case DBoolean(false) => eval(alt, env)
      }
  }

  def evAppl(a: Appl, env: Env): Exp = a match {
    case Appl(Succ, e) => 
      eval(e, env) match {
        case DInteger(i) => DInteger(i + 1)
        case other => throw new Exception(s"Can't increment ${other}")
      }
    //equivalent to Eq(x)(y), currying as in definterp paper:
    case Appl(Appl(Eq, ex), ey) => 
      val xx = eval(ex, env)
      val yy = eval(ey, env)
      DBoolean(xx == yy)
    case Appl(Closure(Lambda(fp, body), cEnv), expr) => 
      /*
       * This is a briefer but equivalent "extension" of the Lambda's original environment
       * as given in the paper in the function "ext".  Evaluation of the lambda in the 
       * closure should not see the _current_ environment, only the binding of its formal 
       * parameter to the expression to which the closure is applied and the environment it 
       * closed over at the time of its definition.
       */
      eval(body, Env(fp, expr, Env(fp, expr, cEnv)))
    case Appl(e1, e2) =>
      val r1 = eval(e1, env)
      val r2 = eval(e2, env)
      eval(Appl(r1, r2), env)
  }

  /*
   * A bit of a cop-out in that I didn't implement environments as a chain of functions.
   * Will likely revisit in a later interpreter especially if I get into the forced-applicative
   * order necessary in something like Haskell.
   */
  def getEnv(name: String, e: Env): Exp = e match {
    case EnvElem(n, v, _) if n == name => v
    case EnvElem(_, _, next) => getEnv(name, next)
    case _ => throw new Exception(s"Failed to find ${name}")
  }
}
