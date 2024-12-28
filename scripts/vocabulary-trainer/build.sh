#!/bin/bash

cd "$(dirname "$0")"

rm -rf ~/apps/vocabulary-trainer/
mkdir -p ~/apps/vocabulary-trainer/util/
cp run.sh ~/apps/vocabulary-trainer/

cd ../../src/main/java
javac util/VocabularyTrainer.java
mv util/*.class ~/apps/vocabulary-trainer/util/
