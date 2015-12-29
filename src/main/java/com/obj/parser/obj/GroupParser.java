package com.obj.parser.obj;

import com.obj.Group;
import com.obj.WavefrontObject;
import com.obj.parser.LineParser;

public class GroupParser extends LineParser {

	Group newGroup = null;
	
	@Override
	public void incoporateResults(WavefrontObject wavefrontObject) {
		
		if (wavefrontObject.getCurrentGroup() != null)
			wavefrontObject.getCurrentGroup().pack();
		
		wavefrontObject.getGroups().add(newGroup);
		wavefrontObject.getGroupsDirectAccess().put(newGroup.getName(),newGroup);
		
		wavefrontObject.setCurrentGroup(newGroup);
	}

	@Override
	public void parse() {
		
		String groupName = words[1];
		newGroup = new Group(groupName);
	}

}
