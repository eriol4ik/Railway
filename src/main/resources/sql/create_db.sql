CREATE SCHEMA `railway`;

CREATE TABLE railway.stations (
  station_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL);

CREATE TABLE railway.routes
(
    route_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    start_station_id BIGINT(20) NOT NULL,
    end_station_id BIGINT(20) NOT NULL,
    distance INT NOT NULL,
    CONSTRAINT start_station_id_fk FOREIGN KEY (start_station_id) REFERENCES stations (station_id),
    CONSTRAINT end_station_id_fk FOREIGN KEY (end_station_id) REFERENCES stations (station_id)
);

CREATE TABLE railway.trains
(
    train_id VARCHAR(4) PRIMARY KEY,
    departure_time TIME NOT NULL,
    duration INT NOT NULL
);

CREATE TABLE railway.train_routes
(
    train_route_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    unit_route_id BIGINT(20) NOT NULL,
    next_unit_route_id BIGINT(20) NOT NULL,
    train_id VARCHAR(4) NOT NULL,
    duration INT NOT NULL,
    stop_time INT NOT NULL,
    CONSTRAINT train_id_fk FOREIGN KEY (train_id) REFERENCES trains (train_id),
    CONSTRAINT unit_route_id_fk FOREIGN KEY (unit_route_id) REFERENCES routes (route_id),
    CONSTRAINT next_unit_route_id_fk FOREIGN KEY (next_unit_route_id) REFERENCES routes (route_id)
);

CREATE TABLE railway.train_routes_prices
(
    train_route_price_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    train_route_id BIGINT(20) NOT NULL,
    carriage_type VARCHAR(15) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT train_route_id_fk FOREIGN KEY (train_route_id) REFERENCES train_routes (train_route_id)
);

CREATE TABLE railway.carriages
(
    carriage_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    train_id VARCHAR(4) NOT NULL,
    carriage_number INT NOT NULL,
    carriage_type VARCHAR(15) NOT NULL,
    CONSTRAINT train_id_fk2 FOREIGN KEY (train_id) REFERENCES trains (train_id)
);

CREATE TABLE railway.carriage_places
(
	place_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    carriage_id BIGINT(20) NOT NULL,
    place_number INT NOT NULL,
    CONSTRAINT carriage_id_fk FOREIGN KEY (carriage_id) REFERENCES carriages (carriage_id)
);

CREATE TABLE railway.places_availability
(
    place_availability_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    train_route_id BIGINT(20) NOT NULL,
    place_id BIGINT(20) NOT NULL,
    departure_date DATE NOT NULL,
    place_status INT NOT NULL,
    CONSTRAINT train_route_id_fk2 FOREIGN KEY (train_route_id) REFERENCES train_routes (train_route_id),
    CONSTRAINT place_id_fk FOREIGN KEY (place_id) REFERENCES carriage_places (place_id)
);