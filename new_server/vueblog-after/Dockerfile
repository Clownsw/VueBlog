FROM smilex1/tencent-kona:8.0.14-372

ENV JAVA_OPTIONS="-XX:+UseG1GC -XX:+G1ParallelFullGC -XX:+G1RebuildRemSet -XX:+UseStringDeduplication -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+UseTLAB -XX:+ZeroTLAB -XX:+ResizeTLAB -Xloggc:gc.log -verbose:gc"
ENV JAVA_MEM_OPTIONS="-XX:ParallelGCThreads=8 -XX:ConcGCThreads=8 -XX:G1HeapRegionSize=4M -Xmx800M"
ENV VUEBLOG_CONFIG_PATH="/app/vueblog-config.json"

ENV FILE_NAME="vueblog-after.jar"

ADD ./${FILE_NAME} /app/${FILE_NAME}

ENTRYPOINT java ${JAVA_OPTIONS} ${JAVA_MEM_OPTIONS} -Dvueblog.config.path=${VUEBLOG_CONFIG_PATH} -jar "/app/${FILE_NAME}"