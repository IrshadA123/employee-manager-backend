
-- Remove the old department text column.
-- Employee now references departments using department_id.

ALTER TABLE employees
DROP COLUMN department;
