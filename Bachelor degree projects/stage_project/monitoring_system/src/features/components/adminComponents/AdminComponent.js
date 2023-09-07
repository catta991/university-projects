import { Link } from "react-router-dom";

import "../../../css/adminComponent.css";
import { useSelector } from "react-redux";

function AdminComponent() {
  let currentUser = useSelector((state) => state.user);

  return (
    <div className="Background">
      <div className="nameContainer">
        Welcome {currentUser && currentUser.decodedAccessToken.sub}
      </div>
      <div className="container">
        <ul className="snip1143">
          <li>
            <Link data-hover="Users " to="/users">
              Users
            </Link>
          </li>

          <li>
            <Link data-hover="Contact Groups " to="/contactGroups">
              Contact groups
            </Link>
          </li>
          <li>
            <Link data-hover="Monitored Hosts" to="/monitored/hosts">
              Monitored Hosts
            </Link>
          </li>

          {currentUser &&
            currentUser.decodedAccessToken.roles[0] === "ROLE_ADMIN" && (
              <li>
                <Link data-hover="Unmonitored Hosts" to="/unmonitored/hosts">
                  Unmonitored Hosts
                </Link>
              </li>
            )}

          <li>
            <Link data-hover="Logout" to="/logout">
              Logout
            </Link>
          </li>
        </ul>
      </div>
    </div>
  );
}

export default AdminComponent;
