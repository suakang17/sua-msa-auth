import React from 'react';
import Login from './Login';
import SignUp from './SignUp';
import AdminPage from './AdminPage';
import MemberView from './MemberView';

import {useEffect, useState} from 'react';
import axios from 'axios';


const Popup = ({ message }) => {
  return (
    <div className="popup">
      <div className="popup-content">
        <p>{message}</p>
      </div>
    </div>
  );
};

const Main = () => {
  useEffect(() => {
    axios.get('http://localhost:8080/main')
    // .then(console.log("connected localhost:8080/main"))
    .catch(error => console.log(error))
  }, []);

  const [isPopupOpen, setPopupOpen] = useState(false);
  const [popupMessage, setPopupMessage] = useState('');
  const [userRole, setUserRole] = useState('');


  const openPopup = (message) => {
    setPopupMessage(message);
    setPopupOpen(true);
  };

  const closePopup = () => {
    setPopupOpen(false);
  };

  const handleRegisterSuccess = (data) => {
    console.log('회원가입 성공:', data);
    openPopup('회원가입이 성공적으로 완료되었습니다. 로그인 후 이용해주세요.');
  };

  const handleLogin = (role) => {
    if (role === false) {
      openPopup('아이디와 비밀번호를 확인해주세요.');
    }
    // success
      // admin or member
    setUserRole(role);
    
    
  }

  return (
    <div>
      <h1>Sua MSA Authentication</h1>
      {isPopupOpen && <Popup message={popupMessage} />}
      {userRole === 'ADMIN' ? (
        <AdminPage />
      ) : userRole === 'MEMBER' ? (
        <MemberView />
      ) : (
        <Login onLoginHandle={handleLogin} />
      )}
      <SignUp onRegisterSuccess={handleRegisterSuccess} />
    </div>
  );
};

export default Main;
