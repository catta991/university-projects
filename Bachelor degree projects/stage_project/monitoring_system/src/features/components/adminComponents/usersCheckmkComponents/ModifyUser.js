import { useDispatch, useSelector } from "react-redux";
import MultiSelectLite from "react-multiple-select-dropdown-lite";
import "react-multiple-select-dropdown-lite/dist/index.css";
import "../../../../css/addUser.css";
import { useState, useEffect } from "react";
import { useUpdateUserMutation } from "../../../reduxApi/usersApi";
import { useNavigate } from "react-router-dom";
import { clearData } from "../../../slice/shareDataSlice";
import {
  getRoles,
  getAllContactGroups,
} from "../../../utilFunctions/fetchFunction";
import { toast } from "react-toastify";
import isExpiredToken from "../../../utilFunctions/isExipredToken";

function ModifyUser({ openRefreshToken, setLoadingData, setLoadingPage }) {
  const loggedUser = useSelector((state) => state.user);
  console.log(loggedUser);

  const [updateUser] = useUpdateUserMutation();

  let userData = useSelector((state) => state.shareData);

  const dispatch = useDispatch();

  const navigate = useNavigate();

  const [roleName, setRoleState] = useState(
    userData && userData.roles === "admin"
      ? "ROLE_ADMIN"
      : userData.roles === "superadmin"
      ? "ROLE_SUPER_ADMIN"
      : "ROLE_USER"
  );
  const [rolesFromDb, setRolesFromDb] = useState([]);
  const [contactGroups, setContactGroupState] = useState(
    userData.contactGroups.replaceAll(" ", "").split(",")
  );

  const [contactGroupMultiselect, setContactGroupMultiselect] = useState([]);

  const callGetRoles = async () => {
    const data = await getRoles(loggedUser);

    setRolesFromDb(data);
  };

  const callGetContactGroups = async () => {
    const data = await getAllContactGroups(loggedUser);

    console.log("data", data);

    let contactGroupApp = [];

    for (let i = 0; i < data.length; i++) {
      contactGroupApp.push({
        label: data[i].name,
        value: data[i].name,
      });
    }

    setContactGroupMultiselect(contactGroupApp);
  };

  const navigateToUsersComponents = () => navigate("/users");
  useEffect(() => {
    callGetRoles();
    callGetContactGroups();

    return () => {};
  }, []);

  const [nameUser, setNameUser] = useState(userData.name.split(" ")[0]);
  const [surname, setSurname] = useState(userData.name.split(" ")[1]);
  const [email, setEmail] = useState(userData.email);
  const [username, setUserName] = useState(userData.username);
  const [newPassword, setNewPassword] = useState("");
  const [passwrodToShort, setPasswordToShort] = useState(false);

  let groups = [];

  const contactGroupChangheHandler = (val) => {
    console.log("val ", typeof val);
    groups = [];
    if (val !== "") groups = val.split(",");
    console.log("gropus " + groups);

    setContactGroupState(groups);
  };

  const roleChangheHandler = (val) => {
    setRoleState(val);
  };

  const applyHandler = (e) => {
    e.preventDefault();
    const user = {
      email,
      username,
      name: nameUser,
      surname,
      password: newPassword,
      roleName,
      contactGroups,
    };

    console.log("new user", user);

    if (isExpiredToken(loggedUser.decodedAccessToken.exp))
      openRefreshToken(true);
    else {
      setLoadingData("updating user");
      setLoadingPage(true);
      updateUser(user)
        .unwrap()
        .then((risp) => {
          setLoadingPage(false);
          toast.success(risp.message);
          if (
            user.username === loggedUser.decodedAccessToken.sub &&
            user.password !== ""
          ) {
            dispatch(clearData());
            navigate("/logout");
          } else {
            dispatch(clearData());
            navigate("/users");
          }
        })
        .catch((err) => {
          setLoadingPage(false);
          console.log(err);
          toast.error(err.data);
        });
    }
  };

  console.log("userdata", contactGroups);

  return (
    <div className="box">
      <div className="form-container">
        <h3 className="title">Modify user {userData.username}</h3>
        <form className="form-horizontal">
          {passwrodToShort && (
            <div
              class="alert alert-danger"
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
            <input className="form-control " value={username} disabled={true} />
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
          <div className="form-group"></div>
          <div className="form-group">
            <label>Select role</label>
            <MultiSelectLite
              className="multiple-select"
              placeholder={roleName}
              value={roleName}
              options={rolesFromDb}
              onChange={roleChangheHandler}
              singleSelect
              disabled={userData.roles === "superadmin"}
            />
          </div>
          <div className="form-group">
            <label>Select contact groups</label>
            <MultiSelectLite
              onChange={contactGroupChangheHandler}
              className="multiple-select"
              placeholder={
                contactGroups[0] !== ""
                  ? contactGroups
                  : "Select contact groups"
              }
              options={contactGroupMultiselect}
              disabled={userData.roles === "superadmin"}
            />
          </div>
          <button
            className="btn signup"
            onClick={(e) => applyHandler(e)}
            disabled={roleName === "" || passwrodToShort}
          >
            Modify
          </button>
          <button
            type="button"
            className="btn back"
            onClick={() => {
              dispatch(clearData());
              navigateToUsersComponents();
            }}
          >
            Back
          </button>
        </form>
      </div>
    </div>
  );
}

export default ModifyUser;
