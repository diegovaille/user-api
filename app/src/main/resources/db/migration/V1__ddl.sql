CREATE TABLE users
(
 id     BIGINT NOT NULL AUTO_INCREMENT,
 name   VARCHAR(256) NOT NULL,
 cpf    VARCHAR(15) UNIQUE NOT NULL,
 birth  DATE NOT NULL,
 PRIMARY KEY (id)
);