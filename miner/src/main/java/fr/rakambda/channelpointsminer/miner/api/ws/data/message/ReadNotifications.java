package fr.rakambda.channelpointsminer.miner.api.ws.data.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.rakambda.channelpointsminer.miner.api.ws.data.message.readnotifications.ReadNotificationsData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@JsonTypeName("read-notifications")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadNotifications extends IPubSubMessage{
	@JsonProperty("data")
	@NotNull
	private ReadNotificationsData data;
}
