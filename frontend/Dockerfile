FROM node:latest
RUN mkdir code

WORKDIR /code

COPY . .

RUN npm install

EXPOSE 5417

CMD [ "npm", "start"]