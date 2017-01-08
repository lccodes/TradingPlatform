package brown.test.prediction;

import com.esotericsoftware.kryo.Kryo;

public final class GameSetup {
	
	public static void setup(Kryo kryo) {
		kryo.register(TestShare.class);
	}

}
