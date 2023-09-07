import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import "../../../css/login.css";
import { useLoginMutation } from "../../reduxApi/loginApi";
import { addLoggedUser } from "../../slice/userSlice";
import { addData } from "../../slice/shareDataSlice";
import { useNavigate } from "react-router-dom";
import { getSecretCode } from "../../utilFunctions/fetchFunction";

function LogIn() {
  let login = useSelector((state) => state.user);
  const navigate = useNavigate();

  const dispatch = useDispatch();
  const [loginApi, { isLoading: loadingUser, data: LoginData }] =
    useLoginMutation();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [logError, setLogError] = useState(false);

  const [secretCodeEntity, setSecretCodeEnitiy] = useState(null);

  const callGetSecretCode = async (LoginData) => {
    const data = await getSecretCode(LoginData).catch((err) =>
      console.log(err)
    );

    console.log(data);
    setSecretCodeEnitiy(data);
    dispatch(addLoggedUser({ LoginData, password }));
    dispatch(addData(data));
  };

  useEffect(() => {
    if (login === null && !loadingUser && LoginData !== undefined) {
      console.log("login ", LoginData);
      callGetSecretCode(LoginData);
    }

    if (
      login !== null &&
      secretCodeEntity !== null &&
      secretCodeEntity !== undefined &&
      secretCodeEntity.firstAccess
    ) {
      console.log("first access");
      navigate("/first/login");
      //setSecretCodeEnitiy(null);
    } else if (
      login !== null &&
      (login.decodedAccessToken.roles.includes("ROLE_ADMIN") ||
        login.decodedAccessToken.roles.includes("ROLE_SUPER_ADMIN"))
    ) {
      navigate("/admin");
    } else if (
      login !== null &&
      login.decodedAccessToken.roles.includes("ROLE_USER")
    ) {
      navigate("/monitored/hosts");
    }

    return () => {};
  }, [login, loadingUser, LoginData]);

  function verifyLogin(e) {
    e.preventDefault();
    console.log("username", username);
    console.log("password", password);

    const user = {
      username,
      password,
    };

    //per ora sembra funzionare
    loginApi(user)
      .unwrap()
      .catch((err) => setLogError(true));
  }

  const loginError = (
    <div id="loginError" className="alert alert-danger" role="alert">
      Ops something went wrong, please check username and password.
    </div>
  );

  return (
    <>
      <div id="bg"></div>
      {logError && loginError}
      <form onSubmit={verifyLogin}>
        <div className="form-field">
          <input
            type="username"
            placeholder="Username"
            required
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>

        <div className="form-field">
          <input
            type="password"
            placeholder="Password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />{" "}
        </div>

        <div className="form-field">
          <button className="btn" type="submit">
            Login
          </button>
        </div>
      </form>
    </>
  );
}

export default LogIn;
