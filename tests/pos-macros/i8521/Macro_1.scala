import scala.quoted._

object Foo {
  inline def foo[T <: AnyKind]: String = ${ bar[T] }

  def bar[T <: AnyKind : Type](using qctx: QuoteContext): Expr[String] = {
    import qctx.tasty.{Type => _, _}

    def packageToName(sym: Symbol): Unit = {
      if sym.isPackageDef then
        packageToName(sym.owner)
    }

    val sym = implicitly[Type[T]].unseal.symbol
    if (!sym.isNoSymbol) {
      sym.tree match {
        case c: ClassDef =>
          if (!sym.maybeOwner.isNoSymbol) {
            if sym.maybeOwner.isPackageDef then
              packageToName(sym.maybeOwner)
          }
      }
    }
    Expr("")
  }
}
