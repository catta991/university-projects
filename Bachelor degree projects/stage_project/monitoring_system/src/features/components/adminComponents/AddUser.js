import MultiSelect from "react-multiple-select-dropdown-lite";
import "react-multiple-select-dropdown-lite/dist/index.css";
import "../../../css/addUser.css";
import { useState, useEffect } from "react";
import { toast } from "react-toastify";
import AdminNavbar from "./AdminNavbar";
import { useAddNewUserMutation } from "../../reduxApi/usersApi";
import { useNavigate } from "react-router-dom";
import isExpiredToken from "../../utilFunctions/isExipredToken";
import { useSelector } from "react-redux";
import {
  getRoles,
  getAllContactGroups,
} from "../../utilFunctions/fetchFunction";

//per visualizzare dei valori di default basta aggiungere a <MuliSelect defaultValue={arr di valori di default}

// passo la props row e poi verifico se è vuota o meno per poter riutilizzare il componente anche per la modifica
function AddUser({ openRefreshToken, setLoadingData, setLoadingPage }) {
  let loggedUser = useSelector((state) => state.user);

  const [addNewUser, { isSuccess: newUserAdded, data: newUser }] =
    useAddNewUserMutation();

  const navigate = useNavigate();

  const [rolesFromDb, setRolesFromDb] = useState([]);
  const [contactGroupMultiselect, setContactGroupMultiselect] = useState([]);

  const navigateToUsersComponents = () => navigate("/users");

  const callGetRoles = async () => {
    const data = await getRoles(loggedUser);

    setRolesFromDb(data);
  };

  const callGetContactGroups = async () => {
    console.log("get contact groups");
    const data = await getAllContactGroups(loggedUser);

    console.log("data", data);

    let contactGroupApp = [];

    for (let i = 0; i < data.length; i++) {
      contactGroupApp.push({
        label: data[i].name,
        value: data[i].name,
      });
    }

    console.log("app", contactGroupApp);
    setContactGroupMultiselect(contactGroupApp);
  };

  useEffect(() => {
    if (!newUserAdded) {
      callGetContactGroups();
      callGetRoles();
    }

    if (newUserAdded) {
      console.log("new user", newUserAdded);
      toast.success("user correctly added");

      setTimeout(navigateToUsersComponents, 1500);
    }

    return () => {};
  }, [newUserAdded]);

  const [roleName, setRoleState] = useState([]);
  const [contactGroups, setContactGroupState] = useState([]);
  const [nameUser, setNameUser] = useState("");
  const [surname, setSurname] = useState("");
  const [username, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [repeatPassword, setRepeatPassword] = useState("");

  const [isPasswordNotMatch, setIsPasswordNotMatch] = useState(false);

  let groups = [];

  const groupChangheHandler = (val) => {
    console.log("val ", typeof val);
    groups = [];
    if (val !== "") groups = val.split(",");
    console.log("gropus " + groups);
    setContactGroupState(groups);
  };

  const roleChangheHandler = (val) => {
    setRoleState(val);
  };

  const registerHandler = (e) => {
    e.preventDefault();
    const user = {
      email,
      name: nameUser,
      surname,
      username,
      password,
      roleName,
      contactGroups,
    };

    console.log("newUser", user);
    if (isExpiredToken(loggedUser.decodedAccessToken.exp))
      openRefreshToken(true);
    else {
      setLoadingData("adding user");
      setLoadingPage(true);
      addNewUser(user)
        .unwrap()
        .then((risp) => {
          setLoadingPage(false);
        })
        .catch((err) => {
          console.log(err.data);
          setLoadingPage(false);
          toast.error(err.data);
        });
    }
  };

  console.log("roleState ", roleName);

  // sistemare posizione alert
  return (
    <div className="box">
      <div className="form-container">
        <h3 className="title">Add new user</h3>
        <form className="form-horizontal">
          {isPasswordNotMatch && (
            <div
              class="alert alert-danger"
              role="alert"
              style={{
                fontSize: "15px",
                textAlign: "center",
              }}
            >
              the password did not match or password's length is under 10
              character
            </div>
          )}
          <div className="form-group">
            <label>Name</label>
            <input
              type="text"
              className="form-control "
              // se row non è vuoto da settare anche lo stato di name = a row.name
              placeholder="Name"
              required
              onChange={(e) => setNameUser(e.target.value)}
            />
          </div>
          <div className="form-group ">
            <label>Surname</label>
            <input
              type="text"
              className="form-control "
              placeholder="Surname"
              required
              onChange={(e) => setSurname(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label>User Name</label>
            <input
              type="text"
              className="form-control "
              placeholder="User Name"
              required
              onChange={(e) => setUserName(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              className="form-control"
              placeholder="Email Address"
              required
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input
              type="text"
              className="form-control"
              placeholder="Password"
              required
              onChange={(e) => {
                if (e.target.value !== repeatPassword || password.length < 10) {
                  setPassword(e.target.value);
                  setIsPasswordNotMatch(true);
                } else {
                  setPassword(e.target.value);
                  setIsPasswordNotMatch(false);
                }
              }}
            />
          </div>
          <div className="form-group">
            <label>Confirm Password</label>
            <input
              type="text"
              className="form-control"
              placeholder="Confirm Password"
              required
              onChange={(e) => {
                if (e.target.value !== password || password.length < 10) {
                  setRepeatPassword(e.target.value);
                  setIsPasswordNotMatch(true);
                } else {
                  setRepeatPassword(e.target.value);
                  setIsPasswordNotMatch(false);
                }
              }}
            />
          </div>
          <div className="form-group">
            <label>Select role</label>
            <MultiSelect
              className="multiple-select"
              options={rolesFromDb}
              onChange={roleChangheHandler}
              singleSelect
            />
          </div>
          <div className="form-group">
            <label>Select contact groups</label>
            <MultiSelect
              options={contactGroupMultiselect}
              onChange={groupChangheHandler}
              className="multiple-select"
            />
          </div>
          <button
            className="btn signup"
            disabled={
              roleName.length === 0 ||
              isPasswordNotMatch ||
              nameUser.length === 0 ||
              surname.length === 0 ||
              email.length === 0 ||
              username.length === 0 ||
              password.length === 0
            }
            onClick={registerHandler}
          >
            Register
          </button>
          <button
            type="button"
            className="btn back"
            onClick={() => {
              navigate(-1);
            }}
          >
            Back
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddUser;
