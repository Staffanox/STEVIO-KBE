drop table if exists song;

create table song
(
    id       SERIAL primary key,
    title    varchar(100),
    artist   varchar(100),
    label    varchar(100),
    released INT
);

insert into song(id,title,artist,label,released) values (1,'test','test','test',1985);
insert into song(id,title,artist,label,released) values (2,'test','test','test',1985);
