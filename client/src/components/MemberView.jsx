import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';


const MemberView = () => {
  const [member, setMember] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = async () => {
    
    try {
      const response = await axios.post('http://localhost:8080/api/v1/members/logout' , member, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('accesstoken')}` // 토큰을 헤더에 포함
        }
      });
      if (response.status === 200) {
        localStorage.removeItem('accesstoken');
        localStorage.removeItem('refreshtoken');
        console.log('로그아웃 성공');
        navigate('/main');
      }
    } catch (error) {
      console.error(error);
    }
  }
  
  const handleModify = () => {
    // axios.post('/logout').then(() => {
    //   console.log('로그아웃 성공');
    //   localStorage.removeItem('accesstoken');
    //   localStorage.removeItem('refreshtoken');
    // }).catch(error => {
    //   console.error('로그아웃 실패', error);
    // });
  }

  useEffect(() => {
    if (location.state && location.state.responseData) {
      setMember(location.state.responseData);
    }
  }, [location.state]);

  return (
    <div>
      <h2>Member View Page</h2>
      <h3>Member Information</h3>
      {member ? (
        <div>
          <p>ID: {member.loginId}</p>
          <p>Name: {member.name}</p>
          <p>Email: {member.email}</p>
          <p>Birth: {member.birth[0]}.{member.birth[1]}.{member.birth[2]}</p>
          <p>Gender: {member.gender}</p>
          <button onClick={handleLogout}>Logout</button>
          <button onClick={handleModify}>수정</button>
        </div>
        
      ) : (
        <p>No member information available.</p>
      )}
    </div>
  );
};

export default MemberView;