package com.wecode.game.bean;

public class MemberTreeNode {
	public String uuid;
	public String username;
	public boolean isAgent;
	public boolean hasAgent;

	public MemberTreeNode(Agent agent) {
		this.uuid = agent.uuid;
		this.username = agent.username;
		this.isAgent = true;
		this.hasAgent = agent.agentNum > 0;
	}

	public MemberTreeNode(Member member) {
		this.uuid = member.uuid;
		this.username = member.username;
		this.isAgent = false;
		this.hasAgent = false;
	}
}
