import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import AccordionDetails from "@mui/material/AccordionDetails";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import Typography from "@mui/material/Typography";
import { Grid } from "@mui/material";
import SkillsFilter from "./SkillsFilter";
import ItemsFilter from "./ItemsFilter";
import MountsFilter from "./MountsFilter";
import OutfitsFilter from "./OutfitsFilter";
import ImbuementsAndQuestsFilter from "./ImbuementsAndQuestsFilter";
import TitlesAndAchievementsFilter from "./TitlesAndAchievementsFilter";
import MainInfoFilter from "./MainInfoFilter";
import SearchButton from "./SearchButton";
import ResetFiltersButton from "./ResetFiltersButton";
import { useAppState } from "../AppStateContext";

export default function AuctionFilters({
  onClickSearchButton,
  onClickResetFiltersButton,
}) {
  const { getFilterValue, setSimpleFieldValue } = useAppState();
  return (
    <Accordion
      expanded={getFilterValue("expanded")}
      onChange={(event, expanded) =>
        setSimpleFieldValue("filters.expanded", expanded)
      }
    >
      <AccordionSummary expandIcon={<ExpandMoreIcon />}>
        <Typography>Filters</Typography>
      </AccordionSummary>
      <AccordionDetails>
        <Grid container rowSpacing={2}>
          <Grid container item>
            <MainInfoFilter />
          </Grid>
          <Grid container item>
            <SkillsFilter />
          </Grid>
          <Grid container item>
            <ItemsFilter />
          </Grid>
          <Grid container item>
            <MountsFilter />
          </Grid>
          <Grid container item>
            <ImbuementsAndQuestsFilter />
          </Grid>
          <Grid container item>
            <TitlesAndAchievementsFilter />
          </Grid>
          <Grid container item>
            <OutfitsFilter />
          </Grid>
          <Grid container item direction="row" spacing={1}>
            <Grid item xs>
              <SearchButton onClick={onClickSearchButton} />
            </Grid>
            <Grid item xs>
              <ResetFiltersButton onClick={onClickResetFiltersButton} />
            </Grid>
          </Grid>
        </Grid>
      </AccordionDetails>
    </Accordion>
  );
}
