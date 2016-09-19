package brown.test;

import com.esotericsoftware.kryo.Kryo;

public final class GameSetup {
	
	public static void setup(Kryo kryo) {
		kryo.register(TestPM.class);
		kryo.register(TestShare.class);
	}

}
