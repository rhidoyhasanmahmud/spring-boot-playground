#!/bin/bash
set -e

# Wipe any existing replica data
rm -rf /var/lib/postgresql/data/*

# Clone from primary
pg_basebackup -h $PRIMARY_HOST -p $PRIMARY_PORT -D /var/lib/postgresql/data \
  -U $REPL_USER -Fp -Xs -P -R -W
