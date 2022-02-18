# Scala coding challenge - Amazon reviews

Amazons online-shop is well known for its review system. Customers can write [reviews](https://www.amazon.com/Legend-Zelda-Links-Awakening-game-boy/dp/B00002ST3U?th=1#customerReviews) for products and rate them with stars from 1 to 5. Customer reviews themselves can be rated as helpful or not helpful by other customers.

Your task is to create a web service that provides an API that takes certain kind of requests and uses the reviews to provide helpful responses.


## The requirements

Your web service is given a path to the file that contains the reviews once when starting up.
It should then be running and listening on `localhost:8080`.

### Find the best rated products

We want to search for best rated products within a certain period of time. The rating of a product is determined by the average number of stars (the `overall` field). Higher numbers means better rating. A date range **in UTC** (in which to search) will be given and the number of results should be limited by the given value `limit`. We only want to consider products that have a minimum number of reviews given by the parameter `min_number_reviews`.

The request will be made like this:

```http
POST /amazon/best-rated HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "start": "01.01.2010",
  "end": "31.12.2020",
  "limit": 2,
  "min_number_reviews": 2
}
```

Your API should respond to such a request by providing a list of results, where each result contains the product id (`asin`) and the average rating.

When working with the [test data](/data_sample/video_game_reviews_example.json) a request like the one above should be answered like that:

```json
[
    {
        "asin": "B000JQ0JNS",
        "average_rating": 4.5
    },
    {
        "asin": "B000NI7RW8",
        "average_rating": 3.666666666666666666666666666666667
    }
]
```

## :green_book: Documentation

### Build instructions
The application is built with sbt, in order to build it you will need to install :
- a jdk
- sbt [installation instructions](https://www.scala-sbt.org/1.x/docs/Setup.html)

Tests can be run with this command `sbt test`

### Usage
The webservice application can be run from sbt using this command from the repository root folder.
Note : The path to the file is configurable by passing it as an argument.
```shell
sbt "api/run data_sample/video_game_reviews_example.json"

```

### Design
- The project is designed around two modules :
    - `core` contains the main logic components of the application
    - `api` contains the all the components related to the http layer and api
- The technical stack includes :
    - [cats-effect](https://typelevel.org/cats-effect/) to allow to write the application in a purely functional style
    - [fs2](https://fs2.io) for the purely functional streaming capabilities
    - [http4s](https://github.com/bkirwi/decline) for building the http layer


### Possible Improvements
- Add a CI to the project (eg github actions)
- Add [scala-steward](https://github.com/scala-steward-org/scala-steward) to help maintain the dependencies up to date
- Improve logging
- Improve api validation
- Improve error handling
- Package the `api` as a native executable with graalvm. It can be done with the `JavaAppPackaging` and `GraalVMNativeImagePlugin` sbt plugins provided by [sbt-native-packager](https://github.com/sbt/sbt-native-packager)
- Package the `api` as a docker image