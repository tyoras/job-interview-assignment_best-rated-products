package com.itravel.core.model

import io.circe.parser.decode
import com.itravel.core._
import io.circe.DecodingFailure
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ReviewDecoderSpec extends AnyFlatSpec with Matchers with EitherValues {

  "Review decoder" should "work with a valid review" in {
    val expected = Review("B000Q75VCO", 2d, 1475261866)
    decode[Review](validJsonReview).value shouldBe expected
  }

  it should "fail when the review is missing a mandatory field" in {
    decode[Review](reviewWithoutAsin).left.value shouldBe a[DecodingFailure]
  }

  it should "work with a review without optional field" in {
    val expected = Review("B000NI7RW8", 3d, 1455120950)
    decode[Review](reviewWithoutReviewerName).value shouldBe expected
  }

}
