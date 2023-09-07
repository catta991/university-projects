import React from "react";
import styled from "styled-components";

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
  margin-left: -25px;
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

const RefreshButton = styled.button`
  text-align: center;
  align-items: center;
  margin-left: 3px;
  font-size: 20px;
`;

const Div = styled.div`
  margin-right: 73%;
  margin-top: 15px;
  text-align: left;
  font-size: 23px;
  margin-left: -20px;
`;

const FilterComponentHosts = ({ refetch, filterText, onFilter, onClear }) => (
  <>
    <Div>
      Hosts
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
  </>
);

export default FilterComponentHosts;
