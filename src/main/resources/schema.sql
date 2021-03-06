drop table category if exists;
drop table pet if exists;
drop table pet_tag if exists;
drop table tag if exists;
drop table urls if exists;
create table category (id bigint not null, name varchar(255) not null, primary key (id));
create table pet (id bigint not null, name varchar(255) not null, status varchar(25), category bigint, primary key (id));
create table pet_tag (pet_id bigint not null, tag_id bigint not null);
create table tag (id bigint not null, name varchar(255) not null, primary key (id));
create table urls (pet_id bigint not null, photo_urls varchar(255));
alter table pet add constraint FK_tie3l02s0x9ysptr9xk6a4u4f foreign key (category) references category;
alter table pet_tag add constraint FK_adbkv1wqtncws1pdy01j5ksur foreign key (tag_id) references tag;
alter table pet_tag add constraint FK_eig7rri11i9trdriw5gpv8wbt foreign key (pet_id) references pet;
alter table urls add constraint FK_ca4uh20bv6uq4gxxui5sy6gmf foreign key (pet_id) references pet;
create sequence pet_seq;

create table pet_image (id bigint not null, content_type varchar(255), image blob(255), name varchar(255), pet_id bigint, primary key (id));

create sequence pet_im_seq;