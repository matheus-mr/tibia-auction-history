package com.matheusmr.tibiaauctionhistory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheusmr.tibiaauctionhistory.model.AuctionSearch;
import com.matheusmr.tibiaauctionhistory.model.AuctionSearchCriterion;
import com.matheusmr.tibiaauctionhistory.model.Operator;
import com.tibiaauctionhistory.module.auctionsearch.repository.AuctionSearchRepository;
import com.tibiaauctionhistory.module.common.model.Auction;
import com.tibiaauctionhistory.module.common.model.Item;
import com.tibiaauctionhistory.module.common.model.Outfit;
import com.tibiaauctionhistory.module.common.model.Vocation;
import com.tibiaauctionhistory.module.common.repository.AuctionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@Testcontainers
public class AbstractIntegrationTests {

    protected UUID KNIGHT_AUCTIONS_SEARCH_ID = UUID.randomUUID();

    @Container
    @ServiceConnection
    private final static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:7.0.0")
                    .withExposedPorts(27017);

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private AuctionSearchRepository auctionSearchRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @PostConstruct
    public void postConstruct(){
        insertAuctionsToDatabase();
        insertAuctionSearchesToDatabase();
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .defaultRequest(get("/").contentType(MediaType.APPLICATION_JSON))
                .build();
    }

    private void insertAuctionsToDatabase(){
        insertAuctionInErrorState();
        insertAuctionNotSold();
        insertAuctionCase1();
        insertAuctionCase2();
        insertAuctionCase3();
    }

    private void insertAuctionInErrorState(){
        auctionRepository.save(Auction.builder().id(1).inErrorState(true).build());
    }

