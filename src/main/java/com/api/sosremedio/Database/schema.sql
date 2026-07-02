create database SosRemedio;

create type user_role as enum (
    'ADMIN',
    'CUSTOMER',
    'PHARMACY_OWNER',
    'PHARMACY_EMPLOYEE'
);

create table users (
    id uuid primary key default gen_random_uuid(),
    name varchar(255) not null,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    role user_role not null default 'CUSTOMER',
    phone varchar(20),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table address (
    id uuid primary key default gen_random_uuid(),
    zip_code varchar(8) not null,
    state varchar(2) not null,
    city varchar(255) not null,
    neighborhood varchar(255) not null,
    street varchar(255) not null,
    number varchar(10) not null,
    complement varchar(255),
    latitude numeric(10, 8),
    longitude numeric(11, 8),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table pharmacies (
    id uuid primary key default gen_random_uuid(),
    owner_id uuid not null references Users(id),
    name varchar(255) not null,
    cnpj varchar(14) not null unique,
    phone varchar(20),
    email varchar(255) not null unique,
    address_id uuid not null references address(id),
    opening_hours time,
    closing_hours time,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table employees (
    id uuid primary key default gen_random_uuid(),
    pharmacy_id uuid references pharmacies(id),
    user_id uuid references Users(id),
    created_at timestamp default current_timestamp,
    positiion varchar(255) not null default 'Employee',
    unique (user_id, pharmacy_id)
);

create table medicines (
    id uuid primary key default gen_random_uuid(),
    name varchar(255) not null,
    description varchar(500),
    active_ingredient varchar(255) not null,
    dosage varchar(255) not null,
    pharmaceutical_form varchar(255) not null,
    manufacturer varchar(255) not null,
    requires_prescription boolean not null default false,
    created_at timestamp default current_timestamp
);

create table pharmacy_medicines (
    id uuid primary key default gen_random_uuid(),
    pharmacy_id uuid references pharmacies(id),
    medicine_id uuid references medicines(id),
    price numeric(10, 2) not null,
    stock integer not null default 0,
    updated_at timestamp default current_timestamp,
    unique (pharmacy_id, medicine_id)
);

create type reservation_status as enum (
    'PENDING',
    'CONFIRMED',
    'CANCELLED',
    'EXPIRED',
    'COMPLETED'
);

create table reservations (
    id uuid primary key default gen_random_uuid(),
    customer_id uuid references Users(id),
    pharmacy_medicine_id uuid references pharmacy_medicines(id),
    quantity integer not null check (quantity > 0),
    status reservation_status not null default 'PENDING',
    reservation_code varchar(10) not null unique,
    expires_at timestamp not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table favorites (
    id uuid primary key default gen_random_uuid(),
    user_id uuid references Users(id),
    pharmacy_id uuid references pharmacies(id),
    created_at timestamp default current_timestamp,
    unique (user_id, pharmacy_id)
);

create table notifications (
    id uuid primary key default gen_random_uuid(),
    user_id uuid references Users(id),
    title varchar(255) not null default 'SOS Remédio Notification',
    message varchar(500) not null,
    is_read boolean not null default false,
    created_at timestamp default current_timestamp
)