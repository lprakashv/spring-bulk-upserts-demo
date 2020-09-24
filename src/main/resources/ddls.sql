-- target table
create table testingschema.price (
    upc varchar(255) NOT NULL,
    store_id varchar(40) NOT NULL,
    price numeric(19, 7) NOT NULL
);
GO

ALTER TABLE testingschema.price ADD PRIMARY KEY CLUSTERED (
  [upc] ASC,
  [store_id] ASC
);
GO

-- insert only 'stage' table for price with auto incremented id
create table testingschema.price_with_id (
    id int identity(1, 1) NOT NULL,
    upc varchar(255) NOT NULL,
    store_id varchar(40) NOT NULL,
    price numeric(19, 7) NOT NULL
);
GO

create index IDX_price_2 on testingschema.price_with_id (
	[upc] ASC,
	[store_id] ASC
)
GO


-- stored procedure for merging the 'stage' table data into target table to emulate upserts
CREATE PROCEDURE testingschema.merge_and_dedup_prices
AS
BEGIN
    BEGIN TRANSACTION;

    MERGE testingschema.price AS TARGET
    USING (
        select stage.upc, stage.store_id, stage.price from productcatalog.price_with_id stage
        -- this is a deduplicating step -> filter only latest id record
        where stage.id in (
            select max(id) from testingschema.price_with_id group by upc,store_id
        )
    ) AS SOURCE
    ON (TARGET.UPC = SOURCE.UPC AND TARGET.STORE_ID = SOURCE.STORE_ID)
    WHEN MATCHED
        -- record present in main table
        THEN UPDATE SET TARGET.PRICE = SOURCE.PRICE,
    WHEN NOT MATCHED BY TARGET
        -- record not present in main table
        THEN INSERT (UPC, STORE_NO, PRICE) VALUES (SOURCE.UPC, SOURCE.STORE_ID, SOURCE.PRICE)
    ;

    -- deleting stage table for fresh inserts
    TRUNCATE TABLE testingschema.price_with_id;

    COMMIT TRANSACTION
END;
GO