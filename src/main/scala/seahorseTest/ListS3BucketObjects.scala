/*
 * Copyright (c) 2018. Astraea, Inc. All rights reserved.
 */

package seahorseTest

import java.io.FileNotFoundException

import ai.deepsense.commons.types.ColumnType
import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.params.validators.AcceptAllRegexValidator
import ai.deepsense.deeplang.params.{Param, Params, StringParam}
import ai.deepsense.deeplang.refl.Register
import ai.deepsense.deeplang.{DOperation0To1, ExecutionContext}
import ai.deepsense.reportlib.model.{ReportContent, ReportType, Table}
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import java.sql.Timestamp
import scala.collection.JavaConversions._
import scala.reflect.runtime.universe

/**
 * Seahorse SDK test
 */
@Register
final class ListS3BucketObjects extends DOperation0To1[DataFrame] with Params {
  def tTagTO_0: universe.TypeTag[DataFrame] = implicitly
  val id: Id = "c9eec610-816a-4d66-b029-0da5bf1b14d3"
  val name: String = "List S3 Objects"
  val description: String = "List items matching a prefix in an S3 bucket"
  def params: Array[Param[_]] = Array(region, bucket, prefix)

  val bucket = StringParam(
    "bucket",
    Some("S3 bucket to list"),
    new AcceptAllRegexValidator()
  )
  def getBucket: String = $(bucket)
  def setBucket(value: String): this.type = set(bucket, value)
  setBucket("gdelt-open-data")

  val prefix = StringParam(
    "prefix",
    Some("S3 key prefix"),
    new AcceptAllRegexValidator()
  )
  def getPrefix: String = $(prefix)
  def setPrefix(value: String): this.type = set(prefix, value)
  setPrefix("events")

  val region = StringParam(
    "region",
    Some("AWS compute region"),
    new AcceptAllRegexValidator()
  )
  def getRegion: String = $(region)
  def setRegion(value: String): this.type = set(region, value)
  setRegion("us-east-1")

  private val schema = StructType(Seq(
    StructField("key", StringType, false),
    StructField("owner", StringType, false),
    StructField("lastModified", TimestampType, false),
    StructField("size", LongType, false)
  ))

  protected def execute()(context: ExecutionContext): DataFrame = {
    val builder = AmazonS3ClientBuilder.standard()
    builder.setRegion($(region))
    val s3 = builder.build()

    if(!s3.doesBucketExistV2($(bucket))) {
      throw new FileNotFoundException(s"Could not access S3 bucket '$bucket'")
    }

    val objects = s3.listObjects($(bucket), $(prefix)).getObjectSummaries
    val rows = for {
      obj ← objects.toList
      ts = Timestamp.from(obj.getLastModified.toInstant)
    } yield Row(obj.getKey, obj.getOwner.getDisplayName, ts, obj.getSize)


    val ss = context.sparkSQLSession.getSparkSession
    val sparkDF = ss.createDataFrame(context.sparkContext.makeRDD(rows), schema)
    DataFrame.fromSparkDataFrame(sparkDF)
  }
}
