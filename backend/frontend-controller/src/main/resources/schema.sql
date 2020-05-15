DROP TABLE IF EXISTS t_user CASCADE;
CREATE TABLE t_user(
	id SERIAL PRIMARY KEY,
	username VARCHAR(32) UNIQUE NOT NULL,
	email VARCHAR(254) UNIQUE NOT NULL,
	password VARCHAR(128) NOT NULL,
	telegram_username VARCHAR(32) UNIQUE NOT NULL,
	telegram_user_id INTEGER UNIQUE,
	vat_number VARCHAR(11)
);

DROP TABLE IF EXISTS gateway CASCADE;
CREATE TABLE gateway(
    id SERIAL PRIMARY KEY,
    owner_id INTEGER NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES t_user(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS device CASCADE;
CREATE TABLE device(
	id SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
	ip_address VARCHAR(39) UNIQUE NOT NULL,
	brand VARCHAR(32) NOT NULL,
	model VARCHAR(32) NOT NULL,
    frequency INTEGER NOT NULL,
    gateway_id INTEGER NOT NULL,
    owner_id INTEGER NOT NULL,
    FOREIGN KEY (gateway_id) REFERENCES gateway(id) ON DELETE CASCADE,
	FOREIGN KEY (owner_id) REFERENCES t_user(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS measure_config CASCADE;
CREATE TABLE measure_config(
	id SERIAL PRIMARY KEY,
	name VARCHAR(32) NOT NULL,
	format VARCHAR(32) NOT NULL,
	threshold FLOAT NOT NULL,
	threshold_greater BOOLEAN NOT NULL,
    influential BOOLEAN NOT NULL,
	device_id INTEGER NOT NULL,
	UNIQUE (id, name),
    FOREIGN KEY (device_id) REFERENCES device(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS measure_data CASCADE;
CREATE TABLE measure_data(
    time TIMESTAMPTZ NOT NULL,
    value FLOAT NOT NULL,
    measure_id INTEGER NOT NULL,
    PRIMARY KEY (time, measure_id),
    FOREIGN KEY (measure_id) REFERENCES measure_config(id) ON DELETE CASCADE
);

CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;

SELECT create_hypertable('measure_data', 'time', 'measure_id', 2);