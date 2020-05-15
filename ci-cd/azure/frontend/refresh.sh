#!/bin/bash
# this script is used to refresh the repository and restart the
# deployment
echo "---refreshing repository----"
kill $(lsof -t -i:3000)

# refresh project
cd /home/Heptagon-Code
git fetch --all
git reset --hard origin/rq
git pull
cd frontend/
rm -f .env.development # avoid conflict with env vars
SEARCH_FRONTEND_URL=$(echo $REACT_APP_FRONTEND_URL | sed -e 's/[\/&]/\\&/g')
sed -i "s/http:\/\/localhost:3000/$SEARCH_FRONTEND_URL/g" ./package.json

npm install
npm run build
serve build -l 3000