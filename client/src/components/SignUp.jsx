import React, { useState } from 'react';
import axios from 'axios';
import '../styles.css';

const Register = ({ onRegisterSuccess }) => {
  const [loginId, setId] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [gender, setGender] = useState('');
  const [birth, setBirth] = useState('');

  const handleGenderChange = (e) => {
    setGender(e.target.value);
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/v1/members/signup', {
        loginId,
        password,
        name,
        email,
        gender,
        birth
      });
      if (response.status === 200) {
        onRegisterSuccess(response.status);
      }
    } catch (error) {
      console.error('회원가입 에러:', error);
    }
  };

  return (
    <div className="container">
      <div className="form-container">
        <h2>Register</h2>
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
          <div>
            <label>Name:</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
          <div>
            <label>Email:</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div>
        <label>Gender:</label>
          <select value={gender} onChange={handleGenderChange}>
            <option value="">Select Gender</option>
            <option value="F">Female</option>
            <option value="M">Male</option>
          </select>
      </div>
          <div>
            <label>Birth:</label>
            <input
              type="date"
              value={birth}
              onChange={(e) => setBirth(e.target.value)}
            />
          </div>
          <button type="submit">Register</button>
        </form>
      </div>
    </div>
  );
};

export default Register;
