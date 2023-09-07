import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import "../../../css/adminNavbar.css";
function UserNavbar({ currentPage }) {
  // invoco una funzione di appoggio che setta i vari classname dei vari elementi della navbar

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
          <Link className="nav-item nav-link" to="/logout">
            Logout
          </Link>
        </div>
      </div>
    </nav>
  );
}

export default UserNavbar;
