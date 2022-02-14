package com.itravel.core.model

import com.itravel.core._
import io.circe.{Decoder, DecodingFailure}
import io.circe.parser.decode
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AsinDecoderSpec extends AnyFlatSpec with Matchers with EitherValues {

  implicit val decoder: Decoder[Asin] = com.itravel.core.model.asinDecoder

  "Asin decoder" should "work with a valid review" in {
    val expected = "B000Q75VCO"
    decode[Asin](validJsonReview).value shouldBe expected
  }

  it should "fail when the review is missing the asin field" in {
    decode[Asin](reviewWithoutAsin).left.value shouldBe a[DecodingFailure]
  }

  it should "work with a review without optional field" in {
    val expected = "B000NI7RW8"
    decode[Asin](reviewWithoutReviewerName).value shouldBe expected
  }

}
