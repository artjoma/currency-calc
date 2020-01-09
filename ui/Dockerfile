FROM nginx:1.17.2-alpine

COPY src /var/www

COPY nginx.conf /etc/nginx/nginx.conf

ENTRYPOINT ["nginx","-g","daemon off;"]