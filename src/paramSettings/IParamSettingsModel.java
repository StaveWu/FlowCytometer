package paramSettings;

public interface IParamSettingsModel {
	
	Channel getChannelAt(int index);
	
	int getChannelsCount();
	
	void addChannel(Channel channel);
	
	void removeChannel(Channel channel);
	
}
