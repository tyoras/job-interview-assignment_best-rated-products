package com.itravel.api.endpoint

import cats.effect.IO
import cats.syntax.all._
import com.itravel.api.endpoint.ReviewAnalysisEndpointSpec.searchPayload
import com.itravel.core.ReviewAnalysisService
import com.itravel.core.ReviewAnalysisService.SearchParams
import com.itravel.core.model.{RatedProduct, Review}
import com.itravel.core.reader.ReviewReader
import fs2.Stream
import io.circe.Json
import io.circe.parser._
import org.http4s.Method.POST
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client.dsl.io._
import org.http4s.implicits._
import org.http4s.{EntityDecoder, Response, Status}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{Assertion, EitherValues}

class ReviewAnalysisEndpointSpec extends AnyFlatSpec with Matchers with EitherValues {
  import cats.effect.unsafe.implicits.global

  "POST amazon/best-rated endpoint" should "return the result as a json array" in {
    val reader = new ReviewReader[IO] {
      override def stream: Stream[IO, Review] = Stream.emits(Seq(Review("abc", 3.1, 1L), Review("def", 2.3, 2L)))
    }
    val service = new ReviewAnalysisService[IO] {
      override def findBestRated(reviews: Stream[IO, Review], searchParams: SearchParams): IO[List[RatedProduct]] =
        List(RatedProduct("abc", 3.1), RatedProduct("def", 2.3)).pure[IO]
    }
    val expectedBody = parse("""[
        |  {
        |    "asin" : "abc",
        |    "average_rating" : 3.1
        |  },
        |  {
        |    "asin" : "def",
        |    "average_rating" : 2.3
        |  }
        |]""".stripMargin).value

    val testedEndpoint = new ReviewAnalysisEndpoint[IO](service, reader)
    val response       = testedEndpoint.routes.orNotFound.run(POST(searchPayload, uri"/best-rated"))

    check[Json](response, Status.Ok, expectedBody.some)
  }

  it should "return the result as an empty json array when no result are found" in {
    val reader = new ReviewReader[IO] {
      override def stream: Stream[IO, Review] = Stream.emits(Seq(Review("abc", 3.1, 1L), Review("def", 2.3, 2L)))
    }
    val service = new ReviewAnalysisService[IO] {
      override def findBestRated(reviews: Stream[IO, Review], searchParams: SearchParams): IO[List[RatedProduct]] =
        List.empty.pure[IO]
    }
    val expectedBody = parse("[]").value

    val testedEndpoint = new ReviewAnalysisEndpoint[IO](service, reader)
    val response       = testedEndpoint.routes.orNotFound.run(POST(searchPayload, uri"/best-rated"))

    check[Json](response, Status.Ok, expectedBody.some)
  }

  private def check[A](actual: IO[Response[IO]], expectedStatus: Status, expectedBody: Option[A])(implicit
    ev: EntityDecoder[IO, A]
  ): Assertion = {
    val actualResp = actual.unsafeRunSync()
    actualResp.status shouldBe expectedStatus
    expectedBody.fold[Assertion](actualResp.body.compile.toVector.unsafeRunSync() shouldBe empty)( // Verify Response's body is empty.
      expected => actualResp.as[A].unsafeRunSync() shouldBe expected
    )
  }
}

object ReviewAnalysisEndpointSpec {

  val searchPayload: Json = parse("""{
      |  "start" : "01.01.2010",
      |  "end" : "31.12.2020",
      |  "limit" : 5,
      |  "min_number_reviews" : 2
      |}""".stripMargin).getOrElse(Json.False)
}
