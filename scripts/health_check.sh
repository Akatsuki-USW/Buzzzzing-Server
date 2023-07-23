TARGET_URL=localhost PORT=8080

for RETRY_COUNT in 1 2 3 4 5 6 7 8 9 10; do
  echo "> #${RETRY_COUNT} trying..."
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" $TARGET_URL:$PORT/hello) # HTTP status code 응답받기

  if [ "${RESPONSE_CODE}" -eq 200 ]; then
    echo "> New WAS successfully running"
    exit 0
  elif [ ${RETRY_COUNT} -eq 10 ]; then
    echo "> Health check failed."
    exit 1
  fi
  sleep 10
done
