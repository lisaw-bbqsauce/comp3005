DROP SCHEMA IF EXISTS comp3000 CASCADE;

CREATE SCHEMA IF NOT EXISTS comp3000;

CREATE TABLE IF NOT EXISTS comp3000.students (
	student_id SERIAL PRIMARY KEY,
	first_name text NOT NULL,
	last_name text NOT NULL,
	email text UNIQUE NOT NULL,
	enrollment_date date
);

INSERT INTO comp3000.students (first_name, last_name, email, enrollment_date) VALUES
('John', 'Doe', 'john.doe@example.com', '2023-09-01'),
('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'),
('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02');