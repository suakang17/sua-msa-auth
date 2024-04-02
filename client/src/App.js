import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Login from './components/Login';
import SignUp from './components/SignUp';
import Main from './components/Main';
import './styles.css';

const App = () => {
  
  return (
    <div className='App'>
        <Routes>
          <Route path="/login" element={ <Login/> } />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/main" element={ <Main/> } />
        </Routes>
    </div>
  );
};

export default App;
