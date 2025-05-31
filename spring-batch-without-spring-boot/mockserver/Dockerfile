FROM node:22-alpine
COPY ./server.js /srv/.
COPY ./samples/acme-supplier-products.csv /srv/samples/.
RUN apk add --update curl
WORKDIR /srv
ENTRYPOINT node /srv/server.js