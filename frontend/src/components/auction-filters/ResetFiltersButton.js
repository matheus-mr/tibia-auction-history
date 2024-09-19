import Button from "@mui/material/Button";
import FilterAltOffIcon from "@mui/icons-material/FilterAltOff";

export default function ResetFiltersButton({ onClick }) {
  return (
    <Button
      fullWidth={true}
      variant="contained"
      endIcon={<FilterAltOffIcon />}
      onClick={onClick}
    >
      Reset Filters
    </Button>
  );
}
