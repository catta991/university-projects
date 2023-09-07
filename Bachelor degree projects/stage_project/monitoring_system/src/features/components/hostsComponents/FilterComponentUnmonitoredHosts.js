import React from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import MultiSelectLite from "react-multiple-select-dropdown-lite";
import { getSingleUserInfo } from "../../utilFunctions/fetchFunction";
import { useState, useMemo, useEffect, useImperativeHandle } from "react";
import { useSelector, useDispatch } from "react-redux";

const Input = styled.input.attrs((props) => ({
  type: "text",
  size: props.small ? 5 : undefined,
}))`
  height: 32px;
  width: 200px;
  border-radius: 3px;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
  border: 1px solid #e5e5e5;
  padding: 0 32px 0 16px;
  margin-top: 15px;
  margin-left: -150px;
`;

const ClearButton = styled.button`
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  height: 32px;
  width: 33px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #e5e5e5;
  margin-right: 2px;
  margin-top: 15px;
  background-color: #f44336;
`;

const AddButton = styled.button`
  text-align: center;
  align-items: left;
  margin-top: -40px;
  margin-left: -200px;
  margin-right: 700px;
  font-size: 20px;
  display: inline-block;
`;

const Div = styled.div`
  margin-right: 73%;
  margin-top: 15px;
  text-align: left;
  font-size: 23px;
  margin-left: -30px;
`;

const RefreshButton = styled.button`
  text-align: center;
  align-items: center;
  margin-left: 3px;
  font-size: 20px;
`;

const FilterComponentUnmonitoredHost = ({
  onAdd,
  filterText,
  onFilter,
  onClear,
  refetch,
  contactGroupChangheHandler,
}) => {
  let user = useSelector((state) => state.user);

  const [contactGroupMultiselect, setContactGroupMultiselect] = useState([]);
  const callGetSingleUserInfo = async () => {
    const data = await getSingleUserInfo(user);

    let contactGroupMulti = [];

    for (let i = 0; i < data.contactGroups.length; i++) {
      contactGroupMulti.push({
        label: data.contactGroups[i],
        value: data.contactGroups[i],
      });
    }

    setContactGroupMultiselect(contactGroupMulti);
  };

  useEffect(() => {
    callGetSingleUserInfo();

    return () => {};
  }, []);

  return (
    <>
      <Div>
        Unmonitored Hosts
        <RefreshButton
          className="btn btn-sm"
          title="refresh"
          onClick={() => refetch()}
        >
          <i
            className="bi bi-arrow-clockwise"
            style={{ textAlign: "center" }}
          ></i>
        </RefreshButton>
      </Div>
      <Input
        id="search"
        type="text"
        placeholder="Search by host name "
        value={filterText}
        onChange={onFilter}
      />
      <ClearButton onClick={onClear}>X</ClearButton>

      <MultiSelectLite
        options={contactGroupMultiselect}
        placeholder={"select contact groups"}
        onChange={(val) => contactGroupChangheHandler(val)}
        style={{ marginTop: "15px", marginRight: "900px", marginLeft: "-11px" }}
      />
      <AddButton className="btn btn-outline-success btn-sm" onClick={onAdd}>
        Add to monitoring
      </AddButton>
    </>
  );
};

export default FilterComponentUnmonitoredHost;
