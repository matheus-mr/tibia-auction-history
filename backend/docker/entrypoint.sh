docker run --rm \
        -e SPRING_PROFILES_ACTIVE=AUCTION_FETCHING \
        -e SPRING_DATA_MONGODB_URI="mongodb+srv://admin:urBuNvJCY3fWUe93@tibia-auction-history.ejzisul.mongodb.net/?retryWrites=true&w=majority&appName=tibia-auction-history"\
        -e TZ=UTC \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENPAGINATEDCONTENTREQUESTS=1 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENAUCTIONFETCH=1 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITAFTERRATELIMIT=61 \
        --entrypoint /bin/bash \
        tibia-auction-history \
        -c "java -jar /app/app.jar AUCTION_ID_TO_START=1 AUCTION_ID_TO_END=100000"



sudo docker run --rm -d --name=tibia-auction-history \
        -e SPRING_PROFILES_ACTIVE=AUCTION_FETCHING \
        -e SPRING_DATA_MONGODB_URI="mongodb+srv://admin:urBuNvJCY3fWUe93@tibia-auction-history.ejzisul.mongodb.net/?ssl=true&retryWrites=true&w=majority&appName=tibia-auction-history"\
        -e TZ=UTC \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENPAGINATEDCONTENTREQUESTS=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENAUCTIONFETCH=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITAFTERRATELIMIT=61 \
        --entrypoint /bin/bash \
        matheusmr95/tibia-auction-history:1.0.0 \
        -c "java -jar /app/app.jar AUCTION_ID_TO_START=1 AUCTION_ID_TO_END=100000"

sudo docker run --rm -d --name=tibia-auction-history \
        -e SPRING_PROFILES_ACTIVE=AUCTION_FETCHING \
        -e SPRING_DATA_MONGODB_URI="mongodb+srv://admin:urBuNvJCY3fWUe93@tibia-auction-history.ejzisul.mongodb.net/?ssl=true&retryWrites=true&w=majority&appName=tibia-auction-history"\
        -e TZ=UTC \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENPAGINATEDCONTENTREQUESTS=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENAUCTIONFETCH=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITAFTERRATELIMIT=61 \
        --entrypoint /bin/bash \
        matheusmr95/tibia-auction-history:1.0.0 \
        -c "java -jar /app/app.jar AUCTION_ID_TO_START=100001 AUCTION_ID_TO_END=200000"

sudo docker run --rm -d --name=tibia-auction-history \
        -e SPRING_PROFILES_ACTIVE=AUCTION_FETCHING \
        -e SPRING_DATA_MONGODB_URI="mongodb+srv://admin:urBuNvJCY3fWUe93@tibia-auction-history.ejzisul.mongodb.net/?ssl=true&retryWrites=true&w=majority&appName=tibia-auction-history"\
        -e TZ=UTC \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENPAGINATEDCONTENTREQUESTS=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENAUCTIONFETCH=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITAFTERRATELIMIT=61 \
        --entrypoint /bin/bash \
        matheusmr95/tibia-auction-history:1.0.0 \
        -c "java -jar /app/app.jar AUCTION_ID_TO_START=200001 AUCTION_ID_TO_END=300000"

sudo docker run --rm -d --name=tibia-auction-history \
        -e SPRING_PROFILES_ACTIVE=AUCTION_FETCHING \
        -e SPRING_DATA_MONGODB_URI="mongodb+srv://admin:urBuNvJCY3fWUe93@tibia-auction-history.ejzisul.mongodb.net/?ssl=true&retryWrites=true&w=majority&appName=tibia-auction-history"\
        -e TZ=UTC \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENPAGINATEDCONTENTREQUESTS=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENAUCTIONFETCH=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITAFTERRATELIMIT=61 \
        --entrypoint /bin/bash \
        matheusmr95/tibia-auction-history:1.0.0 \
        -c "java -jar /app/app.jar AUCTION_ID_TO_START=300001 AUCTION_ID_TO_END=400000"

sudo docker run --rm -d --name=tibia-auction-history \
        -e SPRING_PROFILES_ACTIVE=AUCTION_FETCHING \
        -e SPRING_DATA_MONGODB_URI="mongodb+srv://admin:urBuNvJCY3fWUe93@tibia-auction-history.ejzisul.mongodb.net/?ssl=true&retryWrites=true&w=majority&appName=tibia-auction-history"\
        -e TZ=UTC \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENPAGINATEDCONTENTREQUESTS=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENAUCTIONFETCH=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITAFTERRATELIMIT=61 \
        --entrypoint /bin/bash \
        matheusmr95/tibia-auction-history:1.0.0 \
        -c "java -jar /app/app.jar AUCTION_ID_TO_START=400001 AUCTION_ID_TO_END=500000"

sudo docker run --rm -d --name=tibia-auction-history \
        -e SPRING_PROFILES_ACTIVE=AUCTION_FETCHING \
        -e SPRING_DATA_MONGODB_URI="mongodb+srv://admin:urBuNvJCY3fWUe93@tibia-auction-history.ejzisul.mongodb.net/?ssl=true&retryWrites=true&w=majority&appName=tibia-auction-history"\
        -e TZ=UTC \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENPAGINATEDCONTENTREQUESTS=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITBETWEENAUCTIONFETCH=0 \
        -e TIBIAAUCTIONHISTORY.SECONDSTOWAITAFTERRATELIMIT=61 \
        --entrypoint /bin/bash \
        matheusmr95/tibia-auction-history:1.0.0 \
        -c "java -jar /app/app.jar AUCTION_ID_TO_START=500001 AUCTION_ID_TO_END=600000"

sudo docker logs -f tibia-auction-history