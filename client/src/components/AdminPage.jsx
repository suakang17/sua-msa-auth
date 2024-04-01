import React, { useState, useEffect } from 'react';

const AdminMemberManagement = () => {
  const [members, setMembers] = useState([]);

  useEffect(() => {
    // 여기서는 실제로 서버에서 회원 목록을 가져오는 API를 호출할 것입니다.
    // 이 예시에서는 더미 데이터를 사용하여 회원 목록을 설정합니다.
    const dummyMembers = [
      { id: 1, name: 'John Doe' },
      { id: 2, name: 'Jane Smith' },
      { id: 3, name: 'Tom Johnson' }
    ];
    setMembers(dummyMembers);
  }, []);

  return (
    <div>
      <h2>Admin Member Management Page</h2>
      <h3>Member List</h3>
      <ul>
        {members.map(member => (
          <li key={member.id}>{member.name}</li>
        ))}
      </ul>
    </div>
  );
};

export default AdminMemberManagement;
