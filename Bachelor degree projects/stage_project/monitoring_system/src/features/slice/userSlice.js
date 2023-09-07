import { createSlice } from "@reduxjs/toolkit";
import decodeToken from "../utilFunctions/decodeJwtToken";

export const userSlice = createSlice({
  name: "user",
  initialState: null,
  reducers: {
    addLoggedUser(state, action) {
      // decodifico l'access jwt token
      const decodedAccessToken = decodeToken(
        action.payload.LoginData.access_token
      );

      const decodedRefreshToken = decodeToken(
        action.payload.LoginData.refresh_token
      );

      // modifico lo stato e lo ritorno
      state = {
        decodedAccessToken,
        accessToken: action.payload.LoginData.access_token,
        refreshToken: action.payload.LoginData.refresh_token,
        password: action.payload.password,
        refreshTokenExipration: decodedRefreshToken.exp,
      };
      return state;
    },

    logOut(state, action) {
      state = null;
      return state;
    },

    refreshToken(state, action) {
      const decodedAccessToken = decodeToken(action.payload.newAccessToken);

      const decodedRefreshToken = decodeToken(action.payload.newRefreshToken);

      state.decodedAccessToken = decodedAccessToken;
      state.accessToken = action.payload.newAccessToken;
      state.refreshToken = action.payload.newRefreshToken;
      state.refreshTokenExipration = decodedRefreshToken.exp;

      return state;
    },
  },
});

const { actions, reducer } = userSlice;

export const { addLoggedUser, logOut, refreshToken } = actions;

export default reducer;