    private void insertAuctionNotSold(){
        auctionRepository.save(
                Auction.builder()
                        .id(4)
                        .inErrorState(false)
                        .name("Angus Tanker")
                        .level(561)
                        .vocation(Vocation.KNIGHT)
                        .world("Belobra")
                        .creationDate(LocalDateTime.of(2004, 10, 15, 8, 51, 52))
                        .auctionStart(LocalDateTime.of(2020, 8, 26, 11, 7, 0))
                        .auctionEnd(LocalDateTime.of(2020, 9, 24, 11, 0, 0))
                        .minimumBid(8000)
                        .sold(false)
                        .mountsAmount(27)
                        .outfitsAmount(31)
                        .titlesAmount(11)
                        .achievementPoints(0)
                        .dailyRewardStreak(1)
                        .axeFighting(54)
                        .axeFightingPercentage(new BigDecimal("53.81"))
                        .clubFighting(14)
                        .clubFightingPercentage(new BigDecimal("32.87"))
                        .distanceFighting(17)
                        .distanceFightingPercentage(new BigDecimal("75.31"))
                        .fishing(29)
                        .fishingPercentage(new BigDecimal("15.57"))
                        .fistFighting(14)
                        .fistFightingPercentage(new BigDecimal("13.69"))
                        .magicLevel(11)
                        .magicLevelPercentage(new BigDecimal("61.91"))
                        .shielding(111)
                        .shieldingPercentage(new BigDecimal("77.22"))
                        .swordFighting(117)
                        .swordFightingPercentage(new BigDecimal("26.47"))
                        .hasCharmExpansion(false)
                        .availableCharmPoints(404)
                        .spentCharmPoints(2200)
                        .charms(List.of(
                                "Dodge",
                                "Freeze",
                                "Zap"
                        ))
                        .huntingTaskPoints(0)
                        .permanentHuntingTaskSlots(0)
                        .permanentPreySlots(0)
                        .preyWildcards(25)
                        .hirelings(0)
                        .hirelingsJobs(0)
                        .hirelingsOutfits(0)
                        .maximumExaltedDust(100)
                        .completedExaltedDust(0)
                        .bossPoints(0)
                        .items(List.of(
                                new Item(1, "jewelled backpack")
                        ))
                        .storeItems(List.of(
                                new Item(3, "energy bomb rune"),
                                new Item(10, "convince creature rune"),
                                new Item(2, "chameleon rune"),
                                new Item(7, "magic wall rune"),
                                new Item(5, "ultimate health potion"),
                                new Item(417, "supreme health potion")
                        ))
                        .mounts(List.of(
                                "War Bear",
                                "Midnight Panther",
                                "Draptor",
                                "Titanica",
                                "Blazebringer",
                                "Stampor",
                                "Undead Cavebear",
                                "Donkey",
                                "Tiger Slug",
                                "Crystal Wolf",
                                "War Horse",
                                "Kingly Deer",
                                "Tamed Panda",
                                "Scorpion King",
                                "Manta Ray",
                                "Magma Crawler",
                                "Gnarlhound",
                                "Ursagrodon",
                                "The Hellgrip",
                                "Noble Lion",
                                "Walker",
                                "Sparkion",
                                "Stone Rhino",
                                "Cold Percht Sleigh",
                                "Bright Percht Sleigh",
                                "Dark Percht Sleigh"
                        ))
                        .storeMounts(List.of(
                                "Shadow Draptor"
                        ))
                        .outfits(List.of(
                                new Outfit("Citizen", true, true),
                                new Outfit("Hunter", true, true),
                                new Outfit("Mage", true, false),
                                new Outfit("Knight", true, true),
                                new Outfit("Nobleman", true, true),
                                new Outfit("Summoner", true, false),
                                new Outfit("Warrior", true, true),
                                new Outfit("Barbarian", true, true),
                                new Outfit("Druid", true, true),
                                new Outfit("Wizard", true, true),
                                new Outfit("Oriental", true, true),
                                new Outfit("Pirate", true, true),
                                new Outfit("Assassin", true, true),
                                new Outfit("Beggar", true, true),
                                new Outfit("Shaman", false, false),
                                new Outfit("Norseman", true, true),
                                new Outfit("Jester", true, true),
                                new Outfit("Brotherhood", true, true),
                                new Outfit("Demon Hunter", true, true),
                                new Outfit("Yalaharian", true, false),
                                new Outfit("Warmaster", true, true),
                                new Outfit("Wayfarer", true, true),
                                new Outfit("Afflicted", true, true),
                                new Outfit("Elementalist", false, false),
                                new Outfit("Crystal Warlord", true, true),
                                new Outfit("Soil Guardian", true, true),
                                new Outfit("Demon Outfit", true, false),
                                new Outfit("Battle Mage", false, false),
                                new Outfit("Percht Raider", false, false)
                        ))
                        .storeOutfits(List.of(
                                new Outfit("Champion", true, true),
                                new Outfit("Lupine Warden", true, true)
                        ))
                        .imbuements(List.of(
                                "Powerful Bash",
                                "Powerful Chop",
                                "Powerful Cloud Fabric",
                                "Powerful Demon Presence",
                                "Powerful Dragon Hide",
                                "Powerful Electrify",
                                "Powerful Epiphany",
                                "Powerful Lich Shroud",
                                "Powerful Precision",
                                "Powerful Reap",
                                "Powerful Scorch",
                                "Powerful Slash",
                                "Powerful Snake Skin",
                                "Powerful Strike",
                                "Powerful Swiftness",
                                "Powerful Vampirism",
                                "Powerful Venom",
                                "Powerful Void"
                        ))
                        .completedCyclopediaMapAreas(List.of(
                                "Thais",
                                "Venore"
                        ))
                        .completedQuestLines(List.of(
                                "A Father's Burden",
                                "Blood Brothers",
                                "Children of the Revolution",
                                "Heart of Destruction",
                                "Hero of Rathleton",
                                "Hot Cuisine",
                                "In Service Of Yalahar",
                                "Kissing a Pig",
                                "Shadows of Yalahar",
                                "The Ancient Tombs",
                                "The Ape City",
                                "The Desert Dungeon",
                                "The Djinn War - Marid Faction",
                                "The Gravedigger of Drefia",
                                "The Ice Islands",
                                "The Inquisition",
                                "The New Frontier",
                                "The Outlaw Camp",
                                "The Paradox Tower",
                                "The Pits of Inferno",
                                "The Postman Missions",
                                "The Queen of the Banshees",
                                "The Shattered Isles",
                                "The Thieves Guild",
                                "Twenty Miles Beneath The Sea",
                                "Unnatural Selection",
                                "What a foolish Quest",
                                "Wrath of the Emperor"
                        ))
                        .titles(List.of(
                                "Beastrider (Grade 1)",
                                "Beastrider (Grade 2)",
                                "Creature of Habit (Grade 1)",
                                "Cyclopscamper",
                                "Demondoom",
                                "Dragondouser",
                                "Drakenbane",
                                "Silencer",
                                "Tibia's Topmodel (Grade 1)",
                                "Tibia's Topmodel (Grade 2)",
                                "Trolltrasher"
                        ))
                        .achievements(List.of())
                        .build()
        );
    }

    private void insertAuctionCase1(){
        auctionRepository.save(
                Auction.builder()
                        .id(33)
                        .inErrorState(false)
                        .name("Dead Stalker")
                        .level(378)
                        .vocation(Vocation.KNIGHT)
                        .world("Gladera")
                        .creationDate(LocalDateTime.of(2007, 8, 25, 5, 19, 53))
                        .auctionStart(LocalDateTime.of(2020, 8, 26, 11, 6, 0))
                        .auctionEnd(LocalDateTime.of(2020, 9, 11, 6, 0, 0))
                        .minimumBid(6000)
                        .sold(true)
                        .mountsAmount(9)
                        .outfitsAmount(23)
                        .titlesAmount(8)
                        .achievementPoints(0)
                        .dailyRewardStreak(1)
                        .axeFighting(94)
                        .axeFightingPercentage(new BigDecimal("87.45"))
                        .clubFighting(31)
                        .clubFightingPercentage(new BigDecimal("11.89"))
                        .distanceFighting(26)
                        .distanceFightingPercentage(new BigDecimal("84.64"))
                        .fishing(10)
                        .fishingPercentage(new BigDecimal("0.00"))
                        .fistFighting(15)
                        .fistFightingPercentage(new BigDecimal("85.00"))
                        .magicLevel(10)
                        .magicLevelPercentage(new BigDecimal("80.23"))
                        .shielding(101)
                        .shieldingPercentage(new BigDecimal("18.71"))
                        .swordFighting(105)
                        .swordFightingPercentage(new BigDecimal("9.96"))
                        .hasCharmExpansion(false)
                        .availableCharmPoints(141)
                        .spentCharmPoints(2000)
                        .charms(List.of(
                                "Dodge",
                                "Wound",
                                "Zap"
                        ))
                        .huntingTaskPoints(1599)
                        .permanentHuntingTaskSlots(0)
                        .permanentPreySlots(0)
                        .preyWildcards(0)
                        .hirelings(0)
                        .hirelingsJobs(0)
                        .hirelingsOutfits(0)
                        .maximumExaltedDust(100)
                        .completedExaltedDust(0)
                        .bossPoints(0)
                        .items(List.of(
                                new Item(1, "necromancer shield"),
                                new Item(1, "depth calcei"),
                                new Item(1, "ornate legs"),
                                new Item(1, "gnome sword")
                        ))
                        .storeItems(List.of())
                        .mounts(List.of(
                                "Racing Bird",
                                "Blazebringer",
                                "Donkey",
                                "Gnarlhound",
                                "Sparkion",
                                "Bright Percht Sleigh",
                                "Dark Percht Sleigh",
                                "Dark Percht Sleigh Variant",
                                "Haze"
                        ))
                        .storeMounts(List.of())
                        .outfits(List.of(
                                new Outfit("Citizen", true, true),
                                new Outfit("Hunter", false, false),
                                new Outfit("Mage", true, false),
                                new Outfit("Knight", false, false),
                                new Outfit("Nobleman", true, true),
                                new Outfit("Summoner", false, false),
                                new Outfit("Warrior", true, true),
                                new Outfit("Barbarian", false, false),
                                new Outfit("Druid", false, false),
                                new Outfit("Wizard", false, false),
                                new Outfit("Oriental", false, false),
                                new Outfit("Assassin", true, true),
                                new Outfit("Norseman", true, true),
                                new Outfit("Brotherhood", true, true),
                                new Outfit("Demon Hunter", true, true),
                                new Outfit("Yalaharian", false, false),
                                new Outfit("Warmaster", true, true),
                                new Outfit("Wayfarer", false, false),
                                new Outfit("Afflicted", true, true),
                                new Outfit("Crystal Warlord", true, true),
                                new Outfit("Battle Mage", false, false),
                                new Outfit("Percht Raider", true, false)
                        ))
                        .storeOutfits(List.of(
                                new Outfit("Retro Warrior", false, false)
                        ))
                        .imbuements(List.of(
                                "Powerful Cloud Fabric",
                                "Powerful Electrify",
                                "Powerful Swiftness"
                        ))
                        .completedCyclopediaMapAreas(List.of())
                        .completedQuestLines(List.of(
                                "Children of the Revolution",
                                "In Service Of Yalahar",
                                "Shadows of Yalahar",
                                "The Ape City",
                                "The Beginning",
                                "The Djinn War - Efreet Faction",
                                "The Ice Islands",
                                "The Inquisition",
                                "The New Frontier",
                                "The Pits of Inferno",
                                "The Queen of the Banshees",
                                "Wrath of the Emperor"
                        ))
                        .titles(List.of(
                                "Creature of Habit (Grade 1)",
                                "Creature of Habit (Grade 2)",
                                "Cyclopscamper",
                                "Demondoom",
                                "Dragondouser",
                                "Tibia's Topmodel (Grade 1)",
                                "Tibia's Topmodel (Grade 2)",
                                "Trolltrasher"
                        ))
                        .achievements(List.of(
                                "A Study in Scarlett",
                                "All Hail the King",
                                "Allow Cookies?"
                        ))
                        .build()
        );
    }

