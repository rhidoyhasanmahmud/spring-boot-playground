-- Only create replication user, since "app" already exists
CREATE USER replicator REPLICATION LOGIN ENCRYPTED PASSWORD 'repl_pass';
