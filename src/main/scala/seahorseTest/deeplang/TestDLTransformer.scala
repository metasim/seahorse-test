package seahorseTest.deeplang

import ai.deepsense.deeplang.doperables.SparkTransformerWrapper
import ai.deepsense.deeplang.params.Param
import seahorseTest.spark.TestSparkTransformer

class TestDLTransformer extends SparkTransformerWrapper[TestSparkTransformer] {
  override def params: Array[Param[_]] = Array.empty
}
