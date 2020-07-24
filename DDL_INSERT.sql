DROP DATABASE IF EXISTS spotitube;
CREATE DATABASE spotitube;
USE spotitube;

-- DROP table videos;
-- DROP table songs;
-- DROP table track_in_playlist;
-- DROP table tracks;
-- DROP table playlists;
-- DROP table gebruikers;

CREATE TABLE `Gebruikers` (
  `username` varchar(50) NOT NULL,
  `password` varchar(150) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  PRIMARY KEY (`username`)
);

CREATE TABLE `Playlists` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `owner` VARCHAR(50) NOT NULL,
  FOREIGN KEY (owner)
	REFERENCES Gebruikers(username)
    ON DELETE CASCADE,
  PRIMARY KEY (`id`)
);

CREATE TABLE `Tracks` (
	`id` INT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(100) NOT NULL,
    `performer` VARCHAR(100) NOT NULL,
    `duration` INT NOT NULL,
    `playcount` INT NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `Songs` (
	`id` INT NOT NULL,
	`album` VARCHAR(150) NOT NULL,
    
    FOREIGN KEY (id)
		REFERENCES Tracks(id)
        ON DELETE CASCADE,
        
	PRIMARY KEY (`id`)
);

CREATE TABLE `Videos` (
	`id` INT NOT NULL,
	`publicationDate` VARCHAR(20) NOT NULL,
    `description` VARCHAR(300),
    
    FOREIGN KEY (id)
		REFERENCES Tracks(id)
        ON DELETE CASCADE,
        
	PRIMARY KEY (`id`)
);

CREATE TABLE `Track_In_Playlist` (
	`trackid` INT NOT NULL,
    `playlistid` INT NOT NULL,
    `offlineAvailable` boolean NOT NULL,
      FOREIGN KEY (trackid)
		REFERENCES Tracks(id)
        ON UPDATE CASCADE
		ON DELETE NO ACTION,
        
	FOREIGN KEY (playlistid)
		REFERENCES Playlists(id)
        ON UPDATE CASCADE
		ON DELETE CASCADE,
        
	PRIMARY KEY (trackid, playlistid)
);

INSERT INTO Gebruikers (username, `password`, firstname, lastname)
VALUES
('User_1', '$2a$10$wZp4YSYTyRFdy3cQEzukheGMeH0cwnNjsBNXbl2ppoEcapwHPdD9i', 'User1', 'One'),
('User_2', '$2a$10$9y7iRu8Ocg/9B5eWo0quaOSMlNZMNvz2uortzqaJbXBkFw9/yHtbi', 'User2', 'Two'),
('User_3', '$2a$10$Iapj2kzLd2A0vVJ1VUlcS.sItybKjaNL5iLqQkmLDTp/YyltfZV9W', 'User3', 'Three'),
('User_4', '$2a$10$LQrjQU2Fbc.x6/F3ITA5juB.NJExiVsS.pKSi7aiqJh7hvoTBnBai', 'User4', 'Four');

INSERT INTO Playlists (id, name, owner)
VALUES
(1, 'User_1 playlist', 'User_1'),
(2, 'User_2 playlist', 'User_2'),
(3, 'User_3 playlist', 'User_3'),
(4, 'User_4 playlist', 'User_4');

INSERT INTO Tracks (id, title, performer, duration, playcount)
VALUES
(1, 'Video 1', 'Artist juan', 500, 10),
(2, 'Video 2', 'Artist dos', 120, 3),
(3, 'Song 3', 'Artist tres', 600, 1),
(4, 'Song 4', 'Artist quatro', 5000, 20),
(5, 'Video 5', 'Artist dos', 70, 1);

INSERT INTO Songs (id, album)
VALUES
(3, 'album 2'),
(4, 'album 1');

INSERT INTO Videos (id, publicationDate, description)
VALUES
(1, '12-03-20', 'description of video 1'),
(2, '10-01-1993', 'description of video 2'),
(5, '10-10-2010', 'description of video 5');

INSERT INTO Track_In_Playlist (trackid, playlistid, offlineAvailable)
VALUES
(3, 2, 1),
(1, 4, 1),
(2, 4, 2),
(1, 1, 0),
(2, 1, 0),
(1, 2, 1),
(2, 2, 1),
(1, 3, 1),
(2, 3, 0),
(5, 1, 0),
(4, 1, 1),
(3, 1, 1);
