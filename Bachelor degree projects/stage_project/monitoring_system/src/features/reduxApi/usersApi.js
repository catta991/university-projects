//reduxApi for users table
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const usersApi = createApi({
  reducerPath: "users",
  tagTypes: ["USERS"],
  baseQuery: fetchBaseQuery({
    baseUrl: "http://localhost:8092",

    // setto l'header di autenticazione
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
    getUsers: builder.query({
      query: () => "/auth/user/checkMk",
      providesTags: ["USERS"],
    }),

    getRoles: builder.query({
      query: () => "/auth/user/roles",
    }),

    getContactGroups: builder.query({
      query: (username) => ({ url: `auth/user/getSingleUser/${username}` }),
    }),

    addNewUser: builder.mutation({
      query: (user) => ({
        url: "/auth/user/save",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: user,
      }),
    }),

    updateUser: builder.mutation({
      query: (user) => ({
        url: "auth/user/update",
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: user,
      }),
      invalidatesTags: ["USERS"],
    }),

    deleteUser: builder.mutation({
      query: (user) => ({
        url: "/auth/user/delete",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: user,
      }),
      invalidatesTags: ["USERS"],
    }),

    updateUserFirstLogin: builder.mutation({
      query: (user) => ({
        url: "/auth/user/set/secret/code",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: user,
      }),
    }),
  }),
});

export const {
  useGetUsersQuery,
  useGetRolesQuery,
  useGetContactGroupsQuery,
  useAddNewUserMutation,
  useDeleteUserMutation,
  useUpdateUserMutation,
  useUpdateUserFirstLoginMutation,
} = usersApi;
