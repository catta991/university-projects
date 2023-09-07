//reduxApi for login

import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const loginApi = createApi({
  reducerPath: "login",
  tagTypes: ["login"],
  baseQuery: fetchBaseQuery({
    baseUrl: "http://localhost:8092",
  }),
  endpoints: (builder) => ({
    // mutation che serve a inviare al backend i dati per il login
    login: builder.mutation({
      query: (item) => ({
        url: "/auth/user/login",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: item,
      }),
    }),
  }),
});

export const { useLoginMutation } = loginApi;
