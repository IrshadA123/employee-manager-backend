
-- Remove redundant department identifier.
-- Employee foreign key still references departments.id.

ALTER TABLE departments
DROP COLUMN department_id;
