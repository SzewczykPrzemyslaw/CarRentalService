CREATE TABLE car
(
    id              BIGSERIAL PRIMARY KEY,
    type            VARCHAR(20)  NOT NULL,
    brand           VARCHAR(100) NOT NULL,
    model           VARCHAR(100) NOT NULL,
    registration_no VARCHAR(20)  NOT NULL UNIQUE,

    CONSTRAINT chk_car_type
        CHECK (type IN ('SEDAN', 'SUV', 'VAN'))
);

CREATE TABLE reservation
(
    id              BIGSERIAL PRIMARY KEY,
    car_id          BIGINT      NOT NULL,
    start_date_time TIMESTAMPTZ NOT NULL,
    end_date_time   TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reservation_car
        FOREIGN KEY (car_id)
            REFERENCES car (id),

    CONSTRAINT chk_reservation_dates
        CHECK (end_date_time > start_date_time)
);

CREATE INDEX idx_car_type
    ON car (type);

CREATE INDEX idx_reservation_car_dates
    ON reservation (car_id, start_date_time, end_date_time);