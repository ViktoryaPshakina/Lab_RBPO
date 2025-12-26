-- Удаляем старые таблицы (если существуют)
DROP TABLE IF EXISTS treatments CASCADE;
DROP TABLE IF EXISTS appointments CASCADE;
DROP TABLE IF EXISTS pets CASCADE;
DROP TABLE IF EXISTS owners CASCADE;
DROP TABLE IF EXISTS vets CASCADE;

-- Таблица владельцев
CREATE TABLE owners (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Таблица питомцев
CREATE TABLE pets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    species VARCHAR(50) NOT NULL,
    age INT CHECK (age > 0),
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE
);

-- Таблица врачей
CREATE TABLE vets (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    specialty VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL
);

-- Таблица приёмов
CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    date_time TIMESTAMP NOT NULL,
    reason VARCHAR(255) NOT NULL,
    complaints TEXT,
    status VARCHAR(20) NOT NULL CHECK (status IN ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    pet_id BIGINT NOT NULL,
    vet_id BIGINT NOT NULL,
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    FOREIGN KEY (vet_id) REFERENCES vets(id) ON DELETE RESTRICT
);

-- Таблица лечения
CREATE TABLE treatments (
    id BIGSERIAL PRIMARY KEY,
    prescription TEXT NOT NULL,
    medications TEXT[],
    follow_up_date DATE,
    appointment_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);