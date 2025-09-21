ALTER TABLE expense DROP CONSTRAINT expense_owner_id_fkey;
ALTER TABLE owner DROP CONSTRAINT owner_pkey;
ALTER TABLE owner ADD primary key (username);
ALTER TABLE expense ADD foreign key (owner_id) REFERENCES owner (username);
