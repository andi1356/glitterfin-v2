ALTER TABLE expense
    ALTER COLUMN amount
        TYPE NUMERIC(20,2)
        USING amount::NUMERIC(20,2);