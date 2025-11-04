-- liquibase formatted sql
-- changeset subscribe:update_db_subscribe-1.1.0-1.1.1.sql
-- preconditions onFail:MARK_RAN onError:WARN

--
-- Update table subscribe_subscription
--

ALTER TABLE subscribe_subscription
ADD COLUMN subscription_order int default '1' NOT NULL;