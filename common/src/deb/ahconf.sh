#!/bin/sh

db_get arrowhead-common/mysql_password; AH_PASS_DB=$RET
db_get arrowhead-common/cert_password; AH_PASS_CERT=$RET
db_get arrowhead-common/cloudname; AH_CLOUD_NAME=$RET
db_get arrowhead-common/operator; AH_OPERATOR=$RET
db_get arrowhead-common/company; AH_COMPANY=$RET
db_get arrowhead-common/organisation; AH_ORGANISATION=$RET
db_get arrowhead-common/country; AH_COUNTRY=$RET

ah_gen_cert () {
    if [ ! -f "${1}/${2}.p12" ]; then
        keytool -genkeypair \
            -alias ${2} \
            -keyalg RSA \
            -keysize 2048 \
            -dname "CN=${3}, OU=${AH_ORGANISATION}, O=${AH_COMPANY}, C=${AH_COUNTRY}" \
            -validity 3650 \
            -keypass ${AH_PASS_CERT} \
            -keystore ${1}/${2}.p12 \
            -storepass ${AH_PASS_CERT} \
            -storetype PKCS12

        chown :arrowhead ${1}/${2}.p12
        chmod 640 ${1}/${2}.p12
    fi
}

ah_gen_signed () {
    if [ ! -f "${1}/${2}.p12" ]; then
        ah_gen_cert $1 $2 $3

        keytool -export \
            -alias ${5} \
            -storepass ${AH_PASS_CERT} \
            -keystore ${4}/${5}.p12 \
        | keytool -import \
            -trustcacerts \
            -alias ${5} \
            -keystore ${1}/${2}.p12 \
            -keypass ${AH_PASS_CERT} \
            -storepass ${AH_PASS_CERT} \
            -storetype PKCS12 \
            -noprompt

        keytool -certreq \
            -alias ${2} \
            -keypass ${AH_PASS_CERT} \
            -keystore ${1}/${2}.p12 \
            -storepass ${AH_PASS_CERT} \
        | keytool -gencert \
            -alias ${5} \
            -keypass ${AH_PASS_CERT} \
            -keystore ${4}/${5}.p12 \
            -storepass ${AH_PASS_CERT} \
        | keytool -importcert \
            -alias ${2} \
            -keypass ${AH_PASS_CERT} \
            -keystore ${1}/${2}.p12 \
            -storepass ${AH_PASS_CERT} \
            -noprompt
    fi
}

ah_gen_trust () {
    if [ ! -f "${1}/truststore.p12" ]; then
        keytool -export \
            -alias ${3} \
            -storepass ${AH_PASS_CERT} \
            -keystore ${2}/${3}.p12 \
        | keytool -import \
            -trustcacerts \
            -alias ${3} \
            -keystore ${1}/truststore.p12 \
            -keypass ${AH_PASS_CERT} \
            -storepass ${AH_PASS_CERT} \
            -storetype PKCS12 \
            -noprompt

        chown :arrowhead ${1}/truststore.p12
        chmod 640 ${1}/truststore.p12
    fi
}

ah_gen_db_user () {
    if [ $(mysql -u root -sse "SELECT EXISTS(SELECT 1 FROM mysql.user WHERE user = 'arrowhead')") != 1 ]; then
        mysql -e "CREATE USER arrowhead@localhost IDENTIFIED BY '${AH_PASS_DB}';"
        mysql -e "GRANT ALL PRIVILEGES ON arrowhead.* TO arrowhead@'localhost';"
        mysql -e "FLUSH PRIVILEGES;"
    fi
}

ah_gen_log4j () {
    if [ ! -f "/etc/arrowhead/${1}/log4j.properties" ]; then
        /bin/cat <<EOF >/etc/arrowhead/${1}/log4j.properties
# Define the root logger with appender file
log4j.rootLogger=INFO, DB, FILE

# Database related config
# Define the DB appender
log4j.appender.DB=org.apache.log4j.jdbc.JDBCAppender
# Set Database Driver
log4j.appender.DB.driver=com.mysql.jdbc.Driver
# Set Database URL
log4j.appender.DB.URL=jdbc:mysql://127.0.0.1:3306/arrowhead?useSSL=false
# Set database user name and password
log4j.appender.DB.user=arrowhead
log4j.appender.DB.password=${AH_PASS_DB}
# Set the SQL statement to be executed.
log4j.appender.DB.sql=INSERT INTO logs VALUES(DEFAULT,'%d{yyyy-MM-dd HH:mm:ss}','%C','%p','%m')
# Define the layout for file appender
log4j.appender.DB.layout=org.apache.log4j.PatternLayout
# Disable Hibernate verbose logging
log4j.logger.org.hibernate=fatal

# File related config
# Define the file appender
log4j.appender.FILE=org.apache.log4j.FileAppender
# Set the name of the file
log4j.appender.FILE.File=/var/log/arrowhead/${1}.log
# Set the immediate flush to true (default)
log4j.appender.FILE.ImmediateFlush=true
# Set the threshold to debug mode
log4j.appender.FILE.Threshold=debug
# Set the append to false, overwrite
log4j.appender.FILE.Append=false
# Define the layout for file appender
log4j.appender.FILE.layout=org.apache.log4j.PatternLayout
log4j.appender.FILE.layout.conversionPattern=%d{yyyy-MM-dd HH:mm:ss}, %C, %p, %m%n
EOF
        chown root:arrowhead /etc/arrowhead/${1}/log4j.properties
        chmod 640 /etc/arrowhead/${1}/log4j.properties
    fi
}

ah_export_cert () {
    if [ ! -f "${3}/${2}.crt" ]; then
        keytool -export \
            -alias ${2} \
            -storepass ${AH_PASS_CERT} \
            -keystore ${1}/${2}.p12 \
            -file ${3}/${2}.crt

        chown :arrowhead ${3}/${2}.crt
        chmod 640 ${3}/${2}.crt
    fi
}
