package fr.raksrinana.channelpointsminer.streamer;

import fr.raksrinana.channelpointsminer.prediction.bet.action.IPredictionAction;
import fr.raksrinana.channelpointsminer.prediction.bet.amount.IAmountCalculator;
import fr.raksrinana.channelpointsminer.prediction.bet.outcome.IOutcomePicker;
import fr.raksrinana.channelpointsminer.prediction.delay.IDelayCalculator;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PredictionSettingsTest{
	@Mock
	private IDelayCalculator delayCalculator;
	@Mock
	private IOutcomePicker outcomePicker;
	@Mock
	private IAmountCalculator amountCalculator;
	@Mock
	private IPredictionAction predictionAction;
	
	@Test
	void copy(){
		var actions = List.of(this.predictionAction);
		var tested = PredictionSettings.builder()
				.delayCalculator(delayCalculator)
				.minimumPointsRequired(25)
				.outcomePicker(outcomePicker)
				.amountCalculator(amountCalculator)
				.actions(actions)
				.build();
		
		var copy = new PredictionSettings(tested);
		
		assertThat(copy.getMinimumPointsRequired()).isEqualTo(tested.getMinimumPointsRequired());
		assertThat(copy.getDelayCalculator()).isSameAs(delayCalculator);
		assertThat(copy.getOutcomePicker()).isSameAs(outcomePicker);
		assertThat(copy.getAmountCalculator()).isSameAs(amountCalculator);
		assertThat(copy.getActions()).isSameAs(actions);
	}
}