package com.obj;

import java.util.ArrayList;
import com.obj.Vertex;

public class Group {

	private String name;
	private Vertex min = null;
	private Material material;
	private ArrayList<Face> faces = new ArrayList<Face>();

	public ArrayList<Integer> indices = new ArrayList<Integer>();
	public ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	public ArrayList<Vertex> normals = new ArrayList<Vertex>();
	public ArrayList<TextureCoordinate> texcoords = new ArrayList<TextureCoordinate>();
	public int indexCount;
	
	public Group(String name)
	{
		indexCount = 0;
		this.name = name;
	}
	
	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
	public void addFace(Face face)
	{
		faces.add(face);
	}
	
	public void pack()
	{
		float minX = 0;
		float minY = 0;
		float minZ= 0;
		Face currentFace = null;
		Vertex currentVertex = null;
		for ( int i=0;i<faces.size();i++)
		{
			currentFace = faces.get(i);
			for(int j=0;j<currentFace.getVertices().length;j++)
			{
				currentVertex = currentFace.getVertices()[j];
				if (Math.abs(currentVertex.getX()) > minX )  
					minX = Math.abs(currentVertex.getX());
				if (Math.abs(currentVertex.getY()) > minY )  
					minY = Math.abs(currentVertex.getY());
				if (Math.abs(currentVertex.getZ()) > minZ )  
					minZ = Math.abs(currentVertex.getZ());
			}
		}
		
		min = new Vertex(minX,minY,minZ);
	}

	public String getName() {
		return name;
	}

	public Material getMaterial() 
	{
		return material;
	}

	public ArrayList<Face> getFaces() {
		return faces;
	}

	public Vertex getMin() {
		return min;
	}
}
