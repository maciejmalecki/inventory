#!/bin/bash

pwd
ls -al
cd doc || exit
bundle exec asciidoctor -D ../out-adoc index.adoc
cd ..
