package com.bymarcin.zettaindustries.utils.render;

import com.bymarcin.zettaindustries.utils.render.cmd.Normal;
import com.bymarcin.zettaindustries.utils.render.cmd.RenderCommand;
import com.bymarcin.zettaindustries.utils.render.cmd.Rotate;
import com.bymarcin.zettaindustries.utils.render.cmd.Translate;
import com.bymarcin.zettaindustries.utils.render.cmd.Vertex;
import com.bymarcin.zettaindustries.utils.render.cmd.VertexUV;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class CustomModelFactory {
	static float angel = 0;

	public static CustomModel createModel() {
		return new CustomModel();
	}

	public static CustomModel rotateModel(float angle, float x, float y, float z, CustomModel model) {
		CustomModel newModel = CustomModelFactory.createModel();
		newModel.setUV(model.getMinU(), model.getMaxU(), model.getMinV(), model.getMaxV());
		newModel.addCommand(new Translate(0.5f, 0.5f, 0.5f));
		newModel.addCommand(new Rotate(angle, x, y, z));
		newModel.addCommand(new Translate(-0.5f, -0.5f, -0.5f));

		for (RenderCommand cmd : model.getModelCommands()) {
			newModel.addCommand(cmd);
		}

		return newModel;
	}
	
	public static CustomModel createModel(ResourceLocation model){
		CustomModel customModel = new CustomModel();
		WavefrontObject  model1 = (WavefrontObject)AdvancedModelLoader.loadModel(model);
		
		for(GroupObject go: model1.groupObjects){
			for(Face f: go.faces){
		        if (f.faceNormal == null)
		        {
		            f.faceNormal = f.calculateFaceNormal();
		        }

		        customModel.addCommand(new Normal(f.faceNormal.x, f.faceNormal.y, f.faceNormal.z));

		        float averageU = 0F;
		        float averageV = 0F;

		        if ((f.textureCoordinates != null) && (f.textureCoordinates.length > 0))
		        {
		            for (int i = 0; i < f.textureCoordinates.length; ++i)
		            {
		                averageU += f.textureCoordinates[i].u;
		                averageV += f.textureCoordinates[i].v;
		            }

		            averageU = averageU / f.textureCoordinates.length;
		            averageV = averageV / f.textureCoordinates.length;
		        }

		        float offsetU, offsetV;

		        for (int i = 0; i < f.vertices.length; ++i)
		        {

		            if ((f.textureCoordinates != null) && (f.textureCoordinates.length > 0))
		            {
		                offsetU = 0.0005F;
		                offsetV = 0.0005F;

		                if (f.textureCoordinates[i].u > averageU)
		                {
		                    offsetU = -offsetU;
		                }
		                if (f.textureCoordinates[i].v > averageV)
		                {
		                    offsetV = -offsetV;
		                }

		                customModel.addCommand(new VertexUV(f.vertices[i].x, f.vertices[i].y, f.vertices[i].z, f.textureCoordinates[i].u + offsetU, f.textureCoordinates[i].v + offsetV));
		            }
		            else
		            {
		            	customModel.addCommand(new Vertex(f.vertices[i].x, f.vertices[i].y, f.vertices[i].z));
		            }
		        }
			}
		}
		
		return customModel;
	}
}