    private void insertAuctionCase2(){
        auctionRepository.save(
                Auction.builder()
                        .id(36)
                        .inErrorState(false)
                        .name("Liperaah")
                        .level(309)
                        .vocation(Vocation.KNIGHT)
                        .world("Damora")
                        .creationDate(LocalDateTime.of(2009, 1, 21, 19, 49, 5))
                        .auctionStart(LocalDateTime.of(2020, 8, 26, 11, 6, 0))
                        .auctionEnd(LocalDateTime.of(2020, 8, 30, 11, 0, 0))
                        .minimumBid(3558)
                        .sold(true)
                        .mountsAmount(14)
                        .outfitsAmount(25)
                        .titlesAmount(9)
                        .achievementPoints(0)
                        .dailyRewardStreak(1)
                        .axeFighting(57)
                        .axeFightingPercentage(new BigDecimal("84.19"))
                        .clubFighting(17)
                        .clubFightingPercentage(new BigDecimal("5.15"))
                        .distanceFighting(24)
                        .distanceFightingPercentage(new BigDecimal("57.90"))
                        .fishing(40)
                        .fishingPercentage(new BigDecimal("0.85"))
                        .fistFighting(16)
                        .fistFightingPercentage(new BigDecimal("73.03"))
                        .magicLevel(10)
                        .magicLevelPercentage(new BigDecimal("36.52"))
                        .shielding(106)
                        .shieldingPercentage(new BigDecimal("62.80"))
                        .swordFighting(112)
                        .swordFightingPercentage(new BigDecimal("59.66"))
                        .hasCharmExpansion(false)
                        .availableCharmPoints(415)
                        .spentCharmPoints(0)
                        .charms(List.of())
                        .huntingTaskPoints(0)
                        .permanentHuntingTaskSlots(0)
                        .permanentPreySlots(0)
                        .preyWildcards(16)
                        .hirelings(0)
                        .hirelingsJobs(0)
                        .hirelingsOutfits(0)
                        .maximumExaltedDust(100)
                        .completedExaltedDust(0)
                        .bossPoints(0)
                        .items(List.of(
                                new Item(1, "giant shimmering pearl"),
                                new Item(100, "flash arrow"),
                                new Item(1, "glacier amulet"),
                                new Item(9, "bait"),
                                new Item(1, "bamboo drawer"),
                                new Item(5, "box")
                        ))
                        .storeItems(List.of(
                                new Item(15, "ultimate health potion"),
                                new Item(177, "supreme health potion")
                        ))
                        .mounts(List.of(
                                "Widow Queen",
                                "War Bear",
                                "Draptor",
                                "Tiger Slug"
                        ))
                        .storeMounts(List.of())
                        .outfits(List.of(
                                new Outfit("Citizen", true, true),
                                new Outfit("Hunter", true, true),
                                new Outfit("Mage", true, false)
                        ))
                        .storeOutfits(List.of())
                        .imbuements(List.of(
                                "Powerful Cloud Fabric",
                                "Powerful Dragon Hide",
                                "Powerful Electrify",
                                "Powerful Lich Shroud",
                                "Powerful Reap",
                                "Powerful Scorch"
                        ))
                        .completedCyclopediaMapAreas(List.of())
                        .completedQuestLines(List.of(
                                "A Father's Burden",
                                "Blood Brothers",
                                "Children of the Revolution"
                        ))
                        .titles(List.of(
                                "Beastrider (Grade 1)",
                                "Creature of Habit (Grade 1)",
                                "Creature of Habit (Grade 2)",
                                "Cyclopscamper",
                                "Demondoom"
                        ))
                        .achievements(List.of())
                        .build()
        );
    }

