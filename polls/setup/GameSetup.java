package setup;

import messages.Poll;
import messages.PollMessage;

import com.esotericsoftware.kryo.Kryo;

public final class GameSetup {
  public static void setup(Kryo kryo) {
    kryo.register(Poll.class);
    kryo.register(PollMessage.class);
  }
}
