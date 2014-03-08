Meta-Circular Definitional Interpreter In Scala
===============================================

Just me trying to learn more.

# What
This is an attempt to build a meta-circular interpreter roughly equivalent to the first one shown in the paper [Definitional Interpreters for Higher-Order Programming Languages](http://citeseer.ist.psu.edu/viewdoc/summary?doi=10.1.1.110.5892) (Reynolds, 1972).  I came across mention of it in the original Scheme paper ([AI Memo 349](http://dspace.mit.edu/bitstream/handle/1721.1/5794/AIM-349.pdf)) when it talked about "environments", amongst other things.

This is basically two source files, one for the types making up the [AST](http://en.wikipedia.org/wiki/Abstract_syntax_tree) and one for the simple interpreter itself.  It was interesting to work through how to handle the equality function via currying (that one took me an embarassingly long time) and simple environment extension, both of which were far simpler than I realized at first.

# How
Two tests exist, SimpleTests to run through basic functionality and AdditionProgram that defines an addition function and applies it.  Probably crazy easy to nuke the stack with big numbers.  To run, make sure you have sbt installed and then:

    sbt test

