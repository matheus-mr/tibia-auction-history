package com.matheusmr.tibiaauctionhistory.common.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "auctions")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Auction {

    @Id
    Integer id;

    boolean isInErrorState;

    String name;
    Integer level;
    Vocation vocation;
    String world;
    LocalDateTime creationDate;

    LocalDateTime auctionStart;
    LocalDateTime auctionEnd;
    Integer minimumBid;
    Integer winningBid;
    Boolean sold;

    Integer mountsAmount;
    Integer outfitsAmount;
    Integer titlesAmount;
    Integer achievementPoints;
    Integer dailyRewardStreak;

    Integer axeFighting;
    BigDecimal axeFightingPercentage;
    Integer clubFighting;
    BigDecimal clubFightingPercentage;
    Integer distanceFighting;
    BigDecimal distanceFightingPercentage;
    Integer fishing;
    BigDecimal fishingPercentage;
    Integer fistFighting;
    BigDecimal fistFightingPercentage;
    Integer magicLevel;
    BigDecimal magicLevelPercentage;
    Integer shielding;
    BigDecimal shieldingPercentage;
    Integer swordFighting;
    BigDecimal swordFightingPercentage;

    Boolean hasCharmExpansion;
    Integer availableCharmPoints;
    Integer spentCharmPoints;
    List<String> charms;

    Integer huntingTaskPoints;
    Integer permanentHuntingTaskSlots;
    Integer permanentPreySlots;
    Integer preyWildcards;

    Integer hirelings;
    Integer hirelingsJobs;
    Integer hirelingsOutfits;

    Integer maximumExaltedDust;
    Integer completedExaltedDust;
    Integer bossPoints;

    List<Item> items;
    List<Item> storeItems;

    List<String> mounts;
    List<String> storeMounts;

    List<Outfit> outfits;
    List<Outfit> storeOutfits;

    List<String> imbuements;
    List<String> completedCyclopediaMapAreas;
    List<String> completedQuestLines;
    List<String> titles;
    List<String> achievements;
}
