-- Sequence for Admin table
CREATE SEQUENCE if not exists admin_id_seq
    START 1
    INCREMENT 1
    CACHE 1;

CREATE TABLE if not exists admin (
                                     admin_id INT PRIMARY KEY DEFAULT nextval('admin_id_seq'),
                                     name VARCHAR(50) NOT NULL,
                                     email VARCHAR(100) UNIQUE NOT NULL,
                                     password VARCHAR(100) NOT NULL
);

ALTER SEQUENCE admin_id_seq OWNED BY admin.admin_id;


-- Sequence for Users table
CREATE SEQUENCE if not exists users_id_seq
    START 1
    INCREMENT 1
    CACHE 1;

CREATE TABLE if not exists users (
                                     user_id INT PRIMARY KEY DEFAULT nextval('users_id_seq'),
                                     username VARCHAR(50) UNIQUE NOT NULL,
                                     email VARCHAR(100) UNIQUE NOT NULL,
                                     password VARCHAR(100) NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER SEQUENCE users_id_seq OWNED BY users.user_id;


-- Events table
CREATE TABLE if not exists events (
                                      event_id SERIAL PRIMARY KEY,
                                      user_id INT NOT NULL,
                                      event_name VARCHAR(100) NOT NULL,
                                      event_date DATE NOT NULL,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


-- Invitations table
CREATE TABLE if not exists invitations (
                                           invitation_id SERIAL PRIMARY KEY,
                                           event_id INT NOT NULL,
                                           user_id INT NOT NULL,
                                           status VARCHAR(20) DEFAULT 'PENDING',
                                           constraint unique_invitation UNIQUE (event_id, user_id),
                                           FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
                                           FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


-- Movies table
CREATE TABLE if not exists movies (
                                      movie_id SERIAL PRIMARY KEY,
                                      title VARCHAR(100) NOT NULL,
                                      genre VARCHAR(50),
                                      release_date DATE,
                                      rating NUMERIC(3, 2) CHECK (rating >= 0 AND rating <= 10)
);


-- Votes table
CREATE TABLE if not exists votes (
                                     vote_id SERIAL PRIMARY KEY,
                                     movie_id INT NOT NULL,
                                     user_id INT NOT NULL,
                                     rating INT NOT NULL CHECK (rating BETWEEN 1 AND 10),
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT unique_user_movie_vote UNIQUE (user_id, movie_id),
                                     FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE,
                                     FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


-- Suggestions table
CREATE TABLE if not exists suggestions (
                                           suggestion_id SERIAL PRIMARY KEY,
                                           event_id INT NOT NULL,
                                           user_id INT NOT NULL,
                                           movie_id INT NOT NULL,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           CONSTRAINT unique_suggestion UNIQUE (event_id, user_id, movie_id),
                                           FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
                                           FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);



-- Trigger and function to update the movie rating when votes are added or updated
CREATE OR REPLACE FUNCTION update_movie_rating()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE movies
    SET rating = (
        SELECT AVG(rating)
        FROM votes
        WHERE movie_id = NEW.movie_id
    )
    WHERE movie_id = NEW.movie_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPlACE TRIGGER trg_update_movie_rating
    AFTER INSERT OR UPDATE ON votes
    FOR EACH ROW
EXECUTE FUNCTION update_movie_rating();


CREATE OR REPLACE FUNCTION check_event_creator()
    RETURNS TRIGGER AS $$
BEGIN
    -- Eğer davetiye gönderilen kullanıcı etkinliği oluşturan kişi ise hata fırlat
    IF EXISTS (
        SELECT 1
        FROM events
        WHERE events.event_id = NEW.event_id AND events.user_id = NEW.user_id
    ) THEN
        RAISE EXCEPTION 'Event kurucusuna davetiye gönderilemez.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger'ı oluştur
CREATE TRIGGER check_event_creator_trigger
    BEFORE INSERT ON invitations
    FOR EACH ROW
EXECUTE FUNCTION check_event_creator();

CREATE OR REPLACE FUNCTION send_invitation_with_cursor(
    event_id_input INT,
    current_user_id_input INT,
    invited_user_id_input INT,
    status_input TEXT
)
    RETURNS TEXT AS $$
DECLARE
    event_owner_id INT;
    event_cursor CURSOR FOR
        SELECT user_id
        FROM events
        WHERE event_id = event_id_input;
BEGIN
    -- Cursor'ı aç
    OPEN event_cursor;

    -- Cursor'dan değer al ve etkinlik sahibinin ID'sini kontrol et
    FETCH event_cursor INTO event_owner_id;

    -- Eğer etkinlik yoksa hata fırlat
    IF NOT FOUND THEN
        CLOSE event_cursor;
        RETURN 'Etkinlik bulunamadı.';
    END IF;

    -- Cursor'ı kapat
    CLOSE event_cursor;

    -- Kullanıcının etkinliği oluşturup oluşturmadığını kontrol et
    IF event_owner_id != current_user_id_input THEN
        RETURN 'Sadece etkinlik sahibi davetiye gönderebilir.';
    END IF;

    -- Davetiyeyi ekle
    INSERT INTO invitations (event_id, user_id, status)
    VALUES (event_id_input, invited_user_id_input, status_input);

    RETURN 'Davetiye başarıyla gönderildi.';
END;
$$ LANGUAGE plpgsql;


CREATE TYPE user_record_type AS (
                                    user_id INT,
                                    username VARCHAR,
                                    email VARCHAR,
                                    password VARCHAR
                                );

CREATE OR REPLACE FUNCTION get_user_by_credentials(user_name VARCHAR, user_password VARCHAR)
    RETURNS SETOF user_record_type AS $$
BEGIN
    RETURN QUERY
        SELECT
            user_id,
            username,
            email,
            password
        FROM
            users
        WHERE
            username=user_name AND password=user_password;


END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE VIEW get_movies AS
    SELECT *
    FROM movies
    ORDER BY movie_id;


CREATE OR REPLACE VIEW get_users AS
    SELECT *
    FROM users
    ORDER BY user_id;


CREATE OR REPLACE FUNCTION get_user_events(current_user_id INT)
    RETURNS TABLE(
                     event_id INT,
                     event_name VARCHAR,
                     event_date DATE,
                     user_id INT
                 ) AS $$
BEGIN
    RETURN QUERY
        -- First part: events where the user is the organizer
        SELECT
            e.event_id,
            e.event_name,
            e.event_date,
            e.user_id
        FROM
            events e
        WHERE
            e.user_id = current_user_id

        UNION

        -- Second part: events where the user is invited and accepted the invitation
        SELECT
            e.event_id,
            e.event_name,
            e.event_date,
            e.user_id
        FROM
            invitations i
                JOIN
            events e ON e.event_id = i.event_id
        WHERE
            i.user_id = current_user_id AND i.status = 'ACCEPTED';
END;
$$ LANGUAGE plpgsql;