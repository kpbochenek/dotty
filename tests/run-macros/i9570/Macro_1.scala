import scala.quoted._

object Macros {

  object HList {
    sealed trait HList
    case class HCons[+HD, TL <: HList](hd: HD, tl: TL) extends HList
    case object HNil extends HList

    private def sizeImpl(e: Expr[HList], n:Int)(using qctx:QuoteContext): Expr[Int] = {
      import qctx.tasty._
      e match {
        case '{HCons($_,$t)} =>
        //case '{HCons($a,$t)} =>
          sizeImpl(t,n+1)
        case '{HNil} => Expr(n)
      }
    }

    inline def size(inline expr: HList ): Int = {
      ${sizeImpl('expr,0)}
    }

  }
}
