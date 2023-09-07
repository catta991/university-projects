FROM node:latest

WORKDIR /usr/app

COPY package.json .

COPY package-lock.json .


RUN npm install
RUN npm install @reduxjs/toolkit
RUN npm install bootstrap
RUN npm install jwt-decode
RUN npm install react-router-dom@6
RUN npm install react-data-table-component styled-components
RUN npm i bootstrap-icons
RUN npm install react-bootstrap bootstrap@4.6.0
RUN npm i react-multiple-select-dropdown-lite
RUN npm i react-toastify
RUN npm i react-multi-select-component
RUN npm i bcryptjs

COPY . .

CMD ["npm", "start"]