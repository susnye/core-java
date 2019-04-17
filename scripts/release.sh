#!/usr/bin/env bash

mvn install:install-file -DgroupId=eu.arrowhead -DartifactId=common -Dversion=4.1.1-SNAPSHOT \
    -Dfile=$(dirname "$0")/../common/target/arrowhead-common-4.1.1-SNAPSHOT.jar -Dpackaging=jar \
    -DgeneratePom=true -DlocalRepositoryPath=$(pwd) -DcreateChecksum=true
