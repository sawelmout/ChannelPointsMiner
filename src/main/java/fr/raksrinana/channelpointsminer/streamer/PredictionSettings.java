package fr.raksrinana.channelpointsminer.streamer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.raksrinana.channelpointsminer.prediction.bet.action.IPredictionAction;
import fr.raksrinana.channelpointsminer.prediction.bet.amount.IAmountCalculator;
import fr.raksrinana.channelpointsminer.prediction.bet.amount.PercentageAmount;
import fr.raksrinana.channelpointsminer.prediction.bet.outcome.IOutcomePicker;
import fr.raksrinana.channelpointsminer.prediction.bet.outcome.SmartOutcomePicker;
import fr.raksrinana.channelpointsminer.prediction.delay.FromEndDelay;
import fr.raksrinana.channelpointsminer.prediction.delay.IDelayCalculator;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class PredictionSettings{
	@JsonProperty("delayCalculator")
	@NotNull
	@Builder.Default
	private IDelayCalculator delayCalculator = FromEndDelay.builder().seconds(10).build();
	@JsonProperty("minimumPointsRequired")
	@Builder.Default
	private int minimumPointsRequired = 0;
	@JsonProperty("outcomePicker")
	@NotNull
	@Builder.Default
	private IOutcomePicker outcomePicker = SmartOutcomePicker.builder().percentageGap(.2f).build();
	@JsonProperty("amountCalculator")
	@NotNull
	@Builder.Default
	private IAmountCalculator amountCalculator = PercentageAmount.builder().percentage(.2F).max(50_000).build();
	@JsonProperty("actions")
	@NotNull
	@Builder.Default
	private List<IPredictionAction> actions = new ArrayList<>();
	
	public PredictionSettings(PredictionSettings origin){
		delayCalculator = origin.delayCalculator;
		minimumPointsRequired = origin.minimumPointsRequired;
		outcomePicker = origin.outcomePicker;
		amountCalculator = origin.amountCalculator;
		actions = origin.actions;
	}
}
