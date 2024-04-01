import React from 'react';
import Login from './Login';
import SignUp from './SignUp';

import {useEffect, useState} from 'react';
import axios from 'axios';

const Main = () => {
  useEffect(() => {
    axios.get('http://localhost:8080')
    .catch(error => console.log(error))
  }, []);


  return (
    <div>
      <h1>Sua MSA Authentication</h1>
      <Login />
      <SignUp />
    </div>
  );
};

export default Main;
