rs.status();
db.createUser({user: 'admin', pwd: 'admin', roles: [ { role: 'root', db: 'admin' } ]})

use admin;

db.createUser(
  {
    user: "masterx",
    pwd: "masterx",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
  }
);
