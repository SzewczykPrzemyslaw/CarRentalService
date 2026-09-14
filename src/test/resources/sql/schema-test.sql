INSERT INTO car (id, type, brand, model, registration_no)
VALUES
    (1, 'SEDAN', 'Toyota', 'Camry', 'TEST-SED-001'),
    (2, 'SEDAN', 'Volkswagen', 'Passat', 'TEST-SED-002'),
    (3, 'SEDAN', 'Skoda', 'Octavia', 'TEST-SED-003'),
    (4, 'SEDAN', 'BMW', '3 Series', 'TEST-SED-004');

INSERT INTO reservation (car_id, start_date_time, end_date_time)
VALUES
    (1, '2026-09-15 10:00:00+02', '2026-09-20 10:00:00+02'),
    (3, '2026-09-20 09:00:00+02', '2026-09-25 10:00:00+02'),
    (4, '2026-09-18 12:00:00+02', '2026-09-22 12:00:00+02');