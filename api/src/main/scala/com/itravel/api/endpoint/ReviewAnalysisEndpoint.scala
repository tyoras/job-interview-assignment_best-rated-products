package com.itravel.api.endpoint

import cats.MonadThrow
import cats.effect.kernel.Concurrent
import cats.syntax.all._
import com.itravel.api.endpoint.Payloads.{BestRatedPayload, SearchParamsPayload}
import com.itravel.core.ReviewAnalysisService
import com.itravel.core.reader.ReviewReader
import fs2.io.file.Files
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

import java.nio.file.Path

class ReviewAnalysisEndpoint[F[_] : MonadThrow : Concurrent](reviewAnalysisService: ReviewAnalysisService[F], reviewReader: ReviewReader[F])
    extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of { case req @ POST -> Root / "best-rated" =>
    for {
      payload <- req.as[SearchParamsPayload]
      searchParams = payload.asSearchParams
      reviews      = reviewReader.stream
      bestRated <- reviewAnalysisService.findBestRated(reviews, searchParams)
      responsePayload = bestRated.map(p => BestRatedPayload(p.asin, p.averageRating))
      response <- Ok(responsePayload)
    } yield response
  }
}

object ReviewAnalysisEndpoint {

  /** Produce an endpoint that will read the reviews from a new line delimited json file
    * @param reviewAnalysisService
    *   Service used for analysing the reviews read from the file
    * @param path
    *   Path to the json file
    * @tparam F
    *   effect
    * @return
    *   endpoint
    */
  def fromJsonFile[F[_] : MonadThrow : Concurrent : Files](reviewAnalysisService: ReviewAnalysisService[F], path: Path): ReviewAnalysisEndpoint[F] = {
    val reviewReader = ReviewReader.fromJsonFile(path)
    new ReviewAnalysisEndpoint[F](reviewAnalysisService, reviewReader)
  }
}
