package fr.raksrinana.twitchminer.api.twitch;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.raksrinana.twitchminer.api.twitch.data.MinuteWatchedEvent;
import fr.raksrinana.twitchminer.api.twitch.data.MinuteWatchedProperties;
import fr.raksrinana.twitchminer.api.twitch.data.PlayerEvent;
import fr.raksrinana.twitchminer.tests.UnirestMockExtension;
import fr.raksrinana.twitchminer.utils.json.JacksonUtils;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import static java.nio.charset.StandardCharsets.UTF_8;
import static kong.unirest.HttpMethod.GET;
import static kong.unirest.HttpMethod.POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TwitchApiTest{
	private static final int USER_ID = 123456789;
	private static final String BROADCAST_ID = "broadcast-id";
	private static final String CHANNEL_ID = "channel-id";
	private static final String PLAYER = "player";
	private static final String GAME = "game";
	private static final String STREAMER_URL = "https://google.com/streamer";
	private static final String CONFIG_URL = "https://static.twitchcdn.net/config/settings.sq5d4q6s54ds854c84qs.js";
	private static final String CONFIG_BODY = "<script src=\"%s\" crossorigin=\"anonymous\"></script>".formatted(CONFIG_URL);
	private static final String SPADE_URL = "https://google.com";
	private static final String SPADE_BODY = "\"spade_url\":\"%s\"".formatted(SPADE_URL);
	private static final String SPADE_BODY_INVALID_FORMAT = "\"spade_url\":\"%s\"".formatted("https://google.com:-80/");
	
	@RegisterExtension
	private static final UnirestMockExtension unirest = new UnirestMockExtension();
	
	private final TwitchApi tested = new TwitchApi();
	
	private URL streamerUrl;
	private URL spadeUrl;
	
	@BeforeEach
	void setUp() throws MalformedURLException{
		streamerUrl = new URL(STREAMER_URL);
		spadeUrl = new URL(SPADE_URL);
	}
	
	@Test
	void sendMinutesWatched(){
		var json = "[{\"event\":\"minute-watched\",\"properties\":{\"channel_id\":\"%s\",\"broadcast_id\":\"%s\",\"player\":\"%s\",\"user_id\":%d}}]"
				.formatted(CHANNEL_ID, BROADCAST_ID, PLAYER, USER_ID);
		var expectedData = new String(Base64.getEncoder().encode(json.getBytes(UTF_8)));
		
		unirest.expect(POST, SPADE_URL)
				.body("{\"data\":\"%s\"}".formatted(expectedData))
				.thenReturn()
				.withStatus(204);
		
		var request = MinuteWatchedEvent.builder()
				.properties(MinuteWatchedProperties.builder()
						.userId(USER_ID)
						.broadcastId(BROADCAST_ID)
						.channelId(CHANNEL_ID)
						.player(PLAYER)
						.build())
				.build();
		assertThat(tested.sendPlayerEvents(spadeUrl, request)).isTrue();
		
		unirest.verifyAll();
	}
	
	@Test
	void sendMinutesWatchedWithGame(){
		var json = "[{\"event\":\"minute-watched\",\"properties\":{\"channel_id\":\"%s\",\"broadcast_id\":\"%s\",\"player\":\"%s\",\"user_id\":%d,\"game\":\"%s\"}}]"
				.formatted(CHANNEL_ID, BROADCAST_ID, PLAYER, USER_ID, GAME);
		var expectedData = new String(Base64.getEncoder().encode(json.getBytes(UTF_8)));
		
		unirest.expect(POST, SPADE_URL)
				.body("{\"data\":\"%s\"}".formatted(expectedData))
				.thenReturn()
				.withStatus(204);
		
		var request = MinuteWatchedEvent.builder()
				.properties(MinuteWatchedProperties.builder()
						.userId(USER_ID)
						.broadcastId(BROADCAST_ID)
						.channelId(CHANNEL_ID)
						.player(PLAYER)
						.game(GAME)
						.build())
				.build();
		assertThat(tested.sendPlayerEvents(spadeUrl, request)).isTrue();
		
		unirest.verifyAll();
	}
	
	@Test
	void sendMinutesWatchedNotSuccess(){
		var json = "[{\"event\":\"minute-watched\",\"properties\":{\"channel_id\":\"%s\",\"broadcast_id\":\"%s\",\"player\":\"%s\",\"user_id\":%d}}]"
				.formatted(CHANNEL_ID, BROADCAST_ID, PLAYER, USER_ID);
		var expectedData = new String(Base64.getEncoder().encode(json.getBytes(UTF_8)));
		
		unirest.expect(POST, SPADE_URL)
				.body("{\"data\":\"%s\"}".formatted(expectedData))
				.thenReturn()
				.withStatus(400);
		
		var request = MinuteWatchedEvent.builder()
				.properties(MinuteWatchedProperties.builder()
						.userId(USER_ID)
						.broadcastId(BROADCAST_ID)
						.channelId(CHANNEL_ID)
						.player(PLAYER)
						.build())
				.build();
		assertThat(tested.sendPlayerEvents(spadeUrl, request)).isFalse();
		
		unirest.verifyAll();
	}
	
	@Test
	void sendMinutesWatchedJsonError(){
		try(var jacksonUtils = Mockito.mockStatic(JacksonUtils.class)){
			var exception = mock(JsonProcessingException.class);
			when(exception.getStackTrace()).thenReturn(new StackTraceElement[0]);
			jacksonUtils.when(() -> JacksonUtils.writeAsString(any(PlayerEvent[].class))).thenThrow(exception);
			
			var request = MinuteWatchedEvent.builder()
					.properties(MinuteWatchedProperties.builder()
							.userId(USER_ID)
							.broadcastId(BROADCAST_ID)
							.channelId(CHANNEL_ID)
							.player(PLAYER)
							.build())
					.build();
			assertThat(tested.sendPlayerEvents(spadeUrl, request)).isFalse();
		}
	}
	
	@Test
	void getSpadeUrl(){
		unirest.expect(GET, STREAMER_URL)
				.thenReturn(CONFIG_BODY)
				.withStatus(200);
		
		unirest.expect(GET, CONFIG_URL)
				.thenReturn(SPADE_BODY)
				.withStatus(200);
		
		assertThat(tested.getSpadeUrl(streamerUrl)).isPresent()
				.get().isEqualTo(spadeUrl);
	}
	
	@Test
	void getSpadeUrlInvalidConfigUrlResponse(){
		unirest.expect(GET, STREAMER_URL)
				.thenReturn(CONFIG_BODY)
				.withStatus(500);
		
		assertThat(tested.getSpadeUrl(streamerUrl)).isEmpty();
	}
	
	@Test
	void getSpadeUrlNoConfigUrl(){
		unirest.expect(GET, STREAMER_URL)
				.thenReturn("")
				.withStatus(200);
		
		assertThat(tested.getSpadeUrl(streamerUrl)).isEmpty();
	}
	
	@Test
	void getSpadeUrlInvalidResponse(){
		unirest.expect(GET, STREAMER_URL)
				.thenReturn(CONFIG_BODY)
				.withStatus(200);
		
		unirest.expect(GET, CONFIG_URL)
				.thenReturn(SPADE_BODY)
				.withStatus(500);
		
		assertThat(tested.getSpadeUrl(streamerUrl)).isEmpty();
	}
	
	@Test
	void getSpadeUrlInvalidFormat(){
		unirest.expect(GET, STREAMER_URL)
				.thenReturn(CONFIG_BODY)
				.withStatus(200);
		
		unirest.expect(GET, CONFIG_URL)
				.thenReturn(SPADE_BODY_INVALID_FORMAT)
				.withStatus(200);
		
		assertThat(tested.getSpadeUrl(streamerUrl)).isEmpty();
	}
	
	@Test
	void getSpadeUrlNoUrl(){
		unirest.expect(GET, STREAMER_URL)
				.thenReturn(CONFIG_BODY)
				.withStatus(200);
		
		unirest.expect(GET, CONFIG_URL)
				.thenReturn("")
				.withStatus(200);
		
		assertThat(tested.getSpadeUrl(streamerUrl)).isEmpty();
	}
}