#!/bin/bash
# this script is used to init the enviroment
# start hook server
python3 ./hookServer.py -p 1234 -s ./refresh.sh &

# start project
git clone --depth=1 --single-branch --branch ra git@github.com:heptagon-group/Heptagon-Code.git
cd /home/Heptagon-Code/frontend/
rm -f .env.development # avoid conflict with env vars
SEARCH_FRONTEND_URL=$(echo $REACT_APP_FRONTEND_URL | sed -e 's/[\/&]/\\&/g')
sed -i "s/http:\/\/localhost:3000/$SEARCH_FRONTEND_URL/g" ./package.json
#sed -i "s/localhost:8080/$BACKEND_URL/g" ./.env.development

npm install
npm install -g serve
npm run build
serve build -l 3000

/bin/bash
tail -f /dev/null