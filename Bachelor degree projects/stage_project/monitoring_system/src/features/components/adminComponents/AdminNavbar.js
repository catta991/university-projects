import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import "../../../css/adminNavbar.css";
function AdminNavbar({ currentPage }) {
  // invoco una funzione di appoggio che setta i vari classname dei vari elementi della navbar
  let currentUser = useSelector((state) => state.user);
  const activeClassName = "nav-item nav-link active";
  const noActiveClassName = "nav-item nav-link";

  return (
    <nav className="navbar navbar-expand-lg">
      <span className="navbar-brand mb-0 h1">Monitoring System</span>
      <button
        className="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarNavAltMarkup"
        aria-controls="navbarNavAltMarkup"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarNavDropdown">
        <div className="navbar-nav">
          <Link
            className={
              currentPage === "home" ? activeClassName : noActiveClassName
            }
            to={currentPage === "home" ? "#" : "/admin"}
          >
            Home
          </Link>

          <Link
            className={
              currentPage === "users" ? activeClassName : noActiveClassName
            }
            to={currentPage === "users" ? "#" : "/users"}
          >
            Users
          </Link>

          <Link
            className={
              currentPage === "contactGroups"
                ? activeClassName
                : noActiveClassName
            }
            to={currentPage === "contactGroups" ? "#" : "/contactGroups"}
          >
            Contact Groups
          </Link>

          <Link
            className={
              currentPage === "monitoredHosts"
                ? activeClassName
                : noActiveClassName
            }
            to={currentPage === "monitoredHost" ? "#" : "/monitored/hosts"}
          >
            Monitored Hosts
          </Link>

          {currentUser &&
            currentUser.decodedAccessToken.roles[0] === "ROLE_ADMIN" && (
              <Link
                className={
                  currentPage === "unmonitoredHosts"
                    ? activeClassName
                    : noActiveClassName
                }
                to={
                  currentPage === "unmonitoredHosts"
                    ? "#"
                    : "/unmonitored/hosts"
                }
              >
                Unmonitored Hosts
              </Link>
            )}

          <Link className="nav-item nav-link" to="/logout">
            Logout
          </Link>
        </div>
      </div>
    </nav>
  );
}

export default AdminNavbar;
