import { createSlice } from "@reduxjs/toolkit";

export const shareDataSlice = createSlice({
  name: "shareData",
  initialState: null,
  reducers: {
    addData(state, action) {
      state = action.payload;
      return state;
    },

    clearData(state, action) {
      state = "";
      return state;
    },
  },
});

const { actions, reducer } = shareDataSlice;

export const { addData, clearData } = actions;

export default reducer;
