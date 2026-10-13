-- liquibase formatted sql
-- changeset subscribe:update_db_subscribe-1.1.0-1.1.1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA=database() AND TABLE_NAME='subscribe_subscription' AND COLUMN_NAME='subscription_order'

--
-- Update table subscribe_subscription
--

ALTER TABLE subscribe_subscription
ADD COLUMN subscription_order int default '1' NOT NULL;