CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS favorite_medicines CASCADE;
DROP TABLE IF EXISTS favorites CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS medicine_availability_reports CASCADE;
DROP TABLE IF EXISTS medicine_price_history CASCADE;
DROP TABLE IF EXISTS pharmacy_medicines CASCADE;
DROP TABLE IF EXISTS medicines CASCADE;
DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS pharmacies CASCADE;
DROP TABLE IF EXISTS address CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS reservation_status CASCADE;

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(30) NOT NULL DEFAULT 'CUSTOMER',
                       phone VARCHAR(20),
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT users_role_check
                           CHECK (
                               role IN (
                                        'ADMIN',
                                        'CUSTOMER',
                                        'PHARMACY_OWNER',
                                        'PHARMACY_EMPLOYEE'
                                   )
                               )
);

CREATE TABLE address (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         zip_code VARCHAR(8) NOT NULL,
                         state VARCHAR(2) NOT NULL,
                         city VARCHAR(255) NOT NULL,
                         neighborhood VARCHAR(255) NOT NULL,
                         street VARCHAR(255) NOT NULL,
                         number VARCHAR(10) NOT NULL,
                         complement VARCHAR(255),
                         latitude NUMERIC(10, 8),
                         longitude NUMERIC(11, 8),
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pharmacies (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            owner_id UUID NOT NULL REFERENCES users(id),
                            name VARCHAR(255) NOT NULL,
                            cnpj VARCHAR(14) NOT NULL UNIQUE,
                            phone VARCHAR(20),
                            email VARCHAR(255) NOT NULL UNIQUE,
                            address_id UUID NOT NULL UNIQUE REFERENCES address(id),
                            opening_hours TIME NOT NULL,
                            closing_hours TIME NOT NULL,
                            active BOOLEAN NOT NULL DEFAULT TRUE,
                            verified BOOLEAN NOT NULL DEFAULT FALSE,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT pharmacies_opening_closing_check
                                CHECK (closing_hours > opening_hours)
);

CREATE TABLE employees (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           pharmacy_id UUID NOT NULL REFERENCES pharmacies(id),
                           user_id UUID NOT NULL REFERENCES users(id),
                           position VARCHAR(255) NOT NULL DEFAULT 'Employee',
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT unique_employee_user_pharmacy
                               UNIQUE (user_id, pharmacy_id)
);

CREATE TABLE medicines (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           name VARCHAR(255) NOT NULL,
                           description VARCHAR(500),
                           active_ingredient VARCHAR(255) NOT NULL,
                           dosage VARCHAR(255) NOT NULL,
                           pharmaceutical_form VARCHAR(255) NOT NULL,
                           manufacturer VARCHAR(255) NOT NULL,
                           requires_prescription BOOLEAN NOT NULL DEFAULT FALSE,
                           ean VARCHAR(20),
                           anvisa_registry VARCHAR(50),
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pharmacy_medicines (
                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                    pharmacy_id UUID NOT NULL REFERENCES pharmacies(id),
                                    medicine_id UUID NOT NULL REFERENCES medicines(id),
                                    current_price NUMERIC(10, 2),
                                    stock INTEGER,
                                    availability_status VARCHAR(30) NOT NULL DEFAULT 'UNKNOWN',
                                    last_confirmed_at TIMESTAMP,
                                    last_confirmed_by UUID REFERENCES users(id),
                                    confirmation_source VARCHAR(30),
                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT uk_pharmacy_medicine
                                        UNIQUE (pharmacy_id, medicine_id),

                                    CONSTRAINT pharmacy_medicines_price_check
                                        CHECK (
                                            current_price IS NULL OR current_price > 0
                                            ),

                                    CONSTRAINT pharmacy_medicines_stock_check
                                        CHECK (
                                            stock IS NULL OR stock >= 0
                                            ),

                                    CONSTRAINT pharmacy_medicines_availability_status_check
                                        CHECK (
                                            availability_status IN (
                                                                    'AVAILABLE',
                                                                    'UNAVAILABLE',
                                                                    'UNKNOWN',
                                                                    'OUTDATED'
                                                )
                                            ),

                                    CONSTRAINT pharmacy_medicines_confirmation_source_check
                                        CHECK (
                                            confirmation_source IS NULL OR confirmation_source IN (
                                                                                                   'USER_REPORT',
                                                                                                   'PHARMACY_PANEL',
                                                                                                   'CSV_IMPORT',
                                                                                                   'ADMIN',
                                                                                                   'SCRAPING'
                                                )
                                            )
);

CREATE TABLE medicine_price_history (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                        pharmacy_medicine_id UUID NOT NULL REFERENCES pharmacy_medicines(id) ON DELETE CASCADE,
                                        price NUMERIC(10, 2) NOT NULL,
                                        source VARCHAR(30) NOT NULL,
                                        reported_by UUID REFERENCES users(id),
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                        CONSTRAINT medicine_price_history_price_check
                                            CHECK (price > 0),

                                        CONSTRAINT medicine_price_history_source_check
                                            CHECK (
                                                source IN (
                                                           'USER_REPORT',
                                                           'PHARMACY_PANEL',
                                                           'CSV_IMPORT',
                                                           'ADMIN',
                                                           'SCRAPING'
                                                    )
                                                )
);

CREATE TABLE medicine_availability_reports (
                                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                               pharmacy_medicine_id UUID NOT NULL REFERENCES pharmacy_medicines(id) ON DELETE CASCADE,

                                               user_id UUID REFERENCES users(id),

                                               status VARCHAR(30) NOT NULL,

                                               reported_price NUMERIC(10, 2),

                                               notes VARCHAR(500),

                                               source VARCHAR(30) NOT NULL,

                                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                               CONSTRAINT medicine_availability_reports_status_check
                                                   CHECK (
                                                       status IN (
                                                                  'AVAILABLE',
                                                                  'UNAVAILABLE',
                                                                  'UNKNOWN',
                                                                  'OUTDATED'
                                                           )
                                                       ),

                                               CONSTRAINT medicine_availability_reports_source_check
                                                   CHECK (
                                                       source IN (
                                                                  'USER_REPORT',
                                                                  'PHARMACY_PANEL',
                                                                  'CSV_IMPORT',
                                                                  'ADMIN',
                                                                  'SCRAPING'
                                                           )
                                                       ),

                                               CONSTRAINT medicine_availability_reports_price_check
                                                   CHECK (
                                                       reported_price IS NULL OR reported_price > 0
                                                       )
);

CREATE TABLE reservations (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                              customer_id UUID NOT NULL REFERENCES users(id),

                              pharmacy_medicine_id UUID NOT NULL REFERENCES pharmacy_medicines(id),

                              quantity INTEGER NOT NULL,

                              status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

                              reservation_code VARCHAR(10) NOT NULL UNIQUE,

                              expires_at TIMESTAMP NOT NULL,

                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT reservations_quantity_check
                                  CHECK (quantity > 0),

                              CONSTRAINT reservations_status_check
                                  CHECK (
                                      status IN (
                                                 'PENDING',
                                                 'CONFIRMED',
                                                 'CANCELLED',
                                                 'EXPIRED',
                                                 'COMPLETED'
                                          )
                                      )
);

CREATE TABLE favorite_medicines (
                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                                    user_id UUID NOT NULL REFERENCES users(id),

                                    medicine_id UUID NOT NULL REFERENCES medicines(id),

                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT unique_user_favorite_medicine
                                        UNIQUE (user_id, medicine_id)
);

CREATE TABLE notifications (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                               user_id UUID NOT NULL REFERENCES users(id),

                               title VARCHAR(255) NOT NULL DEFAULT 'SOS Remédio Notification',

                               message VARCHAR(500) NOT NULL,

                               is_read BOOLEAN NOT NULL DEFAULT FALSE,

                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);

CREATE INDEX idx_pharmacies_owner_id ON pharmacies(owner_id);

CREATE INDEX idx_pharmacies_cnpj ON pharmacies(cnpj);

CREATE INDEX idx_employees_pharmacy_id ON employees(pharmacy_id);

CREATE INDEX idx_employees_user_id ON employees(user_id);

CREATE INDEX idx_medicines_name ON medicines(name);

CREATE INDEX idx_pharmacy_medicines_pharmacy_id ON pharmacy_medicines(pharmacy_id);

CREATE INDEX idx_pharmacy_medicines_medicine_id ON pharmacy_medicines(medicine_id);

CREATE INDEX idx_price_history_pharmacy_medicine_id
    ON medicine_price_history(pharmacy_medicine_id);

CREATE INDEX idx_availability_reports_pharmacy_medicine_id
    ON medicine_availability_reports(pharmacy_medicine_id);