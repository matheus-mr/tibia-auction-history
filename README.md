# Tibia Auction History

This project is mainly an exercise of how an REST API could be built using advanced filtering, that is, when the usual query parameters strategy would not be suitable. For example when you need to have logical operators (and/or) and multiple levels of nesting on these operators.

But it is also a complete project for my portfolio as it involves multiple technologies like: web scrapping, REST API with paginating and sorting, Spring Boot, MongoDB, Redis, React, Swagger and TestContainers.

The game Tibia was used as an example, in this game you can auction your characters in the official site but [The official site](https://www.tibia.com/charactertrade/?subtopic=pastcharactertrades) does not provide good filters nor the entire history of auctions.

By providing good filters and the entire history, a player can better match his character to similar characters sold in the past and then have an idea about how much his character would be worth.

## Architecture

This repo is a monorepo. Divided into backend, frontend and api-specs.

Backend uses Spring Boot + MongoDB + Redis.

Frontend uses React + React Router + mui + swr.

### Backend Architecture

The backend has three services:

1. Auction Fetch: A command line app that when executed scrapes auctions.
2. Auction Domain: A command line app that when executed updates the materialized view which stores the auction domain.
3. Auction Search: A webapp that provides the REST API for the frontend.

## API Usage

### Fetching all auctions without filtering

To fetch all auctions without filtering for anything one can make an GET request to `/api/v1/auctions`.
[Pagination and sorting applies](#pagination-and-sorting). Refer to the Open API spec for details on the auction object.

This endpoint is mainly used when the user has just opened the site and has not done any filtering yet.

### Fetching auction domain

The domain consists of all the available options for each field. For example all worlds, all outfits, all items names etc.

This endpoint is mainly used when the site loads for the first time. To get the domain one can make an GET request to
`/api/v1/auctions/domain`, which will return the domain object.
Refer to the Open API spec for details on the domain object.

### Creating an auction search

To search for auctions, first one must create an auction search by making an POST to `/api/v1/auctions/search`
providing the criteria object, this endpoint will then respond with a [nanoid](https://github.com/ai/nanoid) which then can be used to fetch the
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

To fetch the filters used by a search one can make an GET request to `/api/v1/auctions/search/{id}` providing the nanoid
of the search.

Usage examples:

`GET /api/v1/auctions/search/5_0aUi7BRmyOeXQLPscxu` would return the criteria object used to create this search.

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

### Pagination and sorting

Pagination and sorting options are available [when fetching all auctions](#fetching-all-auctions-without-filtering)
or [when fetching the results of a search](#fetching-results-from-an-auction-search)

For pagination and sorting use the following query params:

| Query Param | Possible values                                  | Default Value | Obs                                                                           |
| ----------- | ------------------------------------------------ | ------------- | ----------------------------------------------------------------------------- |
| limit       | an integer between 1 and 1000                    | 100           |                                                                               |
| offset      | an non negative integer                          | 0             | if offset is greater than the result set size then an empty array is returned |
| sortBy      | one of: name, level, vocation, world, auctionEnd | auctionEnd    |                                                                               |
| orderBy     | one of: asc, desc                                | desc          |                                                                               |

### Limits

Logical operators, i.e. "and" and "or", must have at most 30 criterias.

Comparison operators, i.e. all operators besides logical ones, must have at most 50 values.

Also the maximum amount of recursion depth for the criteria object is 10.

## Pros and Cons of this solution

### Pros
1. You get all the benefits of using the traditional query param strategy for filtering:
    1. The request is cacheable because it's a GET so all info is on the URL (unlike the POST approach).
    2. The user can bookmark the link and also share with other users.
    3. Browser history navigation works.
2. The search object is store on server side and is not on URL anymore. This means modifying anything doesn't break previous user saved searches links.

### Cons
1. You have to store the search object somewhere, for as long as you want to make the search available for the user.
    1. This could start to take a considerable amount of storage if a lot of searches are created.
    2. If you decide to expire the search sometime then the previous saved links of this search are no longer valid, which would not happen on the query param approach.
    3. To minimize the amount of saved searches, ideally there should be a way to find if two searches are equals. This can be difficult to implement depending on the DSL defined.
2. Two requests are needed to search results, one for creating the search and another one to fetch the results from the search. Even tho creating a search should be
inexpensive, still some resources from the server will be used.

## Running the project

### Running with Docker

The easiest way is to use docker to run everything (all commands assume root directory as working directory):

1. Start by running a mongodb and redis instance

    ```
    docker compose -f ./backend/docker-compose.yml up -d
    ```

2. Then build the backend image

    ```
    docker build -t tibia-auction-history-backend -f backend/service.auctionsearch/Dockerfile ./backend
    ```

3. Then run the backend image

    ```
    docker run --rm -d --network tibia_auction_history_network --name tibia-auction-history-backend -p 8080:8080 -e SPRING_DATA_MONGODB_URI=mongodb://mongo/tibia-auction-history tibia-auction-history-backend
    ```

4. Then build the frontend image
    
    ```
    docker build -t tibia-auction-history-frontend -f frontend/Dockerfile ./frontend
    ```

5. Then run the frontend image
    
    ```
    docker run --rm -d --network tibia_auction_history_network -p 3000:3000 tibia-auction-history-frontend
    ```

Then the website will be available on http://localhost:3000 and the API on http://localhost:8080

But there is a problem, your mongodb is empty, it doesn't have auction data, the domain view built nor indexes. So there is two options:

1. Use the .dump file to restore the data scrapped from 1.5M auctions plus the domain view built and all indexes:
    1. For that, download the .dump file from [google-drive](https://drive.google.com/file/d/1II72d3f-bC4j8W9CsQQjIR4t0ThwzdVo/view?usp=sharing)
    2. Use [mongorestore](https://www.mongodb.com/docs/database-tools/mongorestore/#mongodb-binary-bin.mongorestore) in the folder containing the downloaded file:
    
        ```
        mongorestore --uri="mongodb://localhost:27017/tibia-auction-history" --gzip --archive=tibia-auction-history.dump
        ```
2. Or scrap the data yourself:
    1. For that, first build the auction fetch service image
    
        ```
        docker build -t tibia-auction-history-fetcher -f backend/service.auctionfetch/Dockerfile ./backend
        ```
    2. Then run the auction fetch service
    
        ```
        docker run --rm --network tibia_auction_history_network --name tibia-auction-history-fetcher -e SPRING_DATA_MONGODB_URI=mongodb://mongo/tibia-auction-history tibia-auction-history-fetcher
        ```
    3. Now when you are done scrapping the amount of auctions you want, you need to build the auction domain view, for that, build the auction domain service image
    
        ```
        docker build -t tibia-auction-history-domain -f backend/service.auctiondomain/Dockerfile ./backend
        ```
    4. Then run the auction domain service, which will build the domain view
    
        ```
        docker run --rm --network tibia_auction_history_network --name tibia-auction-history-domain -e SPRING_DATA_MONGODB_URI=mongodb://mongo/tibia-auction-history tibia-auction-history-domain
        ```
    5. Then create the indexes using [mongosh](https://www.mongodb.com/docs/mongodb-shell/) by executing:
    
        ```
        mongosh tibia-auction-history
        ```
        
        ```
        db.auctions.createIndex(
            { 'sold': 1, 'name': 1, 'level': 1, 'vocation': 1, 'world': 1, 'winningBid': 1, 'auctionEnd': 1, 'axeFighting': 1, 'clubFighting': 1, 'swordFighting': 1, 'fistFighting': 1, 'distanceFighting': 1, 'magicLevel': 1, 'shielding': 1, 'fishing': 1 }, 
            { name: "non_array_fields_index" }
        )
        ```
        
        ```
        db.auctions.createIndexes(
            [
                { 'sold': 1, "mounts": 1, name: 1, level: 1, vocation: 1, world: 1, winningBid: 1, auctionEnd: 1, 'axeFighting': 1, 'clubFighting': 1, 'swordFighting': 1, 'fistFighting': 1, 'distanceFighting': 1, 'magicLevel': 1, 'shielding': 1, 'fishing': 1 },
                { 'sold': 1, "storeMounts": 1, name: 1, level: 1, vocation: 1, world: 1, winningBid: 1, auctionEnd: 1, 'axeFighting': 1, 'clubFighting': 1, 'swordFighting': 1, 'fistFighting': 1, 'distanceFighting': 1, 'magicLevel': 1, 'shielding': 1, 'fishing': 1 },
                { 'sold': 1, "imbuements": 1, name: 1, level: 1, vocation: 1, world: 1, winningBid: 1, auctionEnd: 1, 'axeFighting': 1, 'clubFighting': 1, 'swordFighting': 1, 'fistFighting': 1, 'distanceFighting': 1, 'magicLevel': 1, 'shielding': 1, 'fishing': 1 },
                { 'sold': 1, "completedQuestLines": 1, name: 1, level: 1, vocation: 1, world: 1, winningBid: 1, auctionEnd: 1, 'axeFighting': 1, 'clubFighting': 1, 'swordFighting': 1, 'fistFighting': 1, 'distanceFighting': 1, 'magicLevel': 1, 'shielding': 1, 'fishing': 1 },
                { 'sold': 1, "titles": 1, name: 1, level: 1, vocation: 1, world: 1, winningBid: 1, auctionEnd: 1, 'axeFighting': 1, 'clubFighting': 1, 'swordFighting': 1, 'fistFighting': 1, 'distanceFighting': 1, 'magicLevel': 1, 'shielding': 1, 'fishing': 1 },
                { 'sold': 1, "achievements": 1, name: 1, level: 1, vocation: 1, world: 1, winningBid: 1, auctionEnd: 1, 'axeFighting': 1, 'clubFighting': 1, 'swordFighting': 1, 'fistFighting': 1, 'distanceFighting': 1, 'magicLevel': 1, 'shielding': 1, 'fishing': 1 }
            ]
        )
        ```

### Building the JARs
```
./mvnw clean package -Dmaven.test.skip=true
```

### Running the tests
```
./mvnw clean install test
```

## API Docs

The Open API specification can be seem by copying the contents of the file [api-spec](/api-specs/auction-api-spec.yaml) and pasting into [swagger online editor](https://editor.swagger.io/)

## Disclaimer

This data is scraped from the official Tibia website and provided for educational purposes only.
All rights to the Tibia game and its associated data belong to CipSoft.
