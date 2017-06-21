package brown.setup.library;

import brown.setup.Setup;
import brown.test.prediction.cda.PMRegistration;

import com.esotericsoftware.kryo.Kryo;

public final class CDAGameSetup implements Setup {
	
	public void setup(Kryo kryo) {
		kryo.register(PMRegistration.class);
	}

}
