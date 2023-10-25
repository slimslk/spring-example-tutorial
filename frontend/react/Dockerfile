FROM arm64v8/node:20.8-alpine
WORKDIR /app
ARG api_base_url
COPY package*.json .
RUN npm i --silent
COPY . .
RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env
EXPOSE 5173
CMD ["npm", "run", "dev"]