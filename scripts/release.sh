#!/usr/bin/env bash

mvn install:install-file -DgroupId=eu.arrowhead -DartifactId=arrowhead-common -Dversion=4.1.2-SNAPSHOT \
    -Dfile=$(dirname "$0")/../common/target/arrowhead-common-4.1.2-SNAPSHOT.jar -Dpackaging=jar \
    -DgeneratePom=true -DlocalRepositoryPath=$(pwd) -DcreateChecksum=true
