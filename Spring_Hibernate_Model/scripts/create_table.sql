-- Table : Account
-- Type : Master
-- Desc : To store all account information
create table account
	(nAccountId int,
	sName varchar(255) not null,
	sGroup varchar(100) not null,
	nCreditPercent decimal(5,2),
	sLocation varchar(100) not null,
	constraint pk_account_nAccountId primary key(nAccountId),
	constraint uq_account_nAccountId unique(sName)
	);
