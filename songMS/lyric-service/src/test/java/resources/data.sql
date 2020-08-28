drop table if exists lyrics;
create table lyrics
(
    id     varchar(100) unique,
    lyrics varchar(100)
);

insert into lyrics(id, lyrics) VALUES ('How to Save a Life', 'hi')
