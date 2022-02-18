package com.itravel.api.endpoint

import com.itravel.api.endpoint.Payloads.{BestRatedPayload, SearchParamsPayload}
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import io.circe.parser.decode
import io.circe.syntax._
import java.time.LocalDate

class PayloadsSpec extends AnyFlatSpec with Matchers with EitherValues{
  "Search params payload decoder" should "work with a valid payload" in {
    val expected = SearchParamsPayload(LocalDate.of(2010, 1, 1), LocalDate.of(2020, 12, 31), 5, 2)
    val payload = """{
                    |  "start" : "01.01.2010",
                    |  "end" : "31.12.2020",
                    |  "min_number_reviews" : 2,
                    |  "limit" : 5
                    |}""".stripMargin
    decode[SearchParamsPayload](payload).value shouldBe expected
  }

  "Best rated product payload encoder" should "work" in {
    val expected =
      """{
        |  "asin" : "abc",
        |  "average_rating" : 2.1
        |}""".stripMargin
    val bestRated = BestRatedPayload("abc", 2.1)
    bestRated.asJson.spaces2 shouldBe expected
  }

}
