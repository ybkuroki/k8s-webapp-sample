# docker build -t dbserver_k8s:1.0 -f k8s/db/Dockerfile .
# Dockerfile for PostgreSQL
FROM postgres:12.0

# Set japanese local
RUN localedef -i ja_JP -c -f UTF-8 -A /usr/share/locale/locale.alias ja_JP.UTF-8
ENV LANG ja_JP.utf8

COPY k8s/db/initdb.d/1_dcl.sql /docker-entrypoint-initdb.d/1_dcl.sql
COPY k8s/db/initdb.d/2_ddl.sql /docker-entrypoint-initdb.d/2_ddl.sql
COPY k8s/db/initdb.d/3_dml.sql /docker-entrypoint-initdb.d/3_dml.sql
