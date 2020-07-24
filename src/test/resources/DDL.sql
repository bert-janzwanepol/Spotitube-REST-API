USE test_spotitube;

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
        ON DELETE CASCADE,

    FOREIGN KEY (playlistid)
        REFERENCES Playlists(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

INSERT INTO Gebruikers (username, `password`, firstname, lastname)
VALUES
('Test Username 1', '$2a$10$/FX8W8UOGm1Fdwgr8/Mvte.Bd7BZsdVcpn3eY5Pvia3DJkyhAwCWG', 'Test Firstname 1', 'Test Lastname 1'),
('Test Username 2', '$2a$10$gUCZsFYir9KmVmmIoPze/uRXPW.4ZVIwpKOLkENlE/GHbTZRLpfTC', 'Test Firstname 2', 'Test Lastname 2'),
('Test Username 3', '$2a$10$yIzIauMQKb69uwiThH1Z3us.kDd6BxeQHgXZwl6H8YLn82GrlI4Hq', 'Test Firstname 3', 'Test Lastname 3'),
('Test Username 4', '$2a$10$u6.0evoJHD3oGgnuSrJhX.GVNXsrHcvDYo7tNDHwt69XWEiSxheha', 'Test Firstname 4', 'Test Lastname 4');

INSERT INTO Playlists (name, owner)
VALUES
('Test Playlist 1', 'Test Username 1'),
('Test Playlist 2', 'Test Username 2'),
('Test Playlist 3', 'Test Username 3'),
('Test Playlist 4', 'Test Username 4');

INSERT INTO Tracks (title, performer, duration, playcount)
VALUES
('Video 1', 'Test Artist 1', 500, 10),
('Video 2', 'Test Artist 2', 120, 3),
('Song 3', 'Test Artist 3', 600, 1),
('Song 4', 'Test Artist 4', 5000, 20),
('Video 5', 'Test Artist 5', 70, 1);

INSERT INTO Songs (id, album)
VALUES
(3, 'Test album 1'),
(4, 'Test album 2');

INSERT INTO Videos (id, publicationDate, description)
VALUES
(1, '12-03-20', 'Test description 1'),
(2, '10-01-1993', 'Test description 2'),
(5, '10-10-2010', 'Test description 5');

INSERT INTO Track_In_Playlist (trackid, playlistid, offlineAvailable)
VALUES
(1, 1, 0),
(2, 1, 0),
(3, 1, 1),
(4, 1, 0),
(5, 1, 1),
(1, 2, 1),
(2, 2, 1),
(3, 2, 1),
(1, 3, 1),
(2, 3, 2),
(4, 4, 0);
