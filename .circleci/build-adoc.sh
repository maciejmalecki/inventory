#!/bin/bash

pwd
ls -al
cd doc || exit
bundle exec asciidoctor -D ../out-adoc -r asciidoctor-diagram index.adoc
cd ..
