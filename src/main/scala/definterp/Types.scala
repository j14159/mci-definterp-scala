package definterp

sealed trait Exp
sealed trait Const extends Exp

/*
 * Variable name, see environments below.
 */
case class DVar(name: String) extends Exp

case class DInteger(i: Int) extends Const
case class DBoolean(b: Boolean) extends Const

//Function application
case class Appl(e1: Exp, e2: Exp) extends Exp
//Conditional (think LISP/Scheme)
case class Cond(premise: Exp, conc: Exp, alt: Exp) extends Exp
//Obvious:
case class Lambda(fp: String, body: Exp) extends Exp
/*
 * "Recursive" let expression, meaning that the declared var is accessible
 * within the expression it's bound to.
 */
case class LetRec(declaredVar: String, exp: Exp, body: Exp) extends Exp


/*
 * Environments bind names to values.  A DVar (variable) is a reference to
 * an element in the environment.
 */
sealed trait Env

/*
 * Simple helper object
 */
object Env extends Env {
  def apply(name: String, v: Exp, next: Env) = EnvElem(name, v, next)
}

//Equivalent to Nil for lists:
case object BaseEnv extends Env
//Like a Cons for a list but key-value:
case class EnvElem(name: String, v: Exp, next: Env) extends Env

//built-in function as in the paper, "increment by 1"
case object Succ extends Const
//built-in function as in the paper, equality test.
case object Eq extends Const

/*
 * Packages a lambda definition with its surrounding environment.
 * Necessary for currying.
 */
case class Closure(l: Lambda, env: Env) extends Const
