#!/bin/bash

cd "$(dirname "$0")"
rlwrap java util.VocabularyTrainer ~/Documents/vocab.csv
