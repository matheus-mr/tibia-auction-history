import TextField from "@mui/material/TextField";
import { useAppState } from "../AppStateContext";

export default function NameFilter() {
  const { setSimpleFilterValueFromEvent, getFilterValue } = useAppState();

  return (
    <TextField
      value={getFilterValue("name")}
      onChange={setSimpleFilterValueFromEvent("name")}
      label="Name"
    />
  );
}
