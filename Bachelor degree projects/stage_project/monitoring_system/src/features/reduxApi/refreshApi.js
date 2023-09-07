//reduxApi for refresh access token
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const refreshApi = createApi({
  reducerPath: "refresh",
  baseQuery: fetchBaseQuery({
    baseUrl: "http://localhost:8092",
    // setto nel header il refresh token
    prepareHeaders: (headers, { getState }) => {
      let token = null;
      //serve perchè quando refresho la pagina se no da errore
      if (getState().user !== null) {
        token = getState().user.refreshToken;
      }
      // If we have a token set in state, let's assume that we should be passing it.
      if (token) {
        headers.set("Auth", `Bearer ${token}`);
      }
      return headers;
    },
  }),
  endpoints: (builder) => ({
    refreshToken: builder.mutation({
      query: () => ({
        url: "/auth/user/refreshToken",
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "GET",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
      }),
    }),
  }),
});

export const { useRefreshTokenMutation } = refreshApi;
