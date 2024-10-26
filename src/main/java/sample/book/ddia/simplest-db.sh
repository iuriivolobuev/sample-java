#!/bin/bash

# Usage:
# 1. source simplest-db.sh
# 2. db_set a 15
# 3. db_set b 25
# 4. db_get a
# 5. db_get b

db_set() {
  echo "$1,$2" >> database
}

db_get() {
  grep "^$1," database | sed -e "s/^$1,//" | tail -n 1
}
