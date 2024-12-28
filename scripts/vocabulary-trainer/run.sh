#!/bin/bash

cd "$(dirname "$0")"
file=$(printf "%04d" $1)
rlwrap java util.VocabularyTrainer ~/Documents/german-phrases/$file.csv
