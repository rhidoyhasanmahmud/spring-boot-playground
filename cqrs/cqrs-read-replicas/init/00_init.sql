CREATE USER replicator WITH replication encrypted password 'replicator_password';
SELECT pg_create_physical_replication_slot('replication_slot');