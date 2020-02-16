echo "start create schema, tables, indexes..."
echo psql --version
cat init.sql | PGPASSWORD=58gh95 psql -h localhost -p 5432 -d rates_db -U app_user

echo "end create"
