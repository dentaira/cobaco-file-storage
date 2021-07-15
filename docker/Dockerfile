FROM postgres
RUN localedef -i ja_JP -c -f UTF-8 -A /usr/share/locale/locale.alias ja_JP.UTF-8
ENV LANG ja_JP.utf8

COPY init-user-db.sh /docker-entrypoint-initdb.d/1-init-user-db.sh
COPY schema.sql /docker-entrypoint-initdb.d/2-schema.sql
COPY data.sql /docker-entrypoint-initdb.d/3-data.sql
