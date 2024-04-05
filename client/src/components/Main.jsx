import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Popup from './Popup';
import Login from './Login';
import SignUp from './SignUp';

const Main = () => {
  const navigate = useNavigate();
  const [isPopupOpen, setPopupOpen] = useState(false);
  const [popupMessage, setPopupMessage] = useState('');

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

  const handleLogin = (result) => {
    if (result === false) {
      openPopup('아이디와 비밀번호를 확인해주세요.');
    } else {
      
      console.log('member');
      axios.get('http://localhost:8080/member', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('accesstoken')}` // 토큰을 헤더에 포함
      }
    })
    .then(response => {
      navigate('/member', { state: { responseData: response.data } });
      console.log(response.data);
    })
    .catch(error => {
      // 요청 실패 처리
      console.error(error);
    });
    }
  }

  return (
    <div>
      <h1>Sua MSA Authentication</h1>
      {isPopupOpen && <Popup message={popupMessage} />}
      <Login onLoginHandle={handleLogin} />
      <SignUp onRegisterSuccess={handleRegisterSuccess} />
    </div>
  );
};

export default Main;
