package fr.raksrinana.twitchminer.api.ws.data.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.raksrinana.twitchminer.api.ws.data.message.subtype.Claim;
import fr.raksrinana.twitchminer.utils.json.TwitchTimestampDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.Instant;

@JsonTypeName("claim-available")
@Getter
@ToString(callSuper = true)
public class ClaimAvailable extends Message{
	@JsonProperty("data")
	private Data data;
	
	public ClaimAvailable(){
		super("claim-available");
	}
	
	@Getter
	@NoArgsConstructor
	@ToString
	static class Data{
		@JsonProperty("timestamp")
		@JsonDeserialize(using = TwitchTimestampDeserializer.class)
		private Instant timestamp;
		@JsonProperty("claim")
		private Claim claim;
	}
}