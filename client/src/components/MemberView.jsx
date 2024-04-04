import React, { useState, useEffect } from 'react';

const MemberView = () => {
  const [member, setMember] = useState(null);

  useEffect(() => {
    // const member = { id: 1, name: 'John Doe', email: 'john@example.com' };
    setMember(member);
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
