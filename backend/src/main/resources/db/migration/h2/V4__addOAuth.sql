alter table member add column oauth_id varchar(255);
alter table member add column oauth_type varchar(255);
alter table member alter column nickname varchar(255) null;
