# Getting Started
Simple SpringBoot and React application.

Run:
* change /ui/nginx.config line: 
    `proxy_pass http://172.18.0.1:8082;` to `proxy_pass http://<your network>:8082;`
        
* ` docker-compose up --build --force-recreate `
* open: http://127.0.0.1:9090/index.html

For local development:
* ` npm start `
* ` sh local-nginx.sh ` 
* open: http://127.0.0.1:81/


Stop Node: ` killall node `
