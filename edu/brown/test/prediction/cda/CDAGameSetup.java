package brown.test.prediction.cda;

import brown.setup.Setup;

import com.esotericsoftware.kryo.Kryo;

public final class CDAGameSetup implements Setup {
	
	public void setup(Kryo kryo) {
		kryo.register(PMRegistration.class);
	}

}
