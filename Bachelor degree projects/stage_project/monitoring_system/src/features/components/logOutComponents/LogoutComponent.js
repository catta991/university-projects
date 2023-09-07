import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { usersApi } from "../../reduxApi/usersApi";
import { contactGroupApi } from "../../reduxApi/contactGroupApi";
import { loginApi } from "../../reduxApi/loginApi";

import { logOut } from "../../slice/userSlice";

function LogoutComponent() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  useEffect(() => {
    console.log("in logout component");
    navigate("/");
    dispatch(logOut());
    // dispatch(usersApi.util.resetApiState());
    // dispatch(contactGroupApi.util.resetApiState());
    dispatch(loginApi.util.resetApiState());

    return () => {};
  }, []);

  return;
}

export default LogoutComponent;
