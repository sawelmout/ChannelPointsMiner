package fr.rakambda.channelpointsminer.miner.event.impl;

import fr.rakambda.channelpointsminer.miner.event.AbstractLoggableStreamerEvent;
import fr.rakambda.channelpointsminer.miner.miner.IMiner;
import fr.rakambda.channelpointsminer.miner.streamer.Streamer;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@ToString
public class StreamDownEvent extends AbstractLoggableStreamerEvent{
	public StreamDownEvent(@NotNull IMiner miner, @NotNull String streamerId, @Nullable String streamerUsername, @Nullable Streamer streamer, @NotNull Instant instant){
		super(miner, streamerId, streamerUsername, streamer, instant);
	}
	
	@Override
	@NotNull
	public String getConsoleLogFormat(){
		return "Stream stopped";
	}
	
	@Override
	@NotNull
	public String getDefaultFormat(){
		return "[{username}] {emoji} {streamer} : Stream stopped";
	}
	
	@Override
	@NotNull
	protected String getColor(){
		return COLOR_INFO;
	}
	
	@Override
	@NotNull
	protected String getEmoji(){
		return "⏹️";
	}
}
