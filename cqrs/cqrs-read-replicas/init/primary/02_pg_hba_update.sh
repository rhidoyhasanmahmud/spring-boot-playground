#!/bin/bash
set -e

# Allow replication connections from anywhere (non-SSL)
echo "host    replication    replicator    0.0.0.0/0    scram-sha-256" >> /var/lib/postgresql/data/pg_hba.conf
echo "hostnossl    replication    replicator    0.0.0.0/0    scram-sha-256" >> /var/lib/postgresql/data/pg_hba.conf
