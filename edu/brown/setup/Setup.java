package brown.setup;

import com.esotericsoftware.kryo.Kryo;

public interface Setup {
	void setup(Kryo kryo);
}
