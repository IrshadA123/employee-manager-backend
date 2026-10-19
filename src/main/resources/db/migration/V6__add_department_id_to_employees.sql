-- Add department_id column to employees table
ALTER TABLE employees
ADD COLUMN department_id BIGINT;

ALTER TABLE employees
ADD CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES departments(id);
