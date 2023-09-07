import { Button } from "react-bootstrap";
import { Modal } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import isExpiredToken from "../../utilFunctions/isExipredToken";
import { useRefreshTokenMutation } from "../../reduxApi/refreshApi";
import { useEffect } from "react";
import { refreshToken } from "../../slice/userSlice";

function RefreshToken({ showRefresh, handleClose, yesRefreshHandler }) {
  let user = useSelector((state) => state.user);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [refreshTokenApi, { isLoading, isSuccess, data: tokens = null }] =
    useRefreshTokenMutation();

  useEffect(() => {
    console.log("isLoading", isLoading);
    console.log("isSuccess", isSuccess);
    console.log("tokens", tokens);

    if (!isLoading && isSuccess && tokens !== null) {
      console.log(tokens);
      const newTokens = {
        newAccessToken: tokens.access_token,
        newRefreshToken: tokens.refresh_token,
      };

      dispatch(refreshToken(newTokens));

      yesRefreshHandler();
    }

    return () => {};
  }, [isLoading, isSuccess, tokens]);

  const onClickYesHandler = () => {
    console.log("in on click yes");

    if (isExpiredToken(user.refreshTokenExipration)) {
      console.log("refresh token exipired");
      alert("refresh token expired please login again");
      navigate("/logout");
    } else {
      refreshTokenApi();
    }
  };

  return (
    <Modal
      show={showRefresh}
      onHide={handleClose}
      backdrop="static"
      keyboard={false}
    >
      <Modal.Header>
        <Modal.Title>Token expired</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p> Do you want extend the session?</p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="outline-danger" onClick={handleClose}>
          No
        </Button>
        <Button variant="outline-success" onClick={() => onClickYesHandler()}>
          Yes
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default RefreshToken;
