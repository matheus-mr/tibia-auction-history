package com.tibiaauctionhistory.service.auctionsearch.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tibiaauctionhistory.module.auctiondomain.model.AuctionDomainDTO;
import com.tibiaauctionhistory.module.common.model.Vocation;
import com.tibiaauctionhistory.service.auctionsearch.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAuctionDomainController extends AbstractIntegrationTests {

    @Test
    public void testFetchingDomain() throws Exception {
        final String response = mockMvc.perform(get("/api/v1/auctions/domain"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final AuctionDomainDTO auctionDomainDTO = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(5, auctionDomainDTO.getVocations().size());
        assertEquals(Vocation.NONE.name(), auctionDomainDTO.getVocations().get(0));
        assertEquals(Vocation.KNIGHT.name(), auctionDomainDTO.getVocations().get(1));
        assertEquals(Vocation.PALADIN.name(), auctionDomainDTO.getVocations().get(2));
        assertEquals(Vocation.SORCERER.name(), auctionDomainDTO.getVocations().get(3));
        assertEquals(Vocation.DRUID.name(), auctionDomainDTO.getVocations().get(4));

        assertEquals(3, auctionDomainDTO.getWorlds().size());
        assertEquals("Belobra", auctionDomainDTO.getWorlds().get(0));
        assertEquals("Damora", auctionDomainDTO.getWorlds().get(1));
        assertEquals("Gladera", auctionDomainDTO.getWorlds().get(2));

        assertEquals(4, auctionDomainDTO.getCharms().size());
        assertEquals("Dodge", auctionDomainDTO.getCharms().get(0));
        assertEquals("Freeze", auctionDomainDTO.getCharms().get(1));
        assertEquals("Wound", auctionDomainDTO.getCharms().get(2));
        assertEquals("Zap", auctionDomainDTO.getCharms().get(3));

        assertEquals(14, auctionDomainDTO.getItems().size());
        assertEquals("bait", auctionDomainDTO.getItems().get(0));
        assertEquals("bamboo drawer", auctionDomainDTO.getItems().get(1));
        assertEquals("box", auctionDomainDTO.getItems().get(2));
        assertEquals("depth calcei", auctionDomainDTO.getItems().get(3));
        assertEquals("empty potion flask", auctionDomainDTO.getItems().get(4));
        assertEquals("flash arrow", auctionDomainDTO.getItems().get(5));
        assertEquals("giant shimmering pearl", auctionDomainDTO.getItems().get(6));
        assertEquals("glacier amulet", auctionDomainDTO.getItems().get(7));
        assertEquals("gnome sword", auctionDomainDTO.getItems().get(8));
        assertEquals("jewelled backpack", auctionDomainDTO.getItems().get(9));
        assertEquals("magma monocle", auctionDomainDTO.getItems().get(10));
        assertEquals("necromancer shield", auctionDomainDTO.getItems().get(11));
        assertEquals("ornate legs", auctionDomainDTO.getItems().get(12));
        assertEquals("piece of paper", auctionDomainDTO.getItems().get(13));

        assertEquals(10, auctionDomainDTO.getStoreItems().size());
        assertEquals("chameleon rune", auctionDomainDTO.getStoreItems().get(0));
        assertEquals("convince creature rune", auctionDomainDTO.getStoreItems().get(1));
        assertEquals("energy bomb rune", auctionDomainDTO.getStoreItems().get(2));
        assertEquals("great mana potion", auctionDomainDTO.getStoreItems().get(3));
        assertEquals("magic wall rune", auctionDomainDTO.getStoreItems().get(4));
        assertEquals("strong mana potion", auctionDomainDTO.getStoreItems().get(5));
        assertEquals("sudden death rune", auctionDomainDTO.getStoreItems().get(6));
        assertEquals("supreme health potion", auctionDomainDTO.getStoreItems().get(7));
        assertEquals("ultimate health potion", auctionDomainDTO.getStoreItems().get(8));
        assertEquals("ultimate mana potion", auctionDomainDTO.getStoreItems().get(9));

        assertEquals(30, auctionDomainDTO.getMounts().size());
        assertEquals("Blazebringer", auctionDomainDTO.getMounts().get(0));
        assertEquals("Bright Percht Sleigh", auctionDomainDTO.getMounts().get(1));
        assertEquals("Cold Percht Sleigh", auctionDomainDTO.getMounts().get(2));
        assertEquals("Crystal Wolf", auctionDomainDTO.getMounts().get(3));
        assertEquals("Dark Percht Sleigh", auctionDomainDTO.getMounts().get(4));
        assertEquals("Dark Percht Sleigh Variant", auctionDomainDTO.getMounts().get(5));
        assertEquals("Donkey", auctionDomainDTO.getMounts().get(6));
        assertEquals("Draptor", auctionDomainDTO.getMounts().get(7));
        assertEquals("Gnarlhound", auctionDomainDTO.getMounts().get(8));
        assertEquals("Haze", auctionDomainDTO.getMounts().get(9));
        assertEquals("Kingly Deer", auctionDomainDTO.getMounts().get(10));
        assertEquals("Magma Crawler", auctionDomainDTO.getMounts().get(11));
        assertEquals("Manta Ray", auctionDomainDTO.getMounts().get(12));
        assertEquals("Midnight Panther", auctionDomainDTO.getMounts().get(13));
        assertEquals("Noble Lion", auctionDomainDTO.getMounts().get(14));
        assertEquals("Racing Bird", auctionDomainDTO.getMounts().get(15));
        assertEquals("Scorpion King", auctionDomainDTO.getMounts().get(16));
        assertEquals("Sparkion", auctionDomainDTO.getMounts().get(17));
        assertEquals("Stampor", auctionDomainDTO.getMounts().get(18));
        assertEquals("Stone Rhino", auctionDomainDTO.getMounts().get(19));
        assertEquals("Tamed Panda", auctionDomainDTO.getMounts().get(20));
        assertEquals("The Hellgrip", auctionDomainDTO.getMounts().get(21));
        assertEquals("Tiger Slug", auctionDomainDTO.getMounts().get(22));
        assertEquals("Titanica", auctionDomainDTO.getMounts().get(23));
        assertEquals("Undead Cavebear", auctionDomainDTO.getMounts().get(24));
        assertEquals("Ursagrodon", auctionDomainDTO.getMounts().get(25));
        assertEquals("Walker", auctionDomainDTO.getMounts().get(26));
        assertEquals("War Bear", auctionDomainDTO.getMounts().get(27));
        assertEquals("War Horse", auctionDomainDTO.getMounts().get(28));
        assertEquals("Widow Queen", auctionDomainDTO.getMounts().get(29));

        assertEquals(1, auctionDomainDTO.getStoreMounts().size());
        assertEquals("Shadow Draptor", auctionDomainDTO.getStoreMounts().get(0));

        assertEquals(29, auctionDomainDTO.getOutfits().size());
        assertEquals("Afflicted", auctionDomainDTO.getOutfits().get(0));
        assertEquals("Assassin", auctionDomainDTO.getOutfits().get(1));
        assertEquals("Barbarian", auctionDomainDTO.getOutfits().get(2));
        assertEquals("Battle Mage", auctionDomainDTO.getOutfits().get(3));
        assertEquals("Beggar", auctionDomainDTO.getOutfits().get(4));
        assertEquals("Brotherhood", auctionDomainDTO.getOutfits().get(5));
        assertEquals("Citizen", auctionDomainDTO.getOutfits().get(6));
        assertEquals("Crystal Warlord", auctionDomainDTO.getOutfits().get(7));
        assertEquals("Demon Hunter", auctionDomainDTO.getOutfits().get(8));
        assertEquals("Demon Outfit", auctionDomainDTO.getOutfits().get(9));
        assertEquals("Druid", auctionDomainDTO.getOutfits().get(10));
        assertEquals("Elementalist", auctionDomainDTO.getOutfits().get(11));
        assertEquals("Hunter", auctionDomainDTO.getOutfits().get(12));
        assertEquals("Jester", auctionDomainDTO.getOutfits().get(13));
        assertEquals("Knight", auctionDomainDTO.getOutfits().get(14));
        assertEquals("Mage", auctionDomainDTO.getOutfits().get(15));
        assertEquals("Nobleman", auctionDomainDTO.getOutfits().get(16));
        assertEquals("Norseman", auctionDomainDTO.getOutfits().get(17));
        assertEquals("Oriental", auctionDomainDTO.getOutfits().get(18));
        assertEquals("Percht Raider", auctionDomainDTO.getOutfits().get(19));
        assertEquals("Pirate", auctionDomainDTO.getOutfits().get(20));
        assertEquals("Shaman", auctionDomainDTO.getOutfits().get(21));
        assertEquals("Soil Guardian", auctionDomainDTO.getOutfits().get(22));
        assertEquals("Summoner", auctionDomainDTO.getOutfits().get(23));
        assertEquals("Warmaster", auctionDomainDTO.getOutfits().get(24));
        assertEquals("Warrior", auctionDomainDTO.getOutfits().get(25));
        assertEquals("Wayfarer", auctionDomainDTO.getOutfits().get(26));
        assertEquals("Wizard", auctionDomainDTO.getOutfits().get(27));
        assertEquals("Yalaharian", auctionDomainDTO.getOutfits().get(28));

        assertEquals(3, auctionDomainDTO.getStoreOutfits().size());
        assertEquals("Champion", auctionDomainDTO.getStoreOutfits().get(0));
        assertEquals("Lupine Warden", auctionDomainDTO.getStoreOutfits().get(1));
        assertEquals("Retro Warrior", auctionDomainDTO.getStoreOutfits().get(2));

        assertEquals(18, auctionDomainDTO.getImbuements().size());
        assertEquals("Powerful Bash", auctionDomainDTO.getImbuements().get(0));
        assertEquals("Powerful Chop", auctionDomainDTO.getImbuements().get(1));
        assertEquals("Powerful Cloud Fabric", auctionDomainDTO.getImbuements().get(2));
        assertEquals("Powerful Demon Presence", auctionDomainDTO.getImbuements().get(3));
        assertEquals("Powerful Dragon Hide", auctionDomainDTO.getImbuements().get(4));
        assertEquals("Powerful Electrify", auctionDomainDTO.getImbuements().get(5));
        assertEquals("Powerful Epiphany", auctionDomainDTO.getImbuements().get(6));
        assertEquals("Powerful Lich Shroud", auctionDomainDTO.getImbuements().get(7));
        assertEquals("Powerful Precision", auctionDomainDTO.getImbuements().get(8));
        assertEquals("Powerful Reap", auctionDomainDTO.getImbuements().get(9));
        assertEquals("Powerful Scorch", auctionDomainDTO.getImbuements().get(10));
        assertEquals("Powerful Slash", auctionDomainDTO.getImbuements().get(11));
        assertEquals("Powerful Snake Skin", auctionDomainDTO.getImbuements().get(12));
        assertEquals("Powerful Strike", auctionDomainDTO.getImbuements().get(13));
        assertEquals("Powerful Swiftness", auctionDomainDTO.getImbuements().get(14));
        assertEquals("Powerful Vampirism", auctionDomainDTO.getImbuements().get(15));
        assertEquals("Powerful Venom", auctionDomainDTO.getImbuements().get(16));
        assertEquals("Powerful Void", auctionDomainDTO.getImbuements().get(17));

        assertEquals(31, auctionDomainDTO.getCompletedQuestLines().size());
        assertEquals("A Father's Burden", auctionDomainDTO.getCompletedQuestLines().get(0));
        assertEquals("Blood Brothers", auctionDomainDTO.getCompletedQuestLines().get(1));
        assertEquals("Children of the Revolution", auctionDomainDTO.getCompletedQuestLines().get(2));
        assertEquals("Dawnport", auctionDomainDTO.getCompletedQuestLines().get(3));
        assertEquals("Heart of Destruction", auctionDomainDTO.getCompletedQuestLines().get(4));
        assertEquals("Hero of Rathleton", auctionDomainDTO.getCompletedQuestLines().get(5));
        assertEquals("Hot Cuisine", auctionDomainDTO.getCompletedQuestLines().get(6));
        assertEquals("In Service Of Yalahar", auctionDomainDTO.getCompletedQuestLines().get(7));
        assertEquals("Kissing a Pig", auctionDomainDTO.getCompletedQuestLines().get(8));
        assertEquals("Shadows of Yalahar", auctionDomainDTO.getCompletedQuestLines().get(9));
        assertEquals("The Ancient Tombs", auctionDomainDTO.getCompletedQuestLines().get(10));
        assertEquals("The Ape City", auctionDomainDTO.getCompletedQuestLines().get(11));
        assertEquals("The Beginning", auctionDomainDTO.getCompletedQuestLines().get(12));
        assertEquals("The Desert Dungeon", auctionDomainDTO.getCompletedQuestLines().get(13));
        assertEquals("The Djinn War - Efreet Faction", auctionDomainDTO.getCompletedQuestLines().get(14));
        assertEquals("The Djinn War - Marid Faction", auctionDomainDTO.getCompletedQuestLines().get(15));
        assertEquals("The Gravedigger of Drefia", auctionDomainDTO.getCompletedQuestLines().get(16));
        assertEquals("The Ice Islands", auctionDomainDTO.getCompletedQuestLines().get(17));
        assertEquals("The Inquisition", auctionDomainDTO.getCompletedQuestLines().get(18));
        assertEquals("The New Frontier", auctionDomainDTO.getCompletedQuestLines().get(19));
        assertEquals("The Outlaw Camp", auctionDomainDTO.getCompletedQuestLines().get(20));
        assertEquals("The Paradox Tower", auctionDomainDTO.getCompletedQuestLines().get(21));
        assertEquals("The Pits of Inferno", auctionDomainDTO.getCompletedQuestLines().get(22));
        assertEquals("The Postman Missions", auctionDomainDTO.getCompletedQuestLines().get(23));
        assertEquals("The Queen of the Banshees", auctionDomainDTO.getCompletedQuestLines().get(24));
        assertEquals("The Shattered Isles", auctionDomainDTO.getCompletedQuestLines().get(25));
        assertEquals("The Thieves Guild", auctionDomainDTO.getCompletedQuestLines().get(26));
        assertEquals("Twenty Miles Beneath The Sea", auctionDomainDTO.getCompletedQuestLines().get(27));
        assertEquals("Unnatural Selection", auctionDomainDTO.getCompletedQuestLines().get(28));
        assertEquals("What a foolish Quest", auctionDomainDTO.getCompletedQuestLines().get(29));
        assertEquals("Wrath of the Emperor", auctionDomainDTO.getCompletedQuestLines().get(30));

        assertEquals(12, auctionDomainDTO.getTitles().size());
        assertEquals("Beastrider (Grade 1)", auctionDomainDTO.getTitles().get(0));
        assertEquals("Beastrider (Grade 2)", auctionDomainDTO.getTitles().get(1));
        assertEquals("Creature of Habit (Grade 1)", auctionDomainDTO.getTitles().get(2));
        assertEquals("Creature of Habit (Grade 2)", auctionDomainDTO.getTitles().get(3));
        assertEquals("Cyclopscamper", auctionDomainDTO.getTitles().get(4));
        assertEquals("Demondoom", auctionDomainDTO.getTitles().get(5));
        assertEquals("Dragondouser", auctionDomainDTO.getTitles().get(6));
        assertEquals("Drakenbane", auctionDomainDTO.getTitles().get(7));
        assertEquals("Silencer", auctionDomainDTO.getTitles().get(8));
        assertEquals("Tibia's Topmodel (Grade 1)", auctionDomainDTO.getTitles().get(9));
        assertEquals("Tibia's Topmodel (Grade 2)", auctionDomainDTO.getTitles().get(10));
        assertEquals("Trolltrasher", auctionDomainDTO.getTitles().get(11));

        assertEquals(3, auctionDomainDTO.getAchievements().size());
        assertEquals("A Study in Scarlett", auctionDomainDTO.getAchievements().get(0));
        assertEquals("All Hail the King", auctionDomainDTO.getAchievements().get(1));
        assertEquals("Allow Cookies?", auctionDomainDTO.getAchievements().get(2));
    }
}
