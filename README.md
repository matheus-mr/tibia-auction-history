# Tibia Auction History

This project is mainly an exercise of how an REST API could be built using advanced filtering, that is, when the usual query
parameters strategy would not be suitable. For example when you need to have logical operators (and/or) and multiple
levels of nesting on these operators.

But it is also a complete project for my portfolio as it involves multiple technologies like: web scrapping, 
REST API with paginating and sorting, Spring Boot, MongoDB, React, Prometheus, Grafana, Swagger and TestContainers.

The game Tibia was chosen as an example, in this game you can auction your characters in the official site.

But [The oficial site](https://www.tibia.com/charactertrade/?subtopic=pastcharactertrades) does not provide good filters 
nor the entire history of auctions.

By providing good filters and the entire history, a player can better match his character to similar characters
sold in the past and then have an idea about how much his character would be worth.

## Pros and Cons of this solution

## Architecture
Backend uses Spring Boot + MongoDB and frontend uses React.

The backend is divided by two features: auction fetch and auction search.

Auction fetch is responsible for scraping the data and also validating the current scrap logic against selected cases
before beginning to scrape new data. This is done to prevent saving incorrect data to the DB. 
Kinda like an test on the fly.

Auction search is responsible for providing an REST API to search for auctions.

## API Usage

### Creating an auction search
To search for auctions, first one must create an auction search by making an POST to `/api/v1/auctions/search`
providing the criteria object, this endpoint will then respond with a UUID v4 which will later be used to fetch the
search results.

The criteria object has the following specification:
```
class AuctionSearchCriterion {

    String field;

    Operator operator;

    List<Object> values;

    List<AuctionSearchCriterion> criterias;
}
```

Note that this is a recursive object, so you can nest criterias.

Also refer to the Open API spec for details on the available fields and operators.

Usage examples:

Finding auctions for characters which have a level greater than 500
```
{
    "field": "level",
    "operator": "gt",
    "values": [500]
}
```

Finding auctions for characters which have a level greater than 500 and are Paladins
```
{
    "operator": "and",
    "criterias": [
        {
            "field": "level",
            "operator": "gt",
            "values": [500]
        },
        {
            "field": "vocation",
            "operator": "eq",
            "values": ["KNIGHT"]
        },
    ]
}
```

### Fetching an auction search
To fetch the filters used by a search one can make an GET request to `/api/v1/auctions/search/{id}` providing the id 
of the search.

Usage examples:

`GET /api/v1/auctions/search/993d2c76-cd05-447f-9ae0-3e1fb1965da5` would return the criteria object used to create this search.
```
{
    "field": "level",
    "operator": "gt",
    "values": [500]
}
```

### Fetching results from an auction search
To fetch the results from a search one can make an GET request to `/api/v1/auctions/search/{id}/results` which will return
an array with auction objects. [Pagination and sorting applies](#pagination-and-sorting). 
Refer to the Open API spec for details on the auction object.

### Fetching all auctions without filtering
To fetch all auctions one can make an GET request to `/api/v1/auctions`. 
[Pagination and sorting applies](#pagination-and-sorting). Refer to the Open API spec for details on the auction object.

This endpoint is mainly used when the user has just opened the site or has not done any filtering.

### Fetching auction domain
The domain consists of all the available options for each field. For example all worlds, all outfits, all items names etc.

This endpoint is mainly used when the site loads for the first time. To get the domain one can make an GET request to 
`/api/v1/auctions/domain`, which will return the domain object. 
Refer to the Open API spec for details on the domain object.

### Pagination and sorting

Pagination and sorting options are available [when fetching all auctions](#fetching-all-auctions-without-filtering) 
or [when fetching the results of a search](#fetching-results-from-an-auction-search) .

An `X-Total-Count` header is provided in the response of the above two endpoints, to indicate the
amount of auctions in the complete result set.

For pagination and sorting use the following query params:

| Query Param | Possible values                                  | Default Value | Obs                                                                           |
|-------------|--------------------------------------------------|---------------|-------------------------------------------------------------------------------|
| limit       | an integer between 1 and 1000                    | 100           |                                                                               |
| offset      | an non negative integer                          | 0             | if offset is greater than the result set size then an empty array is returned |
| sortBy      | one of: name, level, vocation, world, auctionEnd | auctionEnd    |                                                                               |
| orderBy     | one of: asc, desc                                | desc          |                                                                               |

### Limits
Logical operators, i.e. "and" and "or", must have at most 30 criterias.

Comparison operators, i.e. all operators besides logical ones, must have at most 50 values.

Also the maximum amount of recursion depth for the criteria object is 10.

## Running the project
In the `./data` folder I've included the first 1.000.000 auctions scrapped data which will be loaded to 
the MongoDB automatically.

Also, I have a public built docker image of the app on docker hub.

So you just need to use docker compose to spin up the project with: `docker compose up -d`

And the site will be available on http://localhost:3000 and the API on http://localhost:8080

## Disclaimer
This data is scraped from the official Tibia website and provided for educational purposes only.
All rights to the Tibia game and its associated data belong to CipSoft.
