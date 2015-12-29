package com.obj.parser.mtl;

import com.obj.Material;
import com.obj.WavefrontObject;
import com.obj.parser.LineParser;

public class MaterialParser extends LineParser {

	String materialName = "";
	
	@Override
	public void incoporateResults(WavefrontObject wavefrontObject) 
	{
		Material newMaterial = new Material(materialName);
		
		wavefrontObject.getMaterials().put(materialName,newMaterial);
		wavefrontObject.setCurrentMaterial(newMaterial);
	}

	@Override
	public void parse() {
		materialName = words[1];
	}

}
