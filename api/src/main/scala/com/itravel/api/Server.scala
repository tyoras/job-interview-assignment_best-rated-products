package com.itravel.api

import cats.effect._
import cats.syntax.all._
import com.itravel.api.endpoint.ReviewAnalysisEndpoint
import com.itravel.core.ReviewAnalysisService
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.server.middleware.Logger

import java.nio.file.Path
import scala.util.chaining._

object Server extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val datasetPath = args.headOption.map(Path.of(_))
    init[IO](datasetPath).useForever
      .as(ExitCode.Success)
      .handleErrorWith(t => IO.println(s"Service has failed to start because ${t.getMessage}").as(ExitCode.Error))
  }

  private def init[F[_] : Async](datasetPath: Option[Path]): Resource[F, Unit] = for {
    path <- Resource.eval(Async[F].fromOption(datasetPath, new Exception("the dataset path is missing")))
    reviewAnalysisService  = ReviewAnalysisService[F]
    reviewAnalysisEndpoint = ReviewAnalysisEndpoint.fromJsonFile(reviewAnalysisService, path)
    httpApp = Router("amazon" -> reviewAnalysisEndpoint.routes).orNotFound
      .pipe(Logger.httpApp(logHeaders = false, logBody = false))

    _ <- BlazeServerBuilder[F].bindHttp().withHttpApp(httpApp).resource.void
  } yield ()

}
