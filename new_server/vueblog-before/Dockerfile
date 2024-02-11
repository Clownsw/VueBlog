FROM bellsoft/liberica-runtime-container:jdk-all-17-cds-slim-musl

ENV JAVA_OPTIONS="-XX:ParallelGCThreads=2 -XX:ConcGCThreads=1 -XX:G1HeapRegionSize=1M -XX:+UseTLAB -XX:+ZeroTLAB -XX:+ResizeTLAB -Xms128M -Xmx256M --add-opens java.base/sun.misc=ALL-UNNAMED --add-opens java.base/jdk.internal.misc=ALL-UNNAMED -Dio.netty.tryReflectionSetAccessible=true"
ENV VUEBLOG_CONFIG_PATH="/app/vueblog-config.json"

ENV FILE_NAME="vueblog-before.jar"

ADD ./${FILE_NAME} /app/${FILE_NAME}

ENTRYPOINT java ${JAVA_OPTIONS} -Dvueblog.config.path=${VUEBLOG_CONFIG_PATH} -jar "/app/${FILE_NAME}"