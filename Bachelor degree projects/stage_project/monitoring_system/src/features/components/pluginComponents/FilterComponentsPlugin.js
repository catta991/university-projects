import React from "react";
import { useNavigate } from "react-router-dom";
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
  margin-left: -120px;
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
  margin-top: 25px;
  margin-left: 10px;
  margin-right: 520px;
  font-size: 20px;
  display: inline-block;
`;

const BackButton = styled.button`
  text-align: center;
  align-items: left;
  margin-top: -40px;
  margin-left: 10px;
  margin-right: 610px;
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

const ButtonDiv = styled.div`
  width: 100%;
  text-align: center;
`;

const FilterComponentsPlugin = ({
  onBack,
  filterText,
  onFilter,
  onClear,
  installPlugin,
}) => (
  <>
    <Div>Plugins</Div>
    <Input
      id="search"
      type="text"
      placeholder="Search by name "
      value={filterText}
      onChange={onFilter}
    />
    <ClearButton onClick={onClear}>X</ClearButton>

    <AddButton
      className="btn btn-outline-success btn-sm"
      onClick={installPlugin}
    >
      Install
    </AddButton>
    <BackButton className="btn btn-outline-danger btn-sm" onClick={onBack}>
      Back
    </BackButton>
  </>
);

export default FilterComponentsPlugin;
