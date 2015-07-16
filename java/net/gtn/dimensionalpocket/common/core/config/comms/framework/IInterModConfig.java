package net.gtn.dimensionalpocket.common.core.config.comms.framework;

public interface IInterModConfig {

	public void runModSpecificComms();

	public void sendInterModComms();

	public String getModID();
}
