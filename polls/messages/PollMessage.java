package messages;

import java.util.List;

import brown.messages.Message;

public class PollMessage extends Message {
  public final List<Poll> polls;
  
  public PollMessage(Integer ID, List<Poll> polls) {
    super(ID);
    this.polls = polls;
  }
  
  public PollMessage() {
    super(null);
    this.polls = null;
  }

}
