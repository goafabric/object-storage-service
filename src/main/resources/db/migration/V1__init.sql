drop table if exists object_entry;

create table object_entry
(
  id           varchar primary key,
  object_name    varchar(128) not null,
  content_type varchar(128) not null,
  object_size    bigint       not null,
  data         bytea        not null
);
