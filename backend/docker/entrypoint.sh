docker run --rm \
        -p 8000:8000 \
        -e SPRING_PROFILES_ACTIVE=AUCTION_FETCHING \
        -e SPRING_DATA_MONGODB_URI="mongodb+srv://admin:urBuNvJCY3fWUe93@tibia-auction-history.ejzisul.mongodb.net/?retryWrites=true&w=majority&appName=tibia-auction-history"\
        -e TZ=UTC \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENPAGINATEDCONTENTREQUESTS=1 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENAUCTIONFETCH=1 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITAFTERRATELIMIT=61 \
        --entrypoint /bin/bash \
        tibia-auction-history \
        -c "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000 -jar /app/app.jar AUCTION_ID_TO_START=1 AUCTION_ID_TO_END=100000"