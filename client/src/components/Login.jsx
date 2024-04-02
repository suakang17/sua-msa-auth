import React, { useState } from 'react';
import axios from 'axios';
import '../styles.css';

const Login = ({ onLoginHandle }) => {
  const [loginId, setId] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/login', {
        loginId,
        password,
      });
      if (response.status === 200) {
        onLoginHandle(response.data.role);
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
