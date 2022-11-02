FROM node:8

WORKDIR /usr/src/app

COPY package*.json ./
COPY npm-shrinkwrap.json ./

RUN npm install
RUN npm install -g gulp

COPY . .

EXPOSE 3001
CMD [ "gulp", "compose" ]
