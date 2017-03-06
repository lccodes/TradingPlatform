package brown.lab.lab3;

import brown.assets.accounting.Account;
import brown.assets.value.FullType;
import brown.assets.value.StateOfTheWorld;
import brown.assets.value.ITradeable;

/**
 * Implementation of a good for Lab3.
 * 
 * @author lcamery
 */
public class Lab3Good implements ITradeable {

  /**
   * Id of the agent that currently owns the good.
   */
  private Integer agentId;

  /**
   * Empty Constructor.
   */
  public Lab3Good() {

  }

  /**
   * Constructor.
   * 
   * @param agentId
   */
  public Lab3Good(Integer agentId) {
    this.agentId = agentId;
  }

  @Override
  public Integer getAgentID() {
    return this.agentId;
  }

  @Override
  public void setAgentID(Integer ID) {
    this.agentId = ID;
  }

  @Override
  public double getCount() {
    return 1;
  }

@Override
public void setCount(double count) {
	// TODO Auto-generated method stub
	
}

@Override
public FullType getType() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Account close(StateOfTheWorld closingState) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public ITradeable split(double newCount) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public ITradeable toAgent() {
	// TODO Auto-generated method stub
	return null;
}

}
