# Run nginx for development process.
# Backend API used with reverse proxy(see at nginx config)

conf=$(pwd)/nginx_local.conf
echo $conf

sudo /usr/sbin/nginx -c $conf
