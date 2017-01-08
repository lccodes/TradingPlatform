package brown.test.prediction;

public final class LaunchTestGame {
	public static void main(String[] args) {
		TestServer serv = new TestServer(9922);
		serv.startGame();
	}
}
