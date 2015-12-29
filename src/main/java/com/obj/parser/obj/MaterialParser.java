package com.obj.parser.obj;

import com.obj.Material;
import com.obj.WavefrontObject;
import com.obj.parser.LineParser;

public class MaterialParser extends LineParser 
{
		String materialName = "";

		@Override
		public void parse() {
			materialName = words[1];
		}

		@Override
		public void incoporateResults(WavefrontObject wavefrontObject) {
			Material newMaterial = wavefrontObject.getMaterials().get(materialName);
			wavefrontObject.getCurrentGroup().setMaterial(newMaterial);
			
		}

	

}
