import "./App.css";
import LogIn from "./features/components/logInComponents/LogIn";
import AdminComponent from "./features/components/adminComponents/AdminComponent";

import UsersComponent from "./features/components/adminComponents/usersCheckmkComponents/UsersComponent";
import { Routes, Route } from "react-router-dom";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import LogoutComponent from "./features/components/logOutComponents/LogoutComponent";
import ModifyUser from "./features/components/adminComponents/usersCheckmkComponents/ModifyUser";
import AddUser from "./features/components/adminComponents/AddUser";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import RefreshToken from "./features/components/refreshTokenComponent/RefreshToken";
import ContactGroup from "./features/components/adminComponents/contactGroupsComponents/ContactGroup";

import PluginComponent from "./features/components/pluginComponents/PluginComponent";
import MonitoredHosts from "./features/components/hostsComponents/MonitoredHosts";
import LoadingComponent from "./features/components/loadingComponent/LoadingComponent";
import FirstLogin from "./features/components/FirstLoginComponent/FirstLogin";
import UnmonitoredHosts from "./features/components/hostsComponents/UnmonitoredHosts";

// comandi effettuati:
// npx create-react-app monitoring_system --template redux
// npm install bootstrap
// npm install jwt-decode
// npm install react-router-dom@6
// npm install react-data-table-component styled-components
// npm i bootstrap-icons
// npm install react-bootstrap bootstrap@4.6.0
// npm i react-multiple-select-dropdown-lite
// npm i react-toastify
//npm i react-multi-select-component
// npm i bcryptjs

function App() {
  // nel momento in cui si fa login oppure si fa logout lo stato
  // di user cambia e si viene reindirizzati sulla pagina di login
  // anche se faccio localhost:3000/admin e non sono loggato si viene sempre reindirizzati alla
  // pagino di login
  let user = useSelector((state) => state.user);
  const navigate = useNavigate();

  useEffect(() => {
    if (user === null) {
      navigate("/");
    }

    return () => {};
  }, [user]);

  const [showRefreshToken, setShowRefreshToken] = useState(false);
  const [showLoadingPage, setLoadingPage] = useState(false);
  const [loadingData, setLoadingData] = useState("Loading");

  const handleCloseRefreshToken = () => {
    setShowRefreshToken(false);
    navigate("/logout");
  };

  const handleYesButtonRefreshToken = () => {
    setShowRefreshToken(false);
  };

  return (
    <>
      <RefreshToken
        showRefresh={showRefreshToken}
        handleClose={handleCloseRefreshToken}
        yesRefreshHandler={handleYesButtonRefreshToken}
      />

      <LoadingComponent show={showLoadingPage} data={loadingData} />

      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />

      <Routes>
        <Route path="/" exact element={<LogIn />} />
        <Route path="/admin" exact element={<AdminComponent />} />

        <Route path="/logout" exact element={<LogoutComponent />} />
        <Route
          path="/users"
          exact
          element={
            <UsersComponent
              openRefreshToken={setShowRefreshToken}
              showRefToken={showRefreshToken}
              setLoadingData={setLoadingData}
              setLoadingPage={setLoadingPage}
            />
          }
        />
        <Route
          path="/addUser"
          exact
          element={
            <AddUser
              openRefreshToken={setShowRefreshToken}
              setLoadingData={setLoadingData}
              setLoadingPage={setLoadingPage}
            />
          }
        />
        <Route
          path="/modifyUser"
          exact
          element={
            <ModifyUser
              openRefreshToken={setShowRefreshToken}
              setLoadingData={setLoadingData}
              setLoadingPage={setLoadingPage}
            />
          }
        />

        <Route
          path="/contactGroups"
          exact
          element={
            <ContactGroup
              openRefreshToken={setShowRefreshToken}
              showRefToken={showRefreshToken}
              setLoadingData={setLoadingData}
              setLoadingPage={setLoadingPage}
            />
          }
        />

        <Route
          path="/monitored/hosts"
          exact
          element={
            <MonitoredHosts
              openRefreshToken={setShowRefreshToken}
              showRefToken={showRefreshToken}
              setLoadingData={setLoadingData}
              setLoadingPage={setLoadingPage}
            />
          }
        />
        <Route
          path="/unmonitored/hosts"
          exact
          element={
            <UnmonitoredHosts
              openRefreshToken={setShowRefreshToken}
              showRefToken={showRefreshToken}
              setLoadingData={setLoadingData}
              setLoadingPage={setLoadingPage}
            />
          }
        />
        <Route
          path="/plugins"
          exact
          element={
            <PluginComponent
              setLoadingData={setLoadingData}
              setLoadingPage={setLoadingPage}
            />
          }
        />
        <Route
          path="/first/login"
          exact
          element={
            <FirstLogin
              setLoadingData={setLoadingData}
              setLoadingPage={setLoadingPage}
            />
          }
        />

        <Route path="*" element={<LogoutComponent />} />
      </Routes>
    </>
  );
}

export default App;
