import TextField from "@mui/material/TextField";
import { useAppState } from "../AppStateContext";

const MIN_LEVEL = 1;
const MAX_LEVEL = 3000;

export default function LevelFilter() {
  const { setSimpleFilterValueFromEvent, getFilterValue } = useAppState();

  const handleChange = (event) => {
    const newValue = event.target.value;

    if (newValue === "") {
      setSimpleFilterValueFromEvent("level")(event);
      return;
    }

    const numericValue = Number(newValue);

    if (numericValue >= MIN_LEVEL && numericValue <= MAX_LEVEL) {
      setSimpleFilterValueFromEvent("level")(event);
    }
  };

  return (
    <TextField
      label="Level"
      inputProps={{ min: MIN_LEVEL, max: MAX_LEVEL }}
      value={getFilterValue("level")}
      onChange={handleChange}
      type="number"
      fullWidth={true}
    />
  );
}
