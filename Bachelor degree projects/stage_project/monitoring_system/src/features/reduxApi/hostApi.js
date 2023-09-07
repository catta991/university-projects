import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const hostApi = createApi({
  reducerPath: "hosts",
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
    updateHost: builder.mutation({
      query: (host) => ({
        url: "/auth/host/update",
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "PUT",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: host,
      }),
    }),
    installAgent: builder.mutation({
      query: (hostName) => ({
        url: `/auth/host/install/agent/${hostName}`,
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
      }),
    }),

    installPlugins: builder.mutation({
      query: (data) => ({
        url: `/auth/host/install/plugin/${data.hostName}`,
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: data.plugins,
      }),
    }),

    deleteHost: builder.mutation({
      query: (hostName) => ({
        url: `/auth/host/delete/${hostName}`,
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "DELETE",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
      }),
    }),
    addHostToMonitoring: builder.mutation({
      query: (body) => ({
        url: "/auth/host/add/toMonitoring",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: body,
      }),
    }),

    updateSSHCredentials: builder.mutation({
      query: (body) => ({
        url: "/auth/host/set/SSH",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "http://localhost:8092",
          "Access-Control-Allow-Credentials": "true",
          "Access-Control-Request-Method": "POST",
          "Access-Control-Allow-Headers":
            "Origin, X-Requested-With, Content-Type, Accept",
        },
        body: body,
      }),
    }),
  }),
});

export const {
  useUpdateHostMutation,
  useInstallAgentMutation,
  useInstallPluginsMutation,
  useDeleteHostMutation,
  useAddHostToMonitoringMutation,
  useUpdateSSHCredentialsMutation,
} = hostApi;
