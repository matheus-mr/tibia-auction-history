import Button from "@mui/material/Button";
import SearchIcon from "@mui/icons-material/Search";

export default function SearchButton({ onClick }) {
  return (
    <Button
      fullWidth={true}
      variant="contained"
      endIcon={<SearchIcon />}
      onClick={onClick}
    >
      Search
    </Button>
  );
}
