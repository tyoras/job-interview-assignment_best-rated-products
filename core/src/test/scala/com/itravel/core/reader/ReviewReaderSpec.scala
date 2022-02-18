package com.itravel.core.reader

import cats.effect.IO
import com.itravel.core._
import com.itravel.core.model.Review
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.nio.file.Path

class ReviewReaderSpec extends AnyFlatSpec with Matchers {
  import cats.effect.unsafe.implicits.global

  "Streaming from json file" should "work with a file containing one valid review" in {
    val expected = List(Review("B000Q75VCO", 2d, 1475261866))
    val read     = ReviewReader.fromJsonFile[IO](Path.of(oneValidJsonReview)).stream.compile.toList.unsafeRunSync()

    (read should contain).theSameElementsInOrderAs(expected)
  }

  it should "return an empty stream with a file containing only invalid review" in {
    val read = ReviewReader.fromJsonFile[IO](Path.of(invalidJsonReview)).stream.compile.toList.unsafeRunSync()

    read shouldBe empty
  }

  it should "return all review in read order with a file containing several well formatted reviews" in {
    val expected = List(
      Review("B000Q75VCO", 2d, 1475261866),
      Review("B000NI7RW8", 3d, 1455120950),
      Review("B00000AQ4N", 2d, 1571581258),
      Review("B000JQ0JNS", 4d, 1466668179),
      Review("B000KFZ32A", 3d, 1404997356),
      Review("B00000AQ4N", 4d, 1270258819),
      Review("B000NI7RW8", 4d, 1447118407),
      Review("B000KFZ32A", 1d, 1347189467),
      Review("B0002F40AY", 5d, 1348778489),
      Review("B000NI7RW8", 4d, 1339051628),
      Review("B000654P8C", 3d, 1305588946),
      Review("B0002F40AY", 2d, 1342596834),
      Review("B000654P8C", 2d, 1522847344),
      Review("B000JQ0JNS", 5d, 1476369800)
    )
    val read = ReviewReader.fromJsonFile[IO](Path.of(severalValidReviews)).stream.compile.toList.unsafeRunSync()

    (read should contain).theSameElementsInOrderAs(expected)
  }

  it should "ignore invalid line with a file containing several well formatted reviews but one invalid" in {
    val expected = List(
      Review("B000Q75VCO", 2d, 1475261866),
      Review("B000NI7RW8", 3d, 1455120950),
      Review("B000JQ0JNS", 4d, 1466668179),
      Review("B000KFZ32A", 3d, 1404997356),
      Review("B00000AQ4N", 4d, 1270258819),
      Review("B000NI7RW8", 4d, 1447118407),
      Review("B000KFZ32A", 1d, 1347189467),
      Review("B0002F40AY", 5d, 1348778489),
      Review("B000NI7RW8", 4d, 1339051628),
      Review("B000654P8C", 3d, 1305588946),
      Review("B0002F40AY", 2d, 1342596834),
      Review("B000654P8C", 2d, 1522847344),
      Review("B000JQ0JNS", 5d, 1476369800)
    )
    val read = ReviewReader.fromJsonFile[IO](Path.of(severalValidOneInvalidReviews)).stream.compile.toList.unsafeRunSync()

    (read should contain).theSameElementsInOrderAs(expected)
  }

}
