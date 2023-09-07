//reduxApi for contact groups

import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const contactGroupApi = createApi({
  reducerPath: "contactGroup",
  baseQuery: fetchBaseQuery({
    baseUrl: "http://localhost:8092",
    prepareHeaders: (headers, { getState }) => {
      let token = null;
      //serve perchè quando refresho la pagina se no da errore
      if (getState().user !== null) {
        token = getState().user.accessToken;
      }
      // If we have a token set in state, let's assume that we should be passing it.
      if (token) {
        headers.set("Auth", `Bearer ${token}`);
      }

      let pwd = null;

      if (getState().user !== null) {
        pwd = getState().user.password;
      }

      if (pwd) {
        headers.set("Pwd", `${pwd}`);
      }

      return headers;
    },
  }),

  endpoints: (builder) => ({
    getContactGroups: builder.query({
      query: () => "/auth/userContactGroup",
    }),
    updateContactGroup: builder.mutation({
      query: (contactGroup) => ({
        url: "auth/modify/contactgroup",
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "PUT",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: contactGroup,
      }),
    }),
    deleteContactGroup: builder.mutation({
      query: (contactGroup) => ({
        url: "auth/delete/contactgroup",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: contactGroup,
      }),
    }),

    addContactGroup: builder.mutation({
      query: (contactGroup) => ({
        url: "auth/create/contactgroup",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: contactGroup,
      }),
    }),
  }),
});

export const {
  useGetContactGroupsQuery,
  useUpdateContactGroupMutation,
  useDeleteContactGroupMutation,
  useAddContactGroupMutation,
} = contactGroupApi;
