import { useDispatch, useSelector } from "react-redux";

import "../../../css/addUser.css";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getSingleUserInfo } from "../../utilFunctions/fetchFunction";
import { toast } from "react-toastify";
import { verifySecretCode } from "../../utilFunctions/verifySecretCode";
import { useUpdateUserFirstLoginMutation } from "../../reduxApi/usersApi";
import { clearData } from "../../slice/shareDataSlice";

function FirstLogin({ setLoadingData, setLoadingPage }) {
  const loggedUser = useSelector((state) => state.user);

  const secretCodeEntity = useSelector((state) => state.shareData);
  const [firstUpdateUser] = useUpdateUserFirstLoginMutation();
  console.log("sec2", secretCodeEntity);

  const dispatch = useDispatch();

  const navigate = useNavigate();

  const [nameUser, setNameUser] = useState("");
  const [surname, setSurname] = useState("");
  const [email, setEmail] = useState("");
  const [username, setUserName] = useState("");
  const [isCorrectSecretCode, setIsCorrectSecretCode] = useState(false);
  const [newPassword, setNewPassword] = useState("");
  const [secretCode, setSecretCode] = useState("");
  const [passwrodToShort, setPasswordToShort] = useState(true);

  const callGetSingleUserInfo = async () => {
    const data = await getSingleUserInfo(loggedUser).catch((err) => {
      setLoadingPage(false);
      toast.error("error in retriving data");
    });
    console.log(data);
    //setSingleUserInfo(data);
    if (data !== undefined) {
      setNameUser("");
      setSurname("");
      setEmail("");
      setUserName(data.username);
      setNewPassword("");
      setSecretCode("");
      setLoadingPage(false);
    }
  };
  let prova = false;
  const callVerifySecretCode = async () => {
    const data = await verifySecretCode(
      secretCode,
      secretCodeEntity.secretCode
    );

    const user = {
      email,
      username,
      name: nameUser,
      surname,
      password: newPassword,
      roleName: "ROLE_SUPER_ADMIN",
      contactGroups: ["all"],
    };

    console.log("new user", user);
    console.log(prova);

    if (data) {
      setLoadingData("updating user");
      setLoadingPage(true);
      firstUpdateUser(user)
        .unwrap()
        .then((risp) => {
          setLoadingPage(false);
          toast.success(risp.message);
          dispatch(clearData());
          navigate("/logout");
        })
        .catch((err) => {
          setLoadingPage(false);
          console.log(err);
          toast.error(err.data);
        });
    } else toast.error("wrong secret code");
  };

  useEffect(() => {
    setLoadingPage(true);
    setLoadingData("Loading data");

    callGetSingleUserInfo();
    return () => {};
  }, []);

  const applyHandler = (e) => {
    e.preventDefault();
    callVerifySecretCode();
  };

  return (
    <div className="box">
      <div className="form-container">
        <h3 className="title">Modify super admin</h3>
        <form className="form-horizontal">
          {passwrodToShort && (
            <div
              className="alert alert-danger"
              role="alert"
              style={{
                fontSize: "15px",
                textAlign: "center",
              }}
            >
              password's length is under 10 character
            </div>
          )}
          <div className="form-group">
            <label>Name</label>
            <input
              type="text"
              className="form-control "
              placeholder={"name"}
              required
              value={nameUser}
              onChange={(e) => setNameUser(e.target.value)}
            />
          </div>
          <div className="form-group ">
            <label>Surname</label>
            <input
              type="text"
              className="form-control "
              placeholder={"surname"}
              required
              value={surname}
              onChange={(e) => setSurname(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label>User Name</label>
            <input
              className="form-control "
              value={username}
              disabled={true}
              placeholder={"username"}
            />
          </div>
          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              className="form-control"
              placeholder={"email"}
              value={email}
              required
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label>New Password</label>
            <input
              type="text"
              className="form-control"
              placeholder="New Password"
              required
              value={newPassword}
              onChange={(e) => {
                console.log(e.target.value.length);
                if (e.target.value.length < 10 && e.target.value.length !== 0) {
                  setPasswordToShort(true);
                  setNewPassword(e.target.value);
                } else if (e.target.value.length === 0) {
                  setPasswordToShort(false);
                  setNewPassword(e.target.value);
                } else {
                  setPasswordToShort(false);
                  setNewPassword(e.target.value);
                }
              }}
            />
          </div>
          <div className="form-group">
            <label>Secret code</label>
            <input
              type="text"
              className="form-control"
              placeholder="Secret code"
              required
              onChange={(e) => setSecretCode(e.target.value)}
              value={secretCode}
            />
          </div>

          <button
            className="btn signup"
            onClick={(e) => applyHandler(e)}
            disabled={
              passwrodToShort ||
              nameUser.length === 0 ||
              surname.length === 0 ||
              email.length === 0 ||
              secretCode.length === 0
            }
          >
            Modify
          </button>
        </form>
      </div>
    </div>
  );
}

export default FirstLogin;
