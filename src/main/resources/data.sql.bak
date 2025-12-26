-- Владельцы
INSERT INTO owners (first_name, last_name, phone, email) VALUES
('Анна', 'Соколова-Иванова', '+7 (900) 111-22-33', 'anna@example.com'),
('Дмитрий', 'Петров', '+7 (900) 444-55-66', 'dmitry@example.com');

-- Питомцы
INSERT INTO pets (name, species, age, owner_id) VALUES
('Барсик', 'Кот', 5, 1),
('Рекс', 'Собака', 3, 2);

-- Врачи
INSERT INTO vets (first_name, last_name, specialty, phone) VALUES
('Алексей', 'Козлов', 'Офтальмолог', '+7 (901) 111-11-11'),
('Елена', 'Смирнова', 'Хирург', '+7 (901) 222-22-22');

-- Приёмы
INSERT INTO appointments (date_time, reason, complaints, status, pet_id, vet_id) VALUES
('2025-12-25 10:00:00', 'Плановый осмотр', 'Глаза слезятся', 'SCHEDULED', 1, 1),
('2025-12-25 11:00:00', 'Ранение лапы', 'Хромает, не наступает', 'IN_PROGRESS', 2, 2);

-- Лечение
INSERT INTO treatments (prescription, medications, follow_up_date, appointment_id) VALUES
('Промывание глаз, капли 2 раза в день', '{"Фурацилин", "Ципровет"}', '2026-01-05', 1);