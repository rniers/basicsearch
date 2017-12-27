FROM java:8

WORKDIR /
COPY ./server-assembly-0.1.0.jar /opt/basicsearch/server.jar

ENV ROLE shard
ENV SHARDS ""

EXPOSE 8888

HEALTHCHECK --interval=5s --timeout=3s CMD curl --fail http://localhost:8888/health || exit 1

CMD ["sh", "-c", "java -jar /opt/basicsearch/server.jar -http.port=:8888 -role=${ROLE} -shards=${SHARDS}"]