alter table trip add column route_image_url varchar(255);
alter table post add column route_image_url varchar(255);
alter table post rename column image_url to post_image_url;
