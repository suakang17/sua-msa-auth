import React, { useState, useEffect } from 'react';

const AdminMemberManagement = () => {
  const [members, setMembers] = useState([]);

  useEffect(() => {
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
