package fr.raksrinana.channelpointsminer.factory;

import fr.raksrinana.channelpointsminer.api.discord.DiscordApi;
import fr.raksrinana.channelpointsminer.api.passport.PassportApi;
import fr.raksrinana.channelpointsminer.config.AccountConfiguration;
import fr.raksrinana.channelpointsminer.config.DiscordConfiguration;
import fr.raksrinana.channelpointsminer.handler.*;
import fr.raksrinana.channelpointsminer.log.DiscordLogEventListener;
import fr.raksrinana.channelpointsminer.log.LoggerLogEventListener;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinerFactoryTest{
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final boolean USE_2FA = true;
	private static final Path AUTH_FOLDER = Paths.get("/path").resolve("to").resolve("auth");
	
	@Mock
	private AccountConfiguration accountConfiguration;
	@Mock
	private PassportApi passportApi;
	@Mock
	private DiscordApi discordApi;
	@Mock
	private DiscordConfiguration discordConfiguration;
	
	@BeforeEach
	void setUp(){
		lenient().when(accountConfiguration.getUsername()).thenReturn(USERNAME);
		lenient().when(accountConfiguration.getPassword()).thenReturn(PASSWORD);
		lenient().when(accountConfiguration.getAuthenticationFolder()).thenReturn(AUTH_FOLDER);
		lenient().when(accountConfiguration.isUse2Fa()).thenReturn(USE_2FA);
		lenient().when(accountConfiguration.getDiscord()).thenReturn(discordConfiguration);
	}
	
	@Test
	void nominal(){
		try(var apiFactory = mockStatic(ApiFactory.class)){
			apiFactory.when(() -> ApiFactory.createPassportApi(USERNAME, PASSWORD, AUTH_FOLDER, USE_2FA)).thenReturn(passportApi);
			
			var miner = MinerFactory.create(accountConfiguration);
			
			assertThat(miner.getMessageHandlers())
					.hasSize(5)
					.hasAtLeastOneElementOfType(ClaimAvailableHandler.class)
					.hasAtLeastOneElementOfType(StreamStartEndHandler.class)
					.hasAtLeastOneElementOfType(FollowRaidHandler.class)
					.hasAtLeastOneElementOfType(PredictionsHandler.class)
					.hasAtLeastOneElementOfType(PointsHandler.class);
			
			assertThat(miner.getLogEventListeners())
					.hasSize(1)
					.hasAtLeastOneElementOfType(LoggerLogEventListener.class);
			
			miner.close();
		}
	}
	
	@Test
	void nominalWithDiscord() throws MalformedURLException{
		try(var apiFactory = mockStatic(ApiFactory.class)){
			var discordWebhook = new URL("https://discord-webhook");
			
			apiFactory.when(() -> ApiFactory.createPassportApi(USERNAME, PASSWORD, AUTH_FOLDER, USE_2FA)).thenReturn(passportApi);
			apiFactory.when(() -> ApiFactory.createdDiscordApi(discordWebhook)).thenReturn(discordApi);
			
			when(discordConfiguration.getUrl()).thenReturn(discordWebhook);
			
			var miner = MinerFactory.create(accountConfiguration);
			
			assertThat(miner.getMessageHandlers())
					.hasSize(5)
					.hasAtLeastOneElementOfType(ClaimAvailableHandler.class)
					.hasAtLeastOneElementOfType(StreamStartEndHandler.class)
					.hasAtLeastOneElementOfType(FollowRaidHandler.class)
					.hasAtLeastOneElementOfType(PredictionsHandler.class)
					.hasAtLeastOneElementOfType(PointsHandler.class);
			
			assertThat(miner.getLogEventListeners())
					.hasSize(2)
					.hasAtLeastOneElementOfType(LoggerLogEventListener.class)
					.hasAtLeastOneElementOfType(DiscordLogEventListener.class);
			
			miner.close();
		}
	}
}