import React, { useState } from 'react';
import axios from 'axios';
import '../styles.css';

const Login = ({ onLoginHandle }) => {
  const [loginId, setId] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/v1/members/login', {
        loginId,
        password,
      });
      console.log(response.data);
      // 서버에서 반환된 응답에 JWT가 있는지 확인
      if (response.data) {
        
        localStorage.setItem('accesstoken', response.data.accessToken);
        localStorage.setItem('refreshtoken', response.data.refreshToken);
        console.log('로그인 성공', response.data.accessToken);
        
        onLoginHandle(true);
      } else {
        // JWT가 없는 경우 처리
        console.error('로그인 에러: JWT가 없습니다.');
        onLoginHandle(false);
      }
    } catch (error) {
      console.error('로그인 에러:', error);
      onLoginHandle(false);
    }
  };

  return (
    <div className="container">
      <div className="form-container">
        <h2>Login</h2>
        <form onSubmit={handleSubmit}>
          <div>
            <label>Id:</label>
            <input
              type="text"
              value={loginId}
              onChange={(e) => setId(e.target.value)}
            />
          </div>
          <div>
            <label>Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <button type="submit">Login</button>
        </form>
      </div>
    </div>
  );
};

export default Login;
