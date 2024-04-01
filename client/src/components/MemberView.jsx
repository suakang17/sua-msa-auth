import React, { useState, useEffect } from 'react';

const MemberView = () => {
  const [member, setMember] = useState(null);

  useEffect(() => {
    // 여기서는 실제로 서버에서 특정 회원의 정보를 가져오는 API를 호출할 것입니다.
    // 이 예시에서는 더미 데이터를 사용하여 회원 정보를 설정합니다.
    const dummyMember = { id: 1, name: 'John Doe', email: 'john@example.com' };
    setMember(dummyMember);
  }, []);

  return (
    <div>
      <h2>Member View Page</h2>
      <h3>Member Information</h3>
      {member ? (
        <div>
          <p>ID: {member.id}</p>
          <p>Name: {member.name}</p>
          <p>Email: {member.email}</p>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default MemberView;