    private void insertAuctionCase3(){
        auctionRepository.save(
                Auction.builder()
                        .id(23)
                        .inErrorState(false)
                        .name("Aj Dzi")
                        .level(110)
                        .vocation(Vocation.SORCERER)
                        .world("Damora")
                        .creationDate(LocalDateTime.of(2019, 1, 24, 12, 29, 7))
                        .auctionStart(LocalDateTime.of(2020, 8, 26, 11, 5, 0))
                        .auctionEnd(LocalDateTime.of(2020, 8, 27, 11, 0, 0))
                        .winningBid(521)
                        .sold(true)
                        .mountsAmount(1)
                        .outfitsAmount(11)
                        .titlesAmount(4)
                        .achievementPoints(0)
                        .dailyRewardStreak(1)
                        .axeFighting(12)
                        .axeFightingPercentage(new BigDecimal("52.50"))
                        .clubFighting(12)
                        .clubFightingPercentage(new BigDecimal("52.50"))
                        .distanceFighting(12)
                        .distanceFightingPercentage(new BigDecimal("53.33"))
                        .fishing(10)
                        .fishingPercentage(new BigDecimal("0.00"))
                        .fistFighting(10)
                        .fistFightingPercentage(new BigDecimal("68.00"))
                        .magicLevel(68)
                        .magicLevelPercentage(new BigDecimal("75.63"))
                        .shielding(33)
                        .shieldingPercentage(new BigDecimal("18.56"))
                        .swordFighting(12)
                        .swordFightingPercentage(new BigDecimal("58.50"))
                        .hasCharmExpansion(false)
                        .availableCharmPoints(185)
                        .spentCharmPoints(0)
                        .charms(List.of())
                        .huntingTaskPoints(0)
                        .permanentHuntingTaskSlots(0)
                        .permanentPreySlots(0)
                        .preyWildcards(19)
                        .hirelings(0)
                        .hirelingsJobs(0)
                        .hirelingsOutfits(0)
                        .maximumExaltedDust(100)
                        .completedExaltedDust(0)
                        .bossPoints(0)
                        .items(List.of(
                                new Item(2, "empty potion flask"),
                                new Item(1, "piece of paper"),
                                new Item(1, "magma monocle")
                        ))
                        .storeItems(List.of(
                                new Item(17, "strong mana potion"),
                                new Item(8, "great mana potion"),
                                new Item(20, "sudden death rune"),
                                new Item(170, "ultimate mana potion")
                        ))
                        .mounts(List.of(
                                "Crystal Wolf"
                        ))
                        .storeMounts(List.of())
                        .outfits(List.of(
                                new Outfit("Citizen", true, false)
                        ))
                        .storeOutfits(List.of())
                        .imbuements(List.of())
                        .completedCyclopediaMapAreas(List.of())
                        .completedQuestLines(List.of(
                                "Dawnport",
                                "The Djinn War - Marid Faction"
                        ))
                        .titles(List.of(
                                "Creature of Habit (Grade 1)",
                                "Cyclopscamper",
                                "Tibia's Topmodel (Grade 1)",
                                "Trolltrasher"
                        ))
                        .achievements(List.of())
                        .build()
        );
    }

    private void insertAuctionSearchesToDatabase(){
        auctionSearchRepository.save(
                new AuctionSearch(
                        KNIGHT_AUCTIONS_SEARCH_ID,
                        new AuctionSearchCriterion(
                                "vocation",
                                Operator.EQUALS,
                                List.of("KNIGHT"),
                                null
                        ),
                        null
                )
        );
    }
}
