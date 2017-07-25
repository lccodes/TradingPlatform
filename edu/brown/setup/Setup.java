package brown.setup;

import com.esotericsoftware.kryo.Kryo;

/**
 * set up Kryo, which handles agent-server communications.
 * @author lcamery
 *
 */
public interface Setup {
	void setup(Kryo kryo);
}
