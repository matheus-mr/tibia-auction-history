import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import Typography from "@mui/material/Typography";
import SkillsFilter from "./skills-filter";
import ItemsFilter from "./items-filter";
import MountsFilter from "./mounts-filter";
import OutfitsFilter from "./outfits-filter";
import ImbuementsAndQuestsFilter from "./imbuements-quests-filter";
import TitlesAndAchievementsFilter from "./titles-achievements-filter";
import MainInfoFilter from "./main-info-filter";
import SearchButton from "./search-button";

import "./auction-filters.css";

export default function AuctionFilters({
  domain,
  onClickSearch,
  name,
  onChangeName,
  level,
  onChangeLevel,
  selectedVocations,
  onChangeSelectedVocations,
  axeFighting,
  onChangeAxeFighting,
  clubFighting,
  onChangeClubFighting,
  swordFighting,
  onChangeSwordFighting,
  fistFighting,
  onChangeFistFighting,
  distanceFighting,
  onChangeDistanceFighting,
  magicLevel,
  onChangeMagicLevel,
  shielding,
  onChangeShielding,
  fishing,
  onChangeFishing,
  selectedItems,
  onChangeSelectedItems,
  selectedStoreItems,
  onChangeSelectedStoreItems,
  selectedMounts,
  onChangeSelectedMounts,
  selectedStoreMounts,
  onChangeSelectedStoreMounts,
  selectedOutfits,
  onSelectOutfit,
  onDeleteOutfit,
  onClickAddon,
  selectedStoreOutfits,
  onSelectStoreOutfit,
  onDeleteStoreOutfit,
  onClickStoreAddon,
  selectedImbuements,
  onChangeSelectedImbuements,
  selectedCompletedQuestLines,
  onChangeSelectedCompletedQuestLines,
  selectedTitles,
  onChangeSelectedTitles,
  selectedAchievements,
  onChangeSelectedAchievements,
  selectedWorlds,
  onChangeSelectedWorlds,
}) {
  return (
    <div className="auction-filters-container">
      <Accordion defaultExpanded={true}>
        <AccordionSummary expandIcon={<ExpandMoreIcon />}>
          <Typography>Filters</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <div className="filters-container">
            <MainInfoFilter
              name={name}
              onChangeName={onChangeName}
              level={level}
              onChangeLevel={onChangeLevel}
              vocations={domain.vocations}
              selectedVocations={selectedVocations}
              onChangeSelectedVocations={onChangeSelectedVocations}
              worlds={domain.worlds}
              selectedWorlds={selectedWorlds}
              onChangeSelectedWorlds={onChangeSelectedWorlds}
            />
            <SkillsFilter
              axeFighting={axeFighting}
              onChangeAxeFighting={onChangeAxeFighting}
              clubFighting={clubFighting}
              onChangeClubFighting={onChangeClubFighting}
              swordFighting={swordFighting}
              onChangeSwordFighting={onChangeSwordFighting}
              fistFighting={fistFighting}
              onChangeFistFighting={onChangeFistFighting}
              distanceFighting={distanceFighting}
              onChangeDistanceFighting={onChangeDistanceFighting}
              magicLevel={magicLevel}
              onChangeMagicLevel={onChangeMagicLevel}
              shielding={shielding}
              onChangeShielding={onChangeShielding}
              fishing={fishing}
              onChangeFishing={onChangeFishing}
            />
            <ItemsFilter
              items={domain.items}
              storeItems={domain.storeItems}
              selectedItems={selectedItems}
              onChangeSelectedItems={onChangeSelectedItems}
              selectedStoreItems={selectedStoreItems}
              onChangeSelectedStoreItems={onChangeSelectedStoreItems}
            />
            <MountsFilter
              mounts={domain.mounts}
              storeMounts={domain.storeMounts}
              selectedMounts={selectedMounts}
              onChangeSelectedMounts={onChangeSelectedMounts}
              selectedStoreMounts={selectedStoreMounts}
              onChangeSelectedStoreMounts={onChangeSelectedStoreMounts}
            />
            <ImbuementsAndQuestsFilter
              imbuements={domain.imbuements}
              completedQuestLines={domain.completedQuestLines}
              selectedImbuements={selectedImbuements}
              onChangeSelectedImbuements={onChangeSelectedImbuements}
              selectedCompletedQuestLines={selectedCompletedQuestLines}
              onChangeSelectedCompletedQuestLines={
                onChangeSelectedCompletedQuestLines
              }
            />
            <TitlesAndAchievementsFilter
              titles={domain.titles}
              achievements={domain.achievements}
              selectedTitles={selectedTitles}
              onChangeSelectedTitles={onChangeSelectedTitles}
              selectedAchievements={selectedAchievements}
              onChangeSelectedAchievements={onChangeSelectedAchievements}
            />
            <OutfitsFilter
              outfits={domain.outfits}
              storeOutfits={domain.storeOutfits}
              selectedOutfits={selectedOutfits}
              onSelectOutfit={onSelectOutfit}
              onDeleteOutfit={onDeleteOutfit}
              onClickAddon={onClickAddon}
              selectedStoreOutfits={selectedStoreOutfits}
              onSelectStoreOutfit={onSelectStoreOutfit}
              onDeleteStoreOutfit={onDeleteStoreOutfit}
              onClickStoreAddon={onClickStoreAddon}
            />
            <SearchButton onClick={onClickSearch} />
          </div>
        </AccordionDetails>
      </Accordion>
    </div>
  );
}
