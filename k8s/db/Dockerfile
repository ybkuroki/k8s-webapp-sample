# docker build -t dbserver_k8s:1.0 -f k8s/db/Dockerfile .
# Dockerfile for PostgreSQL
FROM postgres:12.0

# Set japanese local
RUN localedef -i ja_JP -c -f UTF-8 -A /usr/share/locale/locale.alias ja_JP.UTF-8
ENV LANG ja_JP.utf8

COPY k8s/db/initdb.d/create-database.sh /docker-entrypoint-initdb.d/
RUN chmod +x /docker-entrypoint-initdb.d/create-database.sh
