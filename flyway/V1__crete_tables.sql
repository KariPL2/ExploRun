CREATE TABLE users(
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'RUNNER', 'PREMIUM_RUNNER') DEFAULT 'RUNNER' NOT NULL
);

CREATE TABLE route (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,

                       user_id BINARY(16) NOT NULL,

                       profile VARCHAR(50) NOT NULL,
                       preference VARCHAR(50) NOT NULL,
                       total_distance DOUBLE NOT NULL,
                       total_duration DOUBLE NOT NULL,
                       geometry_polyline TEXT,
                       original_json TEXT,

                       CONSTRAINT fk_route_user
                           FOREIGN KEY (user_id)
                               REFERENCES users(id)
                               ON DELETE CASCADE -- Usuwa trasy, gdy użytkownik jest usuwany
);


CREATE TABLE route_waypoints (
                                 route_id BIGINT NOT NULL,
                                 lng DOUBLE NOT NULL,
                                 lat DOUBLE NOT NULL,

                                 CONSTRAINT pk_route_waypoints PRIMARY KEY (route_id, lng, lat), -- Klucz złożony

                                 CONSTRAINT fk_waypoints_route
                                     FOREIGN KEY (route_id)
                                         REFERENCES route(id)
                                         ON DELETE CASCADE
);

CREATE TABLE segment (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         route_id BIGINT NOT NULL,
                         distance DOUBLE NOT NULL,
                         duration DOUBLE NOT NULL,

                         CONSTRAINT fk_segment_route
                             FOREIGN KEY (route_id)
                                 REFERENCES route(id)
                                 ON DELETE CASCADE
);

CREATE TABLE step (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      segment_id BIGINT NOT NULL,
                      distance DOUBLE NOT NULL,
                      duration DOUBLE NOT NULL,
                      type INT NOT NULL,
                      instruction VARCHAR(255),
                      street_name VARCHAR(100),

                      CONSTRAINT fk_step_segment
                          FOREIGN KEY (segment_id)
                              REFERENCES segment(id)
                              ON DELETE CASCADE
);
