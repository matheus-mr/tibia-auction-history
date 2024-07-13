package com.matheusmr.tibiaauctionhistory.auctionfetch.service;

import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import com.matheusmr.tibiaauctionhistory.common.model.Item;
import com.matheusmr.tibiaauctionhistory.common.model.Outfit;
import com.matheusmr.tibiaauctionhistory.common.model.Vocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

@Service
@Slf4j
public class AuctionFetchValidationServiceImpl implements AuctionFetchValidationService {

    private final AuctionFetchService auctionFetchService;

    @Autowired
    public AuctionFetchValidationServiceImpl(AuctionFetchService auctionFetchService) {
        this.auctionFetchService = auctionFetchService;
    }

    @Override
    public void validateAuctionFetchingCorrectness() {
        try {
            log.info("Validating auction fetching correctness...");
            validateAuctionInErrorState();
            validateCancelledAuction();
            validateFinishedAuctionWhichWasNotSold();
            validateSoldAuction();
            log.info("Finished validating auction fetching correctness");
        } catch (Exception e){
            final String msg = "Error while validating auction fetching";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private void validateAuctionInErrorState() throws IOException {
        log.info("Validating auction in error state...");
        final Auction auction = auctionFetchService.fetchAuction(1);
        checkState(auction.getId() == 1);
        checkState(auction.isInErrorState());
        log.info("Successfully validated auction in error state");
    }

    private void validateCancelledAuction() throws IOException {
        log.info("Validating cancelled auction...");
        final Auction auction = auctionFetchService.fetchAuction(4);
        checkState(auction.getId() == 4);
        checkState(!auction.isInErrorState());
        checkState(!auction.getSold());
        log.info("Successfully validated cancelled auction");
    }

    private void validateFinishedAuctionWhichWasNotSold() throws IOException {
        log.info("Validating finished auction which was not sold...");
        final Auction auction = auctionFetchService.fetchAuction(13);
        checkState(auction.getId() == 13);
        checkState(!auction.isInErrorState());
        checkState(!auction.getSold());
        checkState(auction.getMinimumBid() == 1000);
        log.info("Successfully validated finished auction which was not sold");
    }

    private void validateSoldAuction() throws IOException {
        log.info("Validating sold auction...");
        final Auction auction = auctionFetchService.fetchAuction(1595613);

        checkState(auction.getId() == 1595613);
        checkState(!auction.isInErrorState());

        checkState(auction.getName().equals("Hardy La Guaira"));
        checkState(auction.getLevel() == 1637);
        checkState(auction.getVocation() == Vocation.PALADIN);
        checkState(auction.getWorld().equals("Lobera"));
        checkState(auction.getCreationDate().equals(LocalDateTime.of(2017, 5, 6, 17, 20, 34)));

        checkState(auction.getAuctionStart().isEqual(LocalDateTime.of(2024, 6, 4, 8, 32, 0)));
        checkState(auction.getAuctionEnd().isEqual(LocalDateTime.of(2024, 6, 6, 8, 0, 0)));
        checkState(auction.getMinimumBid() == null);
        checkState(auction.getWinningBid() == 200000);
        checkState(auction.getSold());

        checkState(auction.getMountsAmount() == 25);
        checkState(auction.getOutfitsAmount() == 37);
        checkState(auction.getTitlesAmount() == 23);
        checkState(auction.getAchievementPoints() == 490);
        checkState(auction.getDailyRewardStreak() == 2);

        checkState(auction.getAxeFighting() == 13);
        checkState(auction.getAxeFightingPercentage().equals(new BigDecimal("58.62")));
        checkState(auction.getClubFighting() == 13);
        checkState(auction.getClubFightingPercentage().equals(new BigDecimal("73.56")));
        checkState(auction.getDistanceFighting() == 132);
        checkState(auction.getDistanceFightingPercentage().equals(new BigDecimal("41.62")));
        checkState(auction.getFishing() == 13);
        checkState(auction.getFishingPercentage().equals(new BigDecimal("15.38")));
        checkState(auction.getFistFighting() == 20);
        checkState(auction.getFistFightingPercentage().equals(new BigDecimal("39.67")));
        checkState(auction.getMagicLevel() == 38);
        checkState(auction.getMagicLevelPercentage().equals(new BigDecimal("18.38")));
        checkState(auction.getShielding() == 113);
        checkState(auction.getShieldingPercentage().equals(new BigDecimal("42.79")));
        checkState(auction.getSwordFighting() == 38);
        checkState(auction.getSwordFightingPercentage().equals(new BigDecimal("37.95")));

        checkState(auction.getHasCharmExpansion());
        checkState(auction.getAvailableCharmPoints() == 758);
        checkState(auction.getSpentCharmPoints() == 10400);
        checkState(auction.getCharms().equals(List.of(
                "Adrenaline Burst",
                "Cleanse",
                "Curse",
                "Divine Wrath",
                "Enflame",
                "Freeze",
                "Low Blow",
                "Parry",
                "Poison",
                "Wound",
                "Zap"
        )));

        checkState(auction.getHuntingTaskPoints() == 19093);
        checkState(auction.getPermanentHuntingTaskSlots() == 0);
        checkState(auction.getPermanentPreySlots() == 1);
        checkState(auction.getPreyWildcards() == 20);

        checkState(auction.getHirelings() == 2);
        checkState(auction.getHirelingsJobs() == 4);
        checkState(auction.getHirelingsOutfits() == 0);

        checkState(auction.getMaximumExaltedDust() == 163);
        checkState(auction.getCompletedExaltedDust() == 163);
        checkState(auction.getBossPoints() == 950);

        checkState(auction.getItems().size() == 216);
        checkState(auction.getItems().getFirst().equals(new Item(2, "giant shimmering pearl")));
        checkState(auction.getItems().getLast().equals(new Item(17, "wardragon tooth")));

        checkState(auction.getStoreItems().size() == 43);
        checkState(auction.getStoreItems().getFirst().equals(new Item(5, "soulfire rune")));
        checkState(auction.getStoreItems().getLast().equals(new Item(1, "podium of vigour")));

        checkState(auction.getMounts().size() == 23);
        checkState(auction.getMounts().getFirst().equals("Racing Bird"));
        checkState(auction.getMounts().getLast().equals("Mutated Abomination"));

        checkState(auction.getStoreMounts().size() == 2);
        checkState(auction.getStoreMounts().getFirst().equals("Jade Pincer"));
        checkState(auction.getStoreMounts().getLast().equals("Gloomwurm"));

        checkState(auction.getOutfits().size() == 35);
        checkState(auction.getOutfits().getFirst().equals(new Outfit("Citizen", false, false)));
        checkState(auction.getOutfits().get(1).equals(new Outfit("Hunter", true, true)));
        checkState(auction.getOutfits().get(23).equals(new Outfit("Poltergeist", true, false)));
        checkState(auction.getOutfits().getLast().equals(new Outfit("Wayfarer", false, false)));

        checkState(auction.getStoreOutfits().size() == 2);
        checkState(auction.getStoreOutfits().getFirst().equals(new Outfit("Retro Warrior", false, false)));
        checkState(auction.getStoreOutfits().getLast().equals(new Outfit("Sinister Archer", true, true)));

        checkState(auction.getImbuements().size() == 20);
        checkState(auction.getImbuements().getFirst().equals("Powerful Bash"));
        checkState(auction.getImbuements().getLast().equals("Powerful Void"));

        checkState(auction.getCompletedCyclopediaMapAreas().isEmpty());

        checkState(auction.getCompletedQuestLines().size() == 21);
        checkState(auction.getCompletedQuestLines().getFirst().equals("20 Years a Cook"));
        checkState(auction.getCompletedQuestLines().getLast().equals("Wrath of the Emperor"));

        checkState(auction.getTitles().size() == 23);
        checkState(auction.getTitles().getFirst().equals("Beastly"));
        checkState(auction.getTitles().getLast().equals("Trolltrasher"));

        checkState(auction.getAchievements().size() == 177);
        checkState(auction.getAchievements().getFirst().equals("A Study in Scarlett"));
        checkState(auction.getAchievements().getLast().equals("Zzztill Zzztanding!"));
        log.info("Successfully validated sold auction");
    }
}
