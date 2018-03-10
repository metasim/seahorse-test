package seahorseTest.spark

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.types.StructType

class TestSparkTransformer extends Transformer {
  override def transform(dataset: Dataset[_]): DataFrame = dataset.toDF

  override def copy(extra: ParamMap): Transformer = defaultCopy(extra)

  override def transformSchema(schema: StructType): StructType = schema

  override val uid: String = "c2d1ee2f-4723-4bf2-8f50-17791a9341b8"
}
