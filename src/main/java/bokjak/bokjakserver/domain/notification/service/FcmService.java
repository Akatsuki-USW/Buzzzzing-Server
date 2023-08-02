package bokjak.bokjakserver.domain.notification.service;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.notification.dto.FcmDto;
import bokjak.bokjakserver.domain.notification.dto.FcmDto.FcmMessage;
import bokjak.bokjakserver.domain.notification.dto.FcmDto.Message;
import bokjak.bokjakserver.domain.notification.dto.NotificationDto;
import bokjak.bokjakserver.domain.notification.exception.NotificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final ObjectMapper objectMapper;
    private final JSONParser jsonParser;


    private static String FCM_PRIVATE_KEY_PATH = "buzzzzing-firebase-private-key.json";
    private static String fireBaseScope = "https://www.googleapis.com/auth/cloud-platform";
    private static String PROJECT_ID_URL = "https://fcm.googleapis.com/v1/projects/buzzzzing-c258e/messages:send";


    private String getAccessToken() {
        try {
            String firebaseConfigPath = FCM_PRIVATE_KEY_PATH;
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of(fireBaseScope));
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            log.warn("FCM getAccessToken Error : {}", e.getMessage());
            throw new NotificationException(StatusCode.GET_FCM_ACCESS_TOKEN_ERROR);
        }
    }

    public String makeMessage(String targetToken, NotificationDto.NotifyParams params) {
                try {
                    FcmMessage fcmMessage = new FcmMessage(
                            false,
                            new Message(
                                    targetToken,
                                    new FcmDto.Notification(
                                            params.title(),
                                            params.content(),
                                            String.valueOf(params.redirectTargetId()),
                                            params.type().toString()
                                    )
                            )
                    );
            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            log.warn("FCM [makeMessage] Error : {}", e.getMessage());
            throw new NotificationException(StatusCode.FCM_MESSAGE_JSON_PARSING_ERROR);
        }
    }

    @Async(value = "AsyncBean")
    public CompletableFuture<Boolean> sendPushMessage(String fcmToken, NotificationDto.NotifyParams params) {
        String message = makeMessage(fcmToken, params);
        String accessToken = getAccessToken();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(PROJECT_ID_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json; UTF-8")
                .post(RequestBody.create(message, MediaType.parse("application/json; charset=urf-8")))
                .build();
        try (Response response = client.newCall(request).execute()){
            if (!response.isSuccessful() && response.body() != null) {
                JSONObject responseBody = (JSONObject) jsonParser.parse(response.body().string());
                String errorMessage = ((JSONObject) responseBody.get("error")).get("message").toString();
                log.warn("FCM [sendPushMessage] okHttp response is not OK : {}", errorMessage);
                return CompletableFuture.completedFuture(false);
            }
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.warn("FCM [sendPushMessage] I/O Exception : {}", e.getMessage());
            throw new NotificationException(StatusCode.SEND_FCM_PUSH_ERROR);
        }
    }
}
