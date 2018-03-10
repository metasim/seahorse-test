package seahorseTest.deeplang

import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang.doperations.TransformerAsOperation
import ai.deepsense.deeplang.refl.Register

import scala.reflect.runtime.universe

@Register
class TestTransformerAsOp extends TransformerAsOperation[TestDLTransformer] {
  override def tTagTO_1: universe.TypeTag[TestDLTransformer] = implicitly
  override val id: Id = "0445c9a5-c426-4aa9-86fa-fa730fa12895"
  override val name: String = "foo"
  override val description: String = "bar"
}
