package com.matheusmr.tibiaauctionhistory.common.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.matheusmr.tibiaauctionhistory.AbstractIntegrationTests;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDTO;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDomainDTO;
import com.matheusmr.tibiaauctionhistory.common.model.Vocation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAuctionController extends AbstractIntegrationTests {

    @Test
    public void testFetchingAllAuctions() throws Exception {
        final String response = mockMvc.perform(get("/api/v1/auctions").queryParam("sortBy","name"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final List<AuctionDTO> auctions = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(3, auctions.size());
        assertEquals(36, auctions.get(0).id());
        assertEquals(33, auctions.get(1).id());
        assertEquals(23, auctions.get(2).id());
    }

    @Test
    public void testFetchingDomain() throws Exception {
        final String response = mockMvc.perform(get("/api/v1/auctions/domain"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final AuctionDomainDTO auctionDomainDTO = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(4, auctionDomainDTO.getVocations().size());
        assertEquals(Vocation.KNIGHT.name(), auctionDomainDTO.getVocations().get(0));
        assertEquals(Vocation.PALADIN.name(), auctionDomainDTO.getVocations().get(1));
        assertEquals(Vocation.SORCERER.name(), auctionDomainDTO.getVocations().get(2));
        assertEquals(Vocation.DRUID.name(), auctionDomainDTO.getVocations().get(3));

        assertEquals(2, auctionDomainDTO.getWorlds().size());
        assertEquals("Damora", auctionDomainDTO.getWorlds().get(0));
        assertEquals("Gladera", auctionDomainDTO.getWorlds().get(1));

        assertEquals(3, auctionDomainDTO.getCharms().size());
        assertEquals("Dodge", auctionDomainDTO.getCharms().get(0));
        assertEquals("Wound", auctionDomainDTO.getCharms().get(1));
        assertEquals("Zap", auctionDomainDTO.getCharms().get(2));

        assertEquals(13, auctionDomainDTO.getItems().size());
        assertEquals("bait", auctionDomainDTO.getItems().get(0));
        assertEquals("bamboo drawer", auctionDomainDTO.getItems().get(1));
        assertEquals("box", auctionDomainDTO.getItems().get(2));
        assertEquals("depth calcei", auctionDomainDTO.getItems().get(3));
        assertEquals("empty potion flask", auctionDomainDTO.getItems().get(4));
        assertEquals("flash arrow", auctionDomainDTO.getItems().get(5));
        assertEquals("giant shimmering pearl", auctionDomainDTO.getItems().get(6));
        assertEquals("glacier amulet", auctionDomainDTO.getItems().get(7));
        assertEquals("gnome sword", auctionDomainDTO.getItems().get(8));
        assertEquals("magma monocle", auctionDomainDTO.getItems().get(9));
        assertEquals("necromancer shield", auctionDomainDTO.getItems().get(10));
        assertEquals("ornate legs", auctionDomainDTO.getItems().get(11));
        assertEquals("piece of paper", auctionDomainDTO.getItems().get(12));

        assertEquals(6, auctionDomainDTO.getStoreItems().size());
        assertEquals("great mana potion", auctionDomainDTO.getStoreItems().get(0));
        assertEquals("strong mana potion", auctionDomainDTO.getStoreItems().get(1));
        assertEquals("sudden death rune", auctionDomainDTO.getStoreItems().get(2));
        assertEquals("supreme health potion", auctionDomainDTO.getStoreItems().get(3));
        assertEquals("ultimate health potion", auctionDomainDTO.getStoreItems().get(4));
        assertEquals("ultimate mana potion", auctionDomainDTO.getStoreItems().get(5));

        assertEquals(14, auctionDomainDTO.getMounts().size());
        assertEquals("Blazebringer", auctionDomainDTO.getMounts().get(0));
        assertEquals("Bright Percht Sleigh", auctionDomainDTO.getMounts().get(1));
        assertEquals("Crystal Wolf", auctionDomainDTO.getMounts().get(2));
        assertEquals("Dark Percht Sleigh", auctionDomainDTO.getMounts().get(3));
        assertEquals("Dark Percht Sleigh Variant", auctionDomainDTO.getMounts().get(4));
        assertEquals("Donkey", auctionDomainDTO.getMounts().get(5));
        assertEquals("Draptor", auctionDomainDTO.getMounts().get(6));
        assertEquals("Gnarlhound", auctionDomainDTO.getMounts().get(7));
        assertEquals("Haze", auctionDomainDTO.getMounts().get(8));
        assertEquals("Racing Bird", auctionDomainDTO.getMounts().get(9));
        assertEquals("Sparkion", auctionDomainDTO.getMounts().get(10));
        assertEquals("Tiger Slug", auctionDomainDTO.getMounts().get(11));
        assertEquals("War Bear", auctionDomainDTO.getMounts().get(12));
        assertEquals("Widow Queen", auctionDomainDTO.getMounts().get(13));

        assertEquals(0, auctionDomainDTO.getStoreMounts().size());

        assertEquals(22, auctionDomainDTO.getOutfits().size());
        assertEquals("Afflicted", auctionDomainDTO.getOutfits().get(0));
        assertEquals("Assassin", auctionDomainDTO.getOutfits().get(1));
        assertEquals("Barbarian", auctionDomainDTO.getOutfits().get(2));
        assertEquals("Battle Mage", auctionDomainDTO.getOutfits().get(3));
        assertEquals("Brotherhood", auctionDomainDTO.getOutfits().get(4));
        assertEquals("Citizen", auctionDomainDTO.getOutfits().get(5));
        assertEquals("Crystal Warlord", auctionDomainDTO.getOutfits().get(6));
        assertEquals("Demon Hunter", auctionDomainDTO.getOutfits().get(7));
        assertEquals("Druid", auctionDomainDTO.getOutfits().get(8));
        assertEquals("Hunter", auctionDomainDTO.getOutfits().get(9));
        assertEquals("Knight", auctionDomainDTO.getOutfits().get(10));
        assertEquals("Mage", auctionDomainDTO.getOutfits().get(11));
        assertEquals("Nobleman", auctionDomainDTO.getOutfits().get(12));
        assertEquals("Norseman", auctionDomainDTO.getOutfits().get(13));
        assertEquals("Oriental", auctionDomainDTO.getOutfits().get(14));
        assertEquals("Percht Raider", auctionDomainDTO.getOutfits().get(15));
        assertEquals("Summoner", auctionDomainDTO.getOutfits().get(16));
        assertEquals("Warmaster", auctionDomainDTO.getOutfits().get(17));
        assertEquals("Warrior", auctionDomainDTO.getOutfits().get(18));
        assertEquals("Wayfarer", auctionDomainDTO.getOutfits().get(19));
        assertEquals("Wizard", auctionDomainDTO.getOutfits().get(20));
        assertEquals("Yalaharian", auctionDomainDTO.getOutfits().get(21));

        assertEquals(1, auctionDomainDTO.getStoreOutfits().size());
        assertEquals("Retro Warrior", auctionDomainDTO.getStoreOutfits().get(0));

        assertEquals(7, auctionDomainDTO.getImbuements().size());
        assertEquals("Powerful Cloud Fabric", auctionDomainDTO.getImbuements().get(0));
        assertEquals("Powerful Dragon Hide", auctionDomainDTO.getImbuements().get(1));
        assertEquals("Powerful Electrify", auctionDomainDTO.getImbuements().get(2));
        assertEquals("Powerful Lich Shroud", auctionDomainDTO.getImbuements().get(3));
        assertEquals("Powerful Reap", auctionDomainDTO.getImbuements().get(4));
        assertEquals("Powerful Scorch", auctionDomainDTO.getImbuements().get(5));
        assertEquals("Powerful Swiftness", auctionDomainDTO.getImbuements().get(6));

        assertEquals(16, auctionDomainDTO.getCompletedQuestLines().size());
        assertEquals("A Father's Burden", auctionDomainDTO.getCompletedQuestLines().get(0));
        assertEquals("Blood Brothers", auctionDomainDTO.getCompletedQuestLines().get(1));
        assertEquals("Children of the Revolution", auctionDomainDTO.getCompletedQuestLines().get(2));
        assertEquals("Dawnport", auctionDomainDTO.getCompletedQuestLines().get(3));
        assertEquals("In Service Of Yalahar", auctionDomainDTO.getCompletedQuestLines().get(4));
        assertEquals("Shadows of Yalahar", auctionDomainDTO.getCompletedQuestLines().get(5));
        assertEquals("The Ape City", auctionDomainDTO.getCompletedQuestLines().get(6));
        assertEquals("The Beginning", auctionDomainDTO.getCompletedQuestLines().get(7));
        assertEquals("The Djinn War - Efreet Faction", auctionDomainDTO.getCompletedQuestLines().get(8));
        assertEquals("The Djinn War - Marid Faction", auctionDomainDTO.getCompletedQuestLines().get(9));
        assertEquals("The Ice Islands", auctionDomainDTO.getCompletedQuestLines().get(10));
        assertEquals("The Inquisition", auctionDomainDTO.getCompletedQuestLines().get(11));
        assertEquals("The New Frontier", auctionDomainDTO.getCompletedQuestLines().get(12));
        assertEquals("The Pits of Inferno", auctionDomainDTO.getCompletedQuestLines().get(13));
        assertEquals("The Queen of the Banshees", auctionDomainDTO.getCompletedQuestLines().get(14));
        assertEquals("Wrath of the Emperor", auctionDomainDTO.getCompletedQuestLines().get(15));

        assertEquals(9, auctionDomainDTO.getTitles().size());
        assertEquals("Beastrider (Grade 1)", auctionDomainDTO.getTitles().get(0));
        assertEquals("Creature of Habit (Grade 1)", auctionDomainDTO.getTitles().get(1));
        assertEquals("Creature of Habit (Grade 2)", auctionDomainDTO.getTitles().get(2));
        assertEquals("Cyclopscamper", auctionDomainDTO.getTitles().get(3));
        assertEquals("Demondoom", auctionDomainDTO.getTitles().get(4));
        assertEquals("Dragondouser", auctionDomainDTO.getTitles().get(5));
        assertEquals("Tibia's Topmodel (Grade 1)", auctionDomainDTO.getTitles().get(6));
        assertEquals("Tibia's Topmodel (Grade 2)", auctionDomainDTO.getTitles().get(7));
        assertEquals("Trolltrasher", auctionDomainDTO.getTitles().get(8));

        assertEquals(3, auctionDomainDTO.getAchievements().size());
        assertEquals("A Study in Scarlett", auctionDomainDTO.getAchievements().get(0));
        assertEquals("All Hail the King", auctionDomainDTO.getAchievements().get(1));
        assertEquals("Allow Cookies?", auctionDomainDTO.getAchievements().get(2));
    }
}
