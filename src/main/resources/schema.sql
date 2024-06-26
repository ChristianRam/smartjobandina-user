create table USERS(
  ID VARCHAR(255) NOT NULL,
  NAME VARCHAR(255) NOT NULL,
  EMAIL VARCHAR(255) NOT NULL,
  PASSWORD VARCHAR(255) NOT NULL,
  IS_ACTIVE BIT NOT NULL,
  CREATE_DATE TIMESTAMP NOT NULL,
  LAST_MODIFIED TIMESTAMP,
  LAST_LOGIN TIMESTAMP NOT NULL,
  ROLE VARCHAR(255) NOT NULL,
  PRIMARY KEY(ID)
);

create table PHONES(
  ID VARCHAR(255) NOT NULL,
  NUMBER VARCHAR(255) NOT NULL,
  CITYCODE VARCHAR(255) NOT NULL,
  COUNTRYCODE VARCHAR(255) NOT NULL,
  USER_ID VARCHAR(255) NOT NULL,
  PRIMARY KEY(ID),
  FOREIGN KEY(USER_ID) references USERS(ID)
);

create table TOKENS(
  ID INT AUTO_INCREMENT PRIMARY KEY,
  TOKEN VARCHAR(255) NOT NULL,
  TOKENTYPE VARCHAR(255) NOT NULL,
  COUNTRYCODE VARCHAR(255) NOT NULL,
  USER_ID VARCHAR(255) NOT NULL,
  REVOKED BIT NOT NULL,
  EXPIRED BIT NOT NULL,
  FOREIGN KEY (USER_ID) references USERS(ID)
);