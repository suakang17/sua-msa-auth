import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Login from './components/Login';
import SignUp from './components/SignUp';
import Main from './components/Main';
import AdminPage from './components/AdminPage';
import MemberView from './components/MemberView';
import './styles.css';

const App = () => {
  return (
    <div className='App'>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/main" element={<Main />} />
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/member" element={<MemberView />} />
        </Routes>
    </div>
  );
};

export default App;
