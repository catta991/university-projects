import { configureStore } from "@reduxjs/toolkit";
import { loginApi } from "../features/reduxApi/loginApi";
import { usersApi } from "../features/reduxApi/usersApi";
import { refreshApi } from "../features/reduxApi/refreshApi";
import { contactGroupApi } from "../features/reduxApi/contactGroupApi";
import { hostApi } from "../features/reduxApi/hostApi";
import userReducer from "../features/slice/userSlice";
import shareDataReducer from "../features/slice/shareDataSlice";

export const store = configureStore({
  reducer: {
    // registrazione userSlice
    user: userReducer,

    // registrazione share data slice
    shareData: shareDataReducer,

    // registrazione loginApi
    [loginApi.reducerPath]: loginApi.reducer,

    //registrazione usersApi
    [usersApi.reducerPath]: usersApi.reducer,

    //registrazione refresh
    [refreshApi.reducerPath]: refreshApi.reducer,

    // registrazione contactGroup api
    [contactGroupApi.reducerPath]: contactGroupApi.reducer,

    // registrazione host api
    [hostApi.reducerPath]: hostApi.reducer,
  },
});
