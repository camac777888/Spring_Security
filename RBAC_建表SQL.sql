
create table user(
id int(10) AUTO_INCREMENT PRIMARY KEY,
name varchar(50) NOT NULL,
username varchar(50) NOT NULL,
password varchar(50) NOT NULL,
createDate date ,
lastLoginDate date ,
enabled int(5)  NOT NULL,
accountNonExpired int(5) NOT NULL ,
accountNonLocked  int(5) NOT NULL ,
credentialsNonExpired  int(5) NOT NULL DEFAULT 50
)


create table role(
id int(10) AUTO_INCREMENT PRIMARY KEY,
roleName varchar(50) ,
roleInfo varchar(50) 
)



create table auth (
id int(10) AUTO_INCREMENT PRIMARY KEY,
authName varchar(50) ,
authInfo varchar(50) 
)


create table user_role(
userId int(10) ,
roleId int(10) ,
FOREIGN KEY (userId) REFERENCES user(id) ,
FOREIGN KEY (roleId) REFERENCES role(id) 
)


create table role_auth(
authId int(10) ,
roleId int(10) ,
FOREIGN KEY (authId) REFERENCES auth(id) ,
FOREIGN KEY (roleId) REFERENCES role(id) 
)