package brown.lab.lab3;

import brown.assets.value.Tradeable;

/**
 * Implementation of a good for Lab3.
 * 
 * @author lcamery
 */
public class Lab3Good implements Tradeable {

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

}
