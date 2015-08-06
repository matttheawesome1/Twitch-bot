package twitchBot;

import java.util.concurrent.atomic.AtomicBoolean;

import org.pircbotx.Configuration;
import org.pircbotx.Configuration.Builder;
import org.pircbotx.MultiBotManager;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.managers.ThreadedListenerManager;

public class ConnectToServer extends ListenerAdapter<PircBotX>
{			
	private int botNum;
	private MultiBotManager<PircBotX> m;
	AtomicBoolean shutdown = new AtomicBoolean(false);
	
	private ConnectToServer() {
	}

		//Set up Configuration
		public static void main(String[] args) throws Exception {
			ConnectToServer m = new ConnectToServer();
			
			Builder<PircBotX> builder = new Configuration.Builder<PircBotX>()
				.setName("botname")
				.setLogin("name of bot") 
				.setServerPassword("oath inserted here") //get this from twitchapps.com/tmi
				.setAutoNickChange(true)
				.setCapEnabled(true)
				.setListenerManager(new ThreadedListenerManager<PircBotX>())
				.addListener(m.new CommandListener())
				.setServerHostname("irc.twitch.tv")
				.setServerPort(6667)
				.addAutoJoinChannel("#Enter your channel here. The octothorpe is necessary (the pound sign)");
		final PircBotX twitch = new PircBotX(builder.buildConfiguration());
		//create our bot
		
		MultiBotManager<PircBotX> manager = new MultiBotManager<PircBotX>();
		m.m = manager;
		m.botNum = twitch.getBotId();
		
		manager.addBot(twitch);
		
		//Attempt connection
		try {
			twitch.startBot();
			manager.start();
		} catch (Exception ex) {
			ex.getStackTrace();
			ex.printStackTrace();
			}
			//A loop to keep this alive
			while(!m.shutdown.get()){
				Thread.sleep(1000);
			}
	}
			@SuppressWarnings("rawtypes")
			public void onMessage(MessageEvent event) throws Exception{
				if (event.getMessage().equalsIgnoreCase("!Hello")) //Test message, if you see it the bot has connected. 
					event.respond("Hello, and thank you for testing me. If you can see this, it means that I am up and running~");
			}
		}