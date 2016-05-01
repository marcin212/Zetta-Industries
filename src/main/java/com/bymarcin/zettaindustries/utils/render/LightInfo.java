package com.bymarcin.zettaindustries.utils.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;


	//@SideOnly(Side.CLIENT)
	public class LightInfo {
//	    /** The IBlockAccess used by this instance of RenderBlocks */
//	    public IBlockAccess blockAccess;
//	    /** If set to >=0, all block faces will be rendered using this texture index */
//	    public IIcon overrideBlockTexture;
//	    /** Set to true if the texture should be flipped horizontally during render*Face */
//	    public boolean flipTexture;
//	    public boolean field_152631_f;
//	    /** If true, renders all faces on all blocks rather than using the logic in Block.shouldSideBeRendered. */
//	    public boolean renderAllFaces;
//	    /** Fancy grass side matching biome */
//	    public static boolean fancyGrass = true;
//	    public boolean useInventoryTint = true;
//	    public boolean renderFromInside = false;
//	    /** The minimum X value for rendering (default 0.0). */
//	    public double renderMinX;
//	    /** The maximum X value for rendering (default 1.0). */
//	    public double renderMaxX;
//	    /** The minimum Y value for rendering (default 0.0). */
//	    public double renderMinY;
//	    /** The maximum Y value for rendering (default 1.0). */
//	    public double renderMaxY;
//	    /** The minimum Z value for rendering (default 0.0). */
//	    public double renderMinZ;
//	    /** The maximum Z value for rendering (default 1.0). */
//	    public double renderMaxZ;
//	    public boolean lockBlockBounds;
//	    public boolean partialRenderBounds;
//
//	    public int uvRotateEast;
//	    public int uvRotateWest;
//	    public int uvRotateSouth;
//	    public int uvRotateNorth;
//	    public int uvRotateTop;
//	    public int uvRotateBottom;
//	    /** Used as a scratch variable for ambient occlusion on the north/bottom/east corner. */
//	    public float aoLightValueScratchXYZNNN;
//	    /** Used as a scratch variable for ambient occlusion between the bottom face and the north face. */
//	    public float aoLightValueScratchXYNN;
//	    /** Used as a scratch variable for ambient occlusion on the north/bottom/west corner. */
//	    public float aoLightValueScratchXYZNNP;
//	    /** Used as a scratch variable for ambient occlusion between the bottom face and the east face. */
//	    public float aoLightValueScratchYZNN;
//	    /** Used as a scratch variable for ambient occlusion between the bottom face and the west face. */
//	    public float aoLightValueScratchYZNP;
//	    /** Used as a scratch variable for ambient occlusion on the south/bottom/east corner. */
//	    public float aoLightValueScratchXYZPNN;
//	    /** Used as a scratch variable for ambient occlusion between the bottom face and the south face. */
//	    public float aoLightValueScratchXYPN;
//	    /** Used as a scratch variable for ambient occlusion on the south/bottom/west corner. */
//	    public float aoLightValueScratchXYZPNP;
//	    /** Used as a scratch variable for ambient occlusion on the north/top/east corner. */
//	    public float aoLightValueScratchXYZNPN;
//	    /** Used as a scratch variable for ambient occlusion between the top face and the north face. */
//	    public float aoLightValueScratchXYNP;
//	    /** Used as a scratch variable for ambient occlusion on the north/top/west corner. */
//	    public float aoLightValueScratchXYZNPP;
//	    /** Used as a scratch variable for ambient occlusion between the top face and the east face. */
//	    public float aoLightValueScratchYZPN;
//	    /** Used as a scratch variable for ambient occlusion on the south/top/east corner. */
//	    public float aoLightValueScratchXYZPPN;
//	    /** Used as a scratch variable for ambient occlusion between the top face and the south face. */
//	    public float aoLightValueScratchXYPP;
//	    /** Used as a scratch variable for ambient occlusion between the top face and the west face. */
//	    public float aoLightValueScratchYZPP;
//	    /** Used as a scratch variable for ambient occlusion on the south/top/west corner. */
//	    public float aoLightValueScratchXYZPPP;
//	    /** Used as a scratch variable for ambient occlusion between the north face and the east face. */
//	    public float aoLightValueScratchXZNN;
//	    /** Used as a scratch variable for ambient occlusion between the south face and the east face. */
//	    public float aoLightValueScratchXZPN;
//	    /** Used as a scratch variable for ambient occlusion between the north face and the west face. */
//	    public float aoLightValueScratchXZNP;
//	    /** Used as a scratch variable for ambient occlusion between the south face and the west face. */
//	    public float aoLightValueScratchXZPP;
//	    /** Ambient occlusion brightness XYZNNN */
//	    public int aoBrightnessXYZNNN;
//	    /** Ambient occlusion brightness XYNN */
//	    public int aoBrightnessXYNN;
//	    /** Ambient occlusion brightness XYZNNP */
//	    public int aoBrightnessXYZNNP;
//	    /** Ambient occlusion brightness YZNN */
//	    public int aoBrightnessYZNN;
//	    /** Ambient occlusion brightness YZNP */
//	    public int aoBrightnessYZNP;
//	    /** Ambient occlusion brightness XYZPNN */
//	    public int aoBrightnessXYZPNN;
//	    /** Ambient occlusion brightness XYPN */
//	    public int aoBrightnessXYPN;
//	    /** Ambient occlusion brightness XYZPNP */
//	    public int aoBrightnessXYZPNP;
//	    /** Ambient occlusion brightness XYZNPN */
//	    public int aoBrightnessXYZNPN;
//	    /** Ambient occlusion brightness XYNP */
//	    public int aoBrightnessXYNP;
//	    /** Ambient occlusion brightness XYZNPP */
//	    public int aoBrightnessXYZNPP;
//	    /** Ambient occlusion brightness YZPN */
//	    public int aoBrightnessYZPN;
//	    /** Ambient occlusion brightness XYZPPN */
//	    public int aoBrightnessXYZPPN;
//	    /** Ambient occlusion brightness XYPP */
//	    public int aoBrightnessXYPP;
//	    /** Ambient occlusion brightness YZPP */
//	    public int aoBrightnessYZPP;
//	    /** Ambient occlusion brightness XYZPPP */
//	    public int aoBrightnessXYZPPP;
//	    /** Ambient occlusion brightness XZNN */
//	    public int aoBrightnessXZNN;
//	    /** Ambient occlusion brightness XZPN */
//	    public int aoBrightnessXZPN;
//	    /** Ambient occlusion brightness XZNP */
//	    public int aoBrightnessXZNP;
//	    /** Ambient occlusion brightness XZPP */
//	    public int aoBrightnessXZPP;
//	    /** Brightness top left */
//	    public int brightnessTopLeft;
//	    /** Brightness bottom left */
//	    public int brightnessBottomLeft;
//	    /** Brightness bottom right */
//	    public int brightnessBottomRight;
//	    /** Brightness top right */
//	    public int brightnessTopRight;
//	    /** Red color value for the top left corner */
//	    public float colorRedTopLeft;
//	    /** Red color value for the bottom left corner */
//	    public float colorRedBottomLeft;
//	    /** Red color value for the bottom right corner */
//	    public float colorRedBottomRight;
//	    /** Red color value for the top right corner */
//	    public float colorRedTopRight;
//	    /** Green color value for the top left corner */
//	    public float colorGreenTopLeft;
//	    /** Green color value for the bottom left corner */
//	    public float colorGreenBottomLeft;
//	    /** Green color value for the bottom right corner */
//	    public float colorGreenBottomRight;
//	    /** Green color value for the top right corner */
//	    public float colorGreenTopRight;
//	    /** Blue color value for the top left corner */
//	    public float colorBlueTopLeft;
//	    /** Blue color value for the bottom left corner */
//	    public float colorBlueBottomLeft;
//	    /** Blue color value for the bottom right corner */
//	    public float colorBlueBottomRight;
//	    /** Blue color value for the top right corner */
//	    public float colorBlueTopRight;
//
//	    
//	    public LightInfo(IBlockAccess world){
//	    	blockAccess = world;
//	    }
//	    
//	    /**
//	     * 0-FaceYNeg
//	     * 1-FaceYPos
//	     * 2-FaceZNeg
//	     * 3-FaceZPos
//	     * 4-FaceXNeg
//	     * 5-FaceXPos
//	     * @param block
//	     * @param x
//	     * @param y
//	     * @param z
//	     * @param r
//	     * @param g
//	     * @param b
//	     * @param side
//	     * @return
//	     */
//	    public boolean renderStandardBlock(int x, int y, int z, int side)
//	    {
//	    	Block block = this.blockAccess.getBlock(x, y, z);
//	        return this.renderStandardBlockWithAmbientOcclusion(block,x,y,z, 1, 1, 1, side);
//	    }
//
//	    /**
//	     * 0-FaceYNeg
//	     * 1-FaceYPos
//	     * 2-FaceZNeg
//	     * 3-FaceZPos
//	     * 4-FaceXNeg
//	     * 5-FaceXPos
//	     * @param block
//	     * @param x
//	     * @param y
//	     * @param z
//	     * @param r
//	     * @param g
//	     * @param b
//	     * @param side
//	     * @return
//	     */
//	    public boolean renderStandardBlockWithAmbientOcclusion(Block block, int x, int y, int z, float r, float g, float b, int side)
//	    {
//	        float f3 = 0.0F;
//	        float f4 = 0.0F;
//	        float f5 = 0.0F;
//	        float f6 = 0.0F;
//	        boolean flag1 = true;
//	        int l = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z);
//	        Tessellator tessellator = Tessellator.instance;
//	        tessellator.setBrightness(983055);
//
//	        boolean flag2;
//	        boolean flag3;
//	        boolean flag4;
//	        boolean flag5;
//	        int i1;
//	        float f7;
//
//	        if (side == 0 && block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0))
//	        {
//	            if (this.renderMinY <= 0.0D)
//	            {
//	                --y;
//	            }
//
//	            this.aoBrightnessXYNN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z);
//	            this.aoBrightnessYZNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1);
//	            this.aoBrightnessYZNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1);
//	            this.aoBrightnessXYPN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z);
//	            this.aoLightValueScratchXYNN = this.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchYZNN = this.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchYZNP = this.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchXYPN = this.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
//	            flag2 = this.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
//	            flag3 = this.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
//	            flag4 = this.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();
//	            flag5 = this.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();
//
//	            if (!flag5 && !flag3)
//	            {
//	                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
//	                this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNNN = this.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z - 1);
//	            }
//
//	            if (!flag4 && !flag3)
//	            {
//	                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
//	                this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNNP = this.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z + 1);
//	            }
//
//	            if (!flag5 && !flag2)
//	            {
//	                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
//	                this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPNN = this.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z - 1);
//	            }
//
//	            if (!flag4 && !flag2)
//	            {
//	                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
//	                this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPNP = this.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z + 1);
//	            }
//
//	            if (this.renderMinY <= 0.0D)
//	            {
//	                ++y;
//	            }
//
//	            i1 = l;
//
//	            if (this.renderMinY <= 0.0D || !this.blockAccess.getBlock(x, y - 1, z).isOpaqueCube())
//	            {
//	                i1 = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z);
//	            }
//
//	            f7 = this.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
//	            f3 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + f7) / 4.0F;
//	            f6 = (this.aoLightValueScratchYZNP + f7 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
//	            f5 = (f7 + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
//	            f4 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + f7 + this.aoLightValueScratchYZNN) / 4.0F;
//	            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, i1);
//	            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, i1);
//	            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, i1);
//	            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, i1);
//
//	            if (flag1)
//	            {
//	                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = r * 0.5F;
//	                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = g * 0.5F;
//	                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = b * 0.5F;
//	            }
//	            else
//	            {
//	                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.5F;
//	                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.5F;
//	                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.5F;
//	            }
//
//	            this.colorRedTopLeft *= f3;
//	            this.colorGreenTopLeft *= f3;
//	            this.colorBlueTopLeft *= f3;
//	            this.colorRedBottomLeft *= f4;
//	            this.colorGreenBottomLeft *= f4;
//	            this.colorBlueBottomLeft *= f4;
//	            this.colorRedBottomRight *= f5;
//	            this.colorGreenBottomRight *= f5;
//	            this.colorBlueBottomRight *= f5;
//	            this.colorRedTopRight *= f6;
//	            this.colorGreenTopRight *= f6;
//	            this.colorBlueTopRight *= f6;
//	            return true;
//	        }
//
//	        if (side==1 && block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1))
//	        {
//	            if (this.renderMaxY >= 1.0D)
//	            {
//	                ++y;
//	            }
//
//	            this.aoBrightnessXYNP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z);
//	            this.aoBrightnessXYPP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z);
//	            this.aoBrightnessYZPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1);
//	            this.aoBrightnessYZPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1);
//	            this.aoLightValueScratchXYNP = this.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchXYPP = this.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchYZPN = this.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchYZPP = this.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
//	            flag2 = this.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
//	            flag3 = this.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
//	            flag4 = this.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
//	            flag5 = this.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();
//
//	            if (!flag5 && !flag3)
//	            {
//	                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
//	                this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNPN = this.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z - 1);
//	            }
//
//	            if (!flag5 && !flag2)
//	            {
//	                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
//	                this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPPN = this.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z - 1);
//	            }
//
//	            if (!flag4 && !flag3)
//	            {
//	                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
//	                this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNPP = this.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z + 1);
//	            }
//
//	            if (!flag4 && !flag2)
//	            {
//	                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
//	                this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPPP = this.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z + 1);
//	            }
//
//	            if (this.renderMaxY >= 1.0D)
//	            {
//	                --y;
//	            }
//
//	            i1 = l;
//
//	            if (this.renderMaxY >= 1.0D || !this.blockAccess.getBlock(x, y + 1, z).isOpaqueCube())
//	            {
//	                i1 = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z);
//	            }
//
//	            f7 = this.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
//	            f6 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + f7) / 4.0F;
//	            f3 = (this.aoLightValueScratchYZPP + f7 + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
//	            f4 = (f7 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
//	            f5 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + f7 + this.aoLightValueScratchYZPN) / 4.0F;
//	            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, i1);
//	            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, i1);
//	            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, i1);
//	            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
//	            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = r;
//	            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = g;
//	            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = b;
//	            this.colorRedTopLeft *= f3;
//	            this.colorGreenTopLeft *= f3;
//	            this.colorBlueTopLeft *= f3;
//	            this.colorRedBottomLeft *= f4;
//	            this.colorGreenBottomLeft *= f4;
//	            this.colorBlueBottomLeft *= f4;
//	            this.colorRedBottomRight *= f5;
//	            this.colorGreenBottomRight *= f5;
//	            this.colorBlueBottomRight *= f5;
//	            this.colorRedTopRight *= f6;
//	            this.colorGreenTopRight *= f6;
//	            this.colorBlueTopRight *= f6;
//	            return  true;
//	        }
//
//	        if (side==2 || block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2))
//	        {
//	            if (this.renderMinZ <= 0.0D)
//	            {
//	                --z;
//	            }
//
//	            this.aoLightValueScratchXZNN = this.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchYZNN = this.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchYZPN = this.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchXZPN = this.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
//	            this.aoBrightnessXZNN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z);
//	            this.aoBrightnessYZNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z);
//	            this.aoBrightnessYZPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z);
//	            this.aoBrightnessXZPN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z);
//	            flag2 = this.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();
//	            flag3 = this.blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
//	            flag4 = this.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();
//	            flag5 = this.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();
//
//	            if (!flag3 && !flag5)
//	            {
//	                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
//	                this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNNN = this.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y - 1, z);
//	            }
//
//	            if (!flag3 && !flag4)
//	            {
//	                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
//	                this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNPN = this.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y + 1, z);
//	            }
//
//	            if (!flag2 && !flag5)
//	            {
//	                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
//	                this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPNN = this.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y - 1, z);
//	            }
//
//	            if (!flag2 && !flag4)
//	            {
//	                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
//	                this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPPN = this.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y + 1, z);
//	            }
//
//	            if (this.renderMinZ <= 0.0D)
//	            {
//	                ++z;
//	            }
//
//	            i1 = l;
//
//	            if (this.renderMinZ <= 0.0D || !this.blockAccess.getBlock(x, y, z - 1).isOpaqueCube())
//	            {
//	                i1 = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1);
//	            }
//
//	            f7 = this.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
//	            f3 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + f7 + this.aoLightValueScratchYZPN) / 4.0F;
//	            f4 = (f7 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
//	            f5 = (this.aoLightValueScratchYZNN + f7 + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
//	            f6 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + f7) / 4.0F;
//	            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
//	            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, i1);
//	            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, i1);
//	            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, i1);
//
//	            if (flag1)
//	            {
//	                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = r * 0.8F;
//	                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = g * 0.8F;
//	                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = b * 0.8F;
//	            }
//	            else
//	            {
//	                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
//	                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
//	                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
//	            }
//
//	            this.colorRedTopLeft *= f3;
//	            this.colorGreenTopLeft *= f3;
//	            this.colorBlueTopLeft *= f3;
//	            this.colorRedBottomLeft *= f4;
//	            this.colorGreenBottomLeft *= f4;
//	            this.colorBlueBottomLeft *= f4;
//	            this.colorRedBottomRight *= f5;
//	            this.colorGreenBottomRight *= f5;
//	            this.colorBlueBottomRight *= f5;
//	            this.colorRedTopRight *= f6;
//	            this.colorGreenTopRight *= f6;
//	            this.colorBlueTopRight *= f6;
//
//	            return true;
//	        }
//
//	        if (side==3 || block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3))
//	        {
//	            if (this.renderMaxZ >= 1.0D)
//	            {
//	                ++z;
//	            }
//
//	            this.aoLightValueScratchXZNP = this.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchXZPP = this.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchYZNP = this.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchYZPP = this.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
//	            this.aoBrightnessXZNP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z);
//	            this.aoBrightnessXZPP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z);
//	            this.aoBrightnessYZNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z);
//	            this.aoBrightnessYZPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z);
//	            flag2 = this.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
//	            flag3 = this.blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();
//	            flag4 = this.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
//	            flag5 = this.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();
//
//	            if (!flag3 && !flag5)
//	            {
//	                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
//	                this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNNP = this.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y - 1, z);
//	            }
//
//	            if (!flag3 && !flag4)
//	            {
//	                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
//	                this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNPP = this.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y + 1, z);
//	            }
//
//	            if (!flag2 && !flag5)
//	            {
//	                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
//	                this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPNP = this.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y - 1, z);
//	            }
//
//	            if (!flag2 && !flag4)
//	            {
//	                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
//	                this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPPP = this.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y + 1, z);
//	            }
//
//	            if (this.renderMaxZ >= 1.0D)
//	            {
//	                --z;
//	            }
//
//	            i1 = l;
//
//	            if (this.renderMaxZ >= 1.0D || !this.blockAccess.getBlock(x, y, z + 1).isOpaqueCube())
//	            {
//	                i1 = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1);
//	            }
//
//	            f7 = this.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
//	            f3 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + f7 + this.aoLightValueScratchYZPP) / 4.0F;
//	            f6 = (f7 + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
//	            f5 = (this.aoLightValueScratchYZNP + f7 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
//	            f4 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + f7) / 4.0F;
//	            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, i1);
//	            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, i1);
//	            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
//	            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, i1);
//
//	            if (flag1)
//	            {
//	                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = r * 0.8F;
//	                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = g * 0.8F;
//	                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = b * 0.8F;
//	            }
//	            else
//	            {
//	                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
//	                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
//	                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
//	            }
//
//	            this.colorRedTopLeft *= f3;
//	            this.colorGreenTopLeft *= f3;
//	            this.colorBlueTopLeft *= f3;
//	            this.colorRedBottomLeft *= f4;
//	            this.colorGreenBottomLeft *= f4;
//	            this.colorBlueBottomLeft *= f4;
//	            this.colorRedBottomRight *= f5;
//	            this.colorGreenBottomRight *= f5;
//	            this.colorBlueBottomRight *= f5;
//	            this.colorRedTopRight *= f6;
//	            this.colorGreenTopRight *= f6;
//	            this.colorBlueTopRight *= f6;
//
//	            return true;
//	        }
//
//	        if (side == 4 || block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4))
//	        {
//	            if (this.renderMinX <= 0.0D)
//	            {
//	                --x;
//	            }
//
//	            this.aoLightValueScratchXYNN = this.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchXZNN = this.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchXZNP = this.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchXYNP = this.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
//	            this.aoBrightnessXYNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z);
//	            this.aoBrightnessXZNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1);
//	            this.aoBrightnessXZNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1);
//	            this.aoBrightnessXYNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z);
//	            flag2 = this.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
//	            flag3 = this.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
//	            flag4 = this.blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
//	            flag5 = this.blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();
//
//	            if (!flag4 && !flag3)
//	            {
//	                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
//	                this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNNN = this.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z - 1);
//	            }
//
//	            if (!flag5 && !flag3)
//	            {
//	                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
//	                this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNNP = this.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z + 1);
//	            }
//
//	            if (!flag4 && !flag2)
//	            {
//	                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
//	                this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNPN = this.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z - 1);
//	            }
//
//	            if (!flag5 && !flag2)
//	            {
//	                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
//	                this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZNPP = this.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z + 1);
//	            }
//
//	            if (this.renderMinX <= 0.0D)
//	            {
//	                ++x;
//	            }
//
//	            i1 = l;
//
//	            if (this.renderMinX <= 0.0D || !this.blockAccess.getBlock(x - 1, y, z).isOpaqueCube())
//	            {
//	                i1 = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z);
//	            }
//
//	            f7 = this.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
//	            f6 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + f7 + this.aoLightValueScratchXZNP) / 4.0F;
//	            f3 = (f7 + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
//	            f4 = (this.aoLightValueScratchXZNN + f7 + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
//	            f5 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + f7) / 4.0F;
//	            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, i1);
//	            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, i1);
//	            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, i1);
//	            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, i1);
//
//	            if (flag1)
//	            {
//	                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = r * 0.6F;
//	                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = g * 0.6F;
//	                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = b * 0.6F;
//	            }
//	            else
//	            {
//	                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
//	                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
//	                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
//	            }
//
//	            this.colorRedTopLeft *= f3;
//	            this.colorGreenTopLeft *= f3;
//	            this.colorBlueTopLeft *= f3;
//	            this.colorRedBottomLeft *= f4;
//	            this.colorGreenBottomLeft *= f4;
//	            this.colorBlueBottomLeft *= f4;
//	            this.colorRedBottomRight *= f5;
//	            this.colorGreenBottomRight *= f5;
//	            this.colorBlueBottomRight *= f5;
//	            this.colorRedTopRight *= f6;
//	            this.colorGreenTopRight *= f6;
//	            this.colorBlueTopRight *= f6;
//
//	            return true;
//	        }
//
//	        if (side == 5 || block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5))
//	        {
//	            if (this.renderMaxX >= 1.0D)
//	            {
//	                ++x;
//	            }
//
//	            this.aoLightValueScratchXYPN = this.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchXZPN = this.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchXZPP = this.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
//	            this.aoLightValueScratchXYPP = this.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
//	            this.aoBrightnessXYPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z);
//	            this.aoBrightnessXZPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1);
//	            this.aoBrightnessXZPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1);
//	            this.aoBrightnessXYPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z);
//	            flag2 = this.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
//	            flag3 = this.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
//	            flag4 = this.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
//	            flag5 = this.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();
//
//	            if (!flag3 && !flag5)
//	            {
//	                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
//	                this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPNN = this.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z - 1);
//	            }
//
//	            if (!flag3 && !flag4)
//	            {
//	                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
//	                this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPNP = this.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z + 1);
//	            }
//
//	            if (!flag2 && !flag5)
//	            {
//	                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
//	                this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPPN = this.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z - 1);
//	            }
//
//	            if (!flag2 && !flag4)
//	            {
//	                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
//	                this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
//	            }
//	            else
//	            {
//	                this.aoLightValueScratchXYZPPP = this.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
//	                this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z + 1);
//	            }
//
//	            if (this.renderMaxX >= 1.0D)
//	            {
//	                --x;
//	            }
//
//	            i1 = l;
//
//	            if (this.renderMaxX >= 1.0D || !this.blockAccess.getBlock(x + 1, y, z).isOpaqueCube())
//	            {
//	                i1 = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z);
//	            }
//
//	            f7 = this.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
//	            f3 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + f7 + this.aoLightValueScratchXZPP) / 4.0F;
//	            f4 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + f7) / 4.0F;
//	            f5 = (this.aoLightValueScratchXZPN + f7 + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
//	            f6 = (f7 + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
//	            this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
//	            this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, i1);
//	            this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, i1);
//	            this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, i1);
//
//	            if (flag1)
//	            {
//	                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = r * 0.6F;
//	                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = g * 0.6F;
//	                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = b * 0.6F;
//	            }
//	            else
//	            {
//	                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
//	                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
//	                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
//	            }
//
//	            this.colorRedTopLeft *= f3;
//	            this.colorGreenTopLeft *= f3;
//	            this.colorBlueTopLeft *= f3;
//	            this.colorRedBottomLeft *= f4;
//	            this.colorGreenBottomLeft *= f4;
//	            this.colorBlueBottomLeft *= f4;
//	            this.colorRedBottomRight *= f5;
//	            this.colorGreenBottomRight *= f5;
//	            this.colorBlueBottomRight *= f5;
//	            this.colorRedTopRight *= f6;
//	            this.colorGreenTopRight *= f6;
//	            this.colorBlueTopRight *= f6;
//
//	            return true;
//	        }
//	        
//	        return false;
//	    }
//	       
//
//	    /**
//	     * Renders the given texture to the east (x-positive) face of the block.  Args: block, x, y, z, texture
//	     */
//	    public void renderFaceXPos(Block p_147764_1_, double p_147764_2_, double p_147764_4_, double p_147764_6_, IIcon p_147764_8_)
//	    {
//
////	            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
////	            tessellator.setBrightness(this.brightnessTopLeft);
////	            tessellator.addVertexWithUV(d11, d12, d15, d8, d10);
////	            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
////	            tessellator.setBrightness(this.brightnessBottomLeft);
////	            tessellator.addVertexWithUV(d11, d12, d14, d4, d6);
////	            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
////	            tessellator.setBrightness(this.brightnessBottomRight);
////	            tessellator.addVertexWithUV(d11, d13, d14, d7, d9);
////	            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
////	            tessellator.setBrightness(this.brightnessTopRight);
////	            tessellator.addVertexWithUV(d11, d13, d15, d3, d5);
//
//	    }
//
//
//
//			public int getAoBrightness(int par0, int par1, int par2, int par3)
//			{
//				if (par0 == 0)
//					par0 = par3;
//				if (par1 == 0)
//					par1 = par3;
//				if (par2 == 0)
//					par2 = par3;
//				return par0 + par1 + par2 + par3 >> 2 & 16711935;
//			}
//			public int mixAoBrightness(int par0, int par1, int par2, int par3, double par4, double par5, double par6, double par7)
//			{
//				int i1 = (int)((double)(par0 >> 16 & 255) * par4 + (double)(par1 >> 16 & 255) * par5 + (double)(par2 >> 16 & 255) * par6 + (double)(par3 >> 16 & 255) * par7) & 255;
//				int j1 = (int)((double)(par0 & 255) * par4 + (double)(par1 & 255) * par5 + (double)(par2 & 255) * par6 + (double)(par3 & 255) * par7) & 255;
//				return i1 << 16 | j1;
//			}
//			
//		public static LightInfo calculateBlockLighting(int side, IBlockAccess world, Block block, int x, int y, int z, float colR, float colG, float colB)
//		{
//			float f3 = 0.0F;
//			float f4 = 0.0F;
//			float f5 = 0.0F;
//			float f6 = 0.0F;
//			int l = block.getMixedBrightnessForBlock(world, x, y, z);
//
//			boolean flag2;
//			boolean flag3;
//			boolean flag4;
//			boolean flag5;
//			int i1;
//			float f7;
//
//			LightInfo lightingInfo = new LightInfo(world);
//
//			if(side==0)
//			{
//				//            if (RenderBlocks.getInstance().renderMinY <= 0.0D)
//				//            {
//				//                --y;
//				//            }
//
//				lightingInfo.aoBrightnessXYNN = block.getMixedBrightnessForBlock(world, x - 1, y, z);
//				lightingInfo.aoBrightnessYZNN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
//				lightingInfo.aoBrightnessYZNP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
//				lightingInfo.aoBrightnessXYPN = block.getMixedBrightnessForBlock(world, x + 1, y, z);
//				lightingInfo.aoLightValueScratchXYNN = world.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchYZNN = world.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchYZNP = world.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchXYPN = world.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
//				flag2 = world.getBlock(x + 1, y - 1, z).getCanBlockGrass();
//				flag3 = world.getBlock(x - 1, y - 1, z).getCanBlockGrass();
//				flag4 = world.getBlock(x, y - 1, z + 1).getCanBlockGrass();
//				flag5 = world.getBlock(x, y - 1, z - 1).getCanBlockGrass();
//
//				if (!flag5 && !flag3)
//				{
//					lightingInfo.aoLightValueScratchXYZNNN = lightingInfo.aoLightValueScratchXYNN;
//					lightingInfo.aoBrightnessXYZNNN = lightingInfo.aoBrightnessXYNN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNNN = world.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(world, x - 1, y, z - 1);
//				}
//
//				if (!flag4 && !flag3)
//				{
//					lightingInfo.aoLightValueScratchXYZNNP = lightingInfo.aoLightValueScratchXYNN;
//					lightingInfo.aoBrightnessXYZNNP = lightingInfo.aoBrightnessXYNN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNNP = world.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(world, x - 1, y, z + 1);
//				}
//
//				if (!flag5 && !flag2)
//				{
//					lightingInfo.aoLightValueScratchXYZPNN = lightingInfo.aoLightValueScratchXYPN;
//					lightingInfo.aoBrightnessXYZPNN = lightingInfo.aoBrightnessXYPN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPNN = world.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(world, x + 1, y, z - 1);
//				}
//
//				if (!flag4 && !flag2)
//				{
//					lightingInfo.aoLightValueScratchXYZPNP = lightingInfo.aoLightValueScratchXYPN;
//					lightingInfo.aoBrightnessXYZPNP = lightingInfo.aoBrightnessXYPN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPNP = world.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(world, x + 1, y, z + 1);
//				}
//
//				if (RenderBlocks.getInstance().renderMinY <= 0.0D)
//				{
//					++y;
//				}
//
//				i1 = l;
//
//				if (RenderBlocks.getInstance().renderMinY <= 0.0D || !world.getBlock(x, y - 1, z).isOpaqueCube())
//				{
//					i1 = block.getMixedBrightnessForBlock(world, x, y - 1, z);
//				}
//
//				f7 = world.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
//				f3 = (lightingInfo.aoLightValueScratchXYZNNP + lightingInfo.aoLightValueScratchXYNN + lightingInfo.aoLightValueScratchYZNP + f7) / 4.0F;
//				f6 = (lightingInfo.aoLightValueScratchYZNP + f7 + lightingInfo.aoLightValueScratchXYZPNP + lightingInfo.aoLightValueScratchXYPN) / 4.0F;
//				f5 = (f7 + lightingInfo.aoLightValueScratchYZNN + lightingInfo.aoLightValueScratchXYPN + lightingInfo.aoLightValueScratchXYZPNN) / 4.0F;
//				f4 = (lightingInfo.aoLightValueScratchXYNN + lightingInfo.aoLightValueScratchXYZNNN + f7 + lightingInfo.aoLightValueScratchYZNN) / 4.0F;
//				lightingInfo.brightnessTopLeft = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXYZNNP, lightingInfo.aoBrightnessXYNN, lightingInfo.aoBrightnessYZNP, i1);
//				lightingInfo.brightnessTopRight = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessYZNP, lightingInfo.aoBrightnessXYZPNP, lightingInfo.aoBrightnessXYPN, i1);
//				lightingInfo.brightnessBottomRight = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessYZNN, lightingInfo.aoBrightnessXYPN, lightingInfo.aoBrightnessXYZPNN, i1);
//				lightingInfo.brightnessBottomLeft = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXYNN, lightingInfo.aoBrightnessXYZNNN, lightingInfo.aoBrightnessYZNN, i1);
//
//				lightingInfo.colorRedTopLeft = lightingInfo.colorRedBottomLeft = lightingInfo.colorRedBottomRight = lightingInfo.colorRedTopRight = 0.5F;
//				lightingInfo.colorGreenTopLeft = lightingInfo.colorGreenBottomLeft = lightingInfo.colorGreenBottomRight = lightingInfo.colorGreenTopRight = 0.5F;
//				lightingInfo.colorBlueTopLeft = lightingInfo.colorBlueBottomLeft = lightingInfo.colorBlueBottomRight = lightingInfo.colorBlueTopRight = 0.5F;
//
//				lightingInfo.colorRedTopLeft *= f3;
//				lightingInfo.colorGreenTopLeft *= f3;
//				lightingInfo.colorBlueTopLeft *= f3;
//				lightingInfo.colorRedBottomLeft *= f4;
//				lightingInfo.colorGreenBottomLeft *= f4;
//				lightingInfo.colorBlueBottomLeft *= f4;
//				lightingInfo.colorRedBottomRight *= f5;
//				lightingInfo.colorGreenBottomRight *= f5;
//				lightingInfo.colorBlueBottomRight *= f5;
//				lightingInfo.colorRedTopRight *= f6;
//				lightingInfo.colorGreenTopRight *= f6;
//				lightingInfo.colorBlueTopRight *= f6;
//			}
//
//			if(side==1)
//				//			if(lightingInfo.renderAllFaces || block.shouldSideBeRendered(world, x, y + 1, z, 1))
//			{
//				//			if (RenderBlocks.getInstance().renderMaxY >= 1.0D)
//				//			{
//				//				++y;
//				//			}
//
//				lightingInfo.aoBrightnessXYNP = block.getMixedBrightnessForBlock(world, x - 1, y, z);
//				lightingInfo.aoBrightnessXYPP = block.getMixedBrightnessForBlock(world, x + 1, y, z);
//				lightingInfo.aoBrightnessYZPN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
//				lightingInfo.aoBrightnessYZPP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
//				lightingInfo.aoLightValueScratchXYNP = world.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchXYPP = world.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchYZPN = world.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchYZPP = world.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
//				flag2 = world.getBlock(x + 1, y + 1, z).getCanBlockGrass();
//				flag3 = world.getBlock(x - 1, y + 1, z).getCanBlockGrass();
//				flag4 = world.getBlock(x, y + 1, z + 1).getCanBlockGrass();
//				flag5 = world.getBlock(x, y + 1, z - 1).getCanBlockGrass();
//
//				if (!flag5 && !flag3)
//				{
//					lightingInfo.aoLightValueScratchXYZNPN = lightingInfo.aoLightValueScratchXYNP;
//					lightingInfo.aoBrightnessXYZNPN = lightingInfo.aoBrightnessXYNP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNPN = world.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(world, x - 1, y, z - 1);
//				}
//
//				if (!flag5 && !flag2)
//				{
//					lightingInfo.aoLightValueScratchXYZPPN = lightingInfo.aoLightValueScratchXYPP;
//					lightingInfo.aoBrightnessXYZPPN = lightingInfo.aoBrightnessXYPP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPPN = world.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(world, x + 1, y, z - 1);
//				}
//
//				if (!flag4 && !flag3)
//				{
//					lightingInfo.aoLightValueScratchXYZNPP = lightingInfo.aoLightValueScratchXYNP;
//					lightingInfo.aoBrightnessXYZNPP = lightingInfo.aoBrightnessXYNP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNPP = world.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(world, x - 1, y, z + 1);
//				}
//
//				if (!flag4 && !flag2)
//				{
//					lightingInfo.aoLightValueScratchXYZPPP = lightingInfo.aoLightValueScratchXYPP;
//					lightingInfo.aoBrightnessXYZPPP = lightingInfo.aoBrightnessXYPP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPPP = world.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(world, x + 1, y, z + 1);
//				}
//
//				if (RenderBlocks.getInstance().renderMaxY >= 1.0D)
//				{
//					--y;
//				}
//
//				i1 = l;
//
//				if (RenderBlocks.getInstance().renderMaxY >= 1.0D || !world.getBlock(x, y + 1, z).isOpaqueCube())
//				{
//					i1 = block.getMixedBrightnessForBlock(world, x, y + 1, z);
//				}
//
//				f7 = world.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
//				f6 = (lightingInfo.aoLightValueScratchXYZNPP + lightingInfo.aoLightValueScratchXYNP + lightingInfo.aoLightValueScratchYZPP + f7) / 4.0F;
//				f3 = (lightingInfo.aoLightValueScratchYZPP + f7 + lightingInfo.aoLightValueScratchXYZPPP + lightingInfo.aoLightValueScratchXYPP) / 4.0F;
//				f4 = (f7 + lightingInfo.aoLightValueScratchYZPN + lightingInfo.aoLightValueScratchXYPP + lightingInfo.aoLightValueScratchXYZPPN) / 4.0F;
//				f5 = (lightingInfo.aoLightValueScratchXYNP + lightingInfo.aoLightValueScratchXYZNPN + f7 + lightingInfo.aoLightValueScratchYZPN) / 4.0F;
//				lightingInfo.brightnessTopRight = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXYZNPP, lightingInfo.aoBrightnessXYNP, lightingInfo.aoBrightnessYZPP, i1);
//				lightingInfo.brightnessTopLeft = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessYZPP, lightingInfo.aoBrightnessXYZPPP, lightingInfo.aoBrightnessXYPP, i1);
//				lightingInfo.brightnessBottomLeft = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessYZPN, lightingInfo.aoBrightnessXYPP, lightingInfo.aoBrightnessXYZPPN, i1);
//				lightingInfo.brightnessBottomRight = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXYNP, lightingInfo.aoBrightnessXYZNPN, lightingInfo.aoBrightnessYZPN, i1);
//				lightingInfo.colorRedTopLeft = lightingInfo.colorRedBottomLeft = lightingInfo.colorRedBottomRight = lightingInfo.colorRedTopRight = colR;
//				lightingInfo.colorGreenTopLeft = lightingInfo.colorGreenBottomLeft = lightingInfo.colorGreenBottomRight = lightingInfo.colorGreenTopRight = colG;
//				lightingInfo.colorBlueTopLeft = lightingInfo.colorBlueBottomLeft = lightingInfo.colorBlueBottomRight = lightingInfo.colorBlueTopRight = colB;
//				lightingInfo.colorRedTopLeft *= f3;
//				lightingInfo.colorGreenTopLeft *= f3;
//				lightingInfo.colorBlueTopLeft *= f3;
//				lightingInfo.colorRedBottomLeft *= f4;
//				lightingInfo.colorGreenBottomLeft *= f4;
//				lightingInfo.colorBlueBottomLeft *= f4;
//				lightingInfo.colorRedBottomRight *= f5;
//				lightingInfo.colorGreenBottomRight *= f5;
//				lightingInfo.colorBlueBottomRight *= f5;
//				lightingInfo.colorRedTopRight *= f6;
//				lightingInfo.colorGreenTopRight *= f6;
//				lightingInfo.colorBlueTopRight *= f6;
//			}
//
//			float f8;
//			float f9;
//			float f10;
//			float f11;
//			int j1;
//			int k1;
//			int l1;
//			int i2;
//
//			if(side==2)
//				//			if (lightingInfo.renderAllFaces || block.shouldSideBeRendered(world, x, y, z - 1, 2))
//			{
//				//			if (RenderBlocks.getInstance().renderMinZ <= 0.0D)
//				//			{
//				//				--z;
//				//			}
//
//				lightingInfo.aoLightValueScratchXZNN = world.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchYZNN = world.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchYZPN = world.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchXZPN = world.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoBrightnessXZNN = block.getMixedBrightnessForBlock(world, x - 1, y, z);
//				lightingInfo.aoBrightnessYZNN = block.getMixedBrightnessForBlock(world, x, y - 1, z);
//				lightingInfo.aoBrightnessYZPN = block.getMixedBrightnessForBlock(world, x, y + 1, z);
//				lightingInfo.aoBrightnessXZPN = block.getMixedBrightnessForBlock(world, x + 1, y, z);
//				flag2 = world.getBlock(x + 1, y, z - 1).getCanBlockGrass();
//				flag3 = world.getBlock(x - 1, y, z - 1).getCanBlockGrass();
//				flag4 = world.getBlock(x, y + 1, z - 1).getCanBlockGrass();
//				flag5 = world.getBlock(x, y - 1, z - 1).getCanBlockGrass();
//
//				if (!flag3 && !flag5)
//				{
//					lightingInfo.aoLightValueScratchXYZNNN = lightingInfo.aoLightValueScratchXZNN;
//					lightingInfo.aoBrightnessXYZNNN = lightingInfo.aoBrightnessXZNN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNNN = world.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(world, x - 1, y - 1, z);
//				}
//
//				if (!flag3 && !flag4)
//				{
//					lightingInfo.aoLightValueScratchXYZNPN = lightingInfo.aoLightValueScratchXZNN;
//					lightingInfo.aoBrightnessXYZNPN = lightingInfo.aoBrightnessXZNN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNPN = world.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(world, x - 1, y + 1, z);
//				}
//
//				if (!flag2 && !flag5)
//				{
//					lightingInfo.aoLightValueScratchXYZPNN = lightingInfo.aoLightValueScratchXZPN;
//					lightingInfo.aoBrightnessXYZPNN = lightingInfo.aoBrightnessXZPN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPNN = world.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(world, x + 1, y - 1, z);
//				}
//
//				if (!flag2 && !flag4)
//				{
//					lightingInfo.aoLightValueScratchXYZPPN = lightingInfo.aoLightValueScratchXZPN;
//					lightingInfo.aoBrightnessXYZPPN = lightingInfo.aoBrightnessXZPN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPPN = world.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(world, x + 1, y + 1, z);
//				}
//
//				if (RenderBlocks.getInstance().renderMinZ <= 0.0D)
//				{
//					++z;
//				}
//
//				i1 = l;
//
//				if (RenderBlocks.getInstance().renderMinZ <= 0.0D || !world.getBlock(x, y, z - 1).isOpaqueCube())
//				{
//					i1 = block.getMixedBrightnessForBlock(world, x, y, z - 1);
//				}
//
//				f7 = world.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
//				f8 = (lightingInfo.aoLightValueScratchXZNN + lightingInfo.aoLightValueScratchXYZNPN + f7 + lightingInfo.aoLightValueScratchYZPN) / 4.0F;
//				f9 = (f7 + lightingInfo.aoLightValueScratchYZPN + lightingInfo.aoLightValueScratchXZPN + lightingInfo.aoLightValueScratchXYZPPN) / 4.0F;
//				f10 = (lightingInfo.aoLightValueScratchYZNN + f7 + lightingInfo.aoLightValueScratchXYZPNN + lightingInfo.aoLightValueScratchXZPN) / 4.0F;
//				f11 = (lightingInfo.aoLightValueScratchXYZNNN + lightingInfo.aoLightValueScratchXZNN + lightingInfo.aoLightValueScratchYZNN + f7) / 4.0F;
//				f3 = (float)((double)f8 * RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMinX) + (double)f9 * RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMinX + (double)f10 * (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMinX + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMinX));
//				f4 = (float)((double)f8 * RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMaxX) + (double)f9 * RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMaxX + (double)f10 * (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMaxX + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMaxX));
//				f5 = (float)((double)f8 * RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMaxX) + (double)f9 * RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMaxX + (double)f10 * (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMaxX + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMaxX));
//				f6 = (float)((double)f8 * RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMinX) + (double)f9 * RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMinX + (double)f10 * (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMinX + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMinX));
//				j1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXZNN, lightingInfo.aoBrightnessXYZNPN, lightingInfo.aoBrightnessYZPN, i1);
//				k1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessYZPN, lightingInfo.aoBrightnessXZPN, lightingInfo.aoBrightnessXYZPPN, i1);
//				l1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessYZNN, lightingInfo.aoBrightnessXYZPNN, lightingInfo.aoBrightnessXZPN, i1);
//				i2 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXYZNNN, lightingInfo.aoBrightnessXZNN, lightingInfo.aoBrightnessYZNN, i1);
//				lightingInfo.brightnessTopLeft = lightingInfo.mixAoBrightness(j1, k1, l1, i2, RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMinX), RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMinX, (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMinX, (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMinX));
//				lightingInfo.brightnessBottomLeft = lightingInfo.mixAoBrightness(j1, k1, l1, i2, RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMaxX), RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMaxX, (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMaxX, (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMaxX));
//				lightingInfo.brightnessBottomRight = lightingInfo.mixAoBrightness(j1, k1, l1, i2, RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMaxX), RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMaxX, (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMaxX, (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMaxX));
//				lightingInfo.brightnessTopRight = lightingInfo.mixAoBrightness(j1, k1, l1, i2, RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMinX), RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMinX, (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMinX, (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMinX));
//
//				lightingInfo.colorRedTopLeft = lightingInfo.colorRedBottomLeft = lightingInfo.colorRedBottomRight = lightingInfo.colorRedTopRight = 0.8F;
//				lightingInfo.colorGreenTopLeft = lightingInfo.colorGreenBottomLeft = lightingInfo.colorGreenBottomRight = lightingInfo.colorGreenTopRight = 0.8F;
//				lightingInfo.colorBlueTopLeft = lightingInfo.colorBlueBottomLeft = lightingInfo.colorBlueBottomRight = lightingInfo.colorBlueTopRight = 0.8F;
//
//				lightingInfo.colorRedTopLeft *= f3;
//				lightingInfo.colorGreenTopLeft *= f3;
//				lightingInfo.colorBlueTopLeft *= f3;
//				lightingInfo.colorRedBottomLeft *= f4;
//				lightingInfo.colorGreenBottomLeft *= f4;
//				lightingInfo.colorBlueBottomLeft *= f4;
//				lightingInfo.colorRedBottomRight *= f5;
//				lightingInfo.colorGreenBottomRight *= f5;
//				lightingInfo.colorBlueBottomRight *= f5;
//				lightingInfo.colorRedTopRight *= f6;
//				lightingInfo.colorGreenTopRight *= f6;
//				lightingInfo.colorBlueTopRight *= f6;
//			}
//
//			if(side==3)
//				//		if (lightingInfo.renderAllFaces || block.shouldSideBeRendered(world, x, y, z + 1, 3))
//			{
//				if (RenderBlocks.getInstance().renderMaxZ >= 1.0D)
//				{
//					++z;
//				}
//
//				lightingInfo.aoLightValueScratchXZNP = world.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchXZPP = world.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchYZNP = world.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchYZPP = world.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoBrightnessXZNP = block.getMixedBrightnessForBlock(world, x - 1, y, z);
//				lightingInfo.aoBrightnessXZPP = block.getMixedBrightnessForBlock(world, x + 1, y, z);
//				lightingInfo.aoBrightnessYZNP = block.getMixedBrightnessForBlock(world, x, y - 1, z);
//				lightingInfo.aoBrightnessYZPP = block.getMixedBrightnessForBlock(world, x, y + 1, z);
//				flag2 = world.getBlock(x + 1, y, z + 1).getCanBlockGrass();
//				flag3 = world.getBlock(x - 1, y, z + 1).getCanBlockGrass();
//				flag4 = world.getBlock(x, y + 1, z + 1).getCanBlockGrass();
//				flag5 = world.getBlock(x, y - 1, z + 1).getCanBlockGrass();
//
//				if (!flag3 && !flag5)
//				{
//					lightingInfo.aoLightValueScratchXYZNNP = lightingInfo.aoLightValueScratchXZNP;
//					lightingInfo.aoBrightnessXYZNNP = lightingInfo.aoBrightnessXZNP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNNP = world.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(world, x - 1, y - 1, z);
//				}
//
//				if (!flag3 && !flag4)
//				{
//					lightingInfo.aoLightValueScratchXYZNPP = lightingInfo.aoLightValueScratchXZNP;
//					lightingInfo.aoBrightnessXYZNPP = lightingInfo.aoBrightnessXZNP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNPP = world.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(world, x - 1, y + 1, z);
//				}
//
//				if (!flag2 && !flag5)
//				{
//					lightingInfo.aoLightValueScratchXYZPNP = lightingInfo.aoLightValueScratchXZPP;
//					lightingInfo.aoBrightnessXYZPNP = lightingInfo.aoBrightnessXZPP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPNP = world.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(world, x + 1, y - 1, z);
//				}
//
//				if (!flag2 && !flag4)
//				{
//					lightingInfo.aoLightValueScratchXYZPPP = lightingInfo.aoLightValueScratchXZPP;
//					lightingInfo.aoBrightnessXYZPPP = lightingInfo.aoBrightnessXZPP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPPP = world.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(world, x + 1, y + 1, z);
//				}
//
//				if (RenderBlocks.getInstance().renderMaxZ >= 1.0D)
//				{
//					--z;
//				}
//
//				i1 = l;
//
//				if (RenderBlocks.getInstance().renderMaxZ >= 1.0D || !world.getBlock(x, y, z + 1).isOpaqueCube())
//				{
//					i1 = block.getMixedBrightnessForBlock(world, x, y, z + 1);
//				}
//
//				f7 = world.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
//				f8 = (lightingInfo.aoLightValueScratchXZNP + lightingInfo.aoLightValueScratchXYZNPP + f7 + lightingInfo.aoLightValueScratchYZPP) / 4.0F;
//				f9 = (f7 + lightingInfo.aoLightValueScratchYZPP + lightingInfo.aoLightValueScratchXZPP + lightingInfo.aoLightValueScratchXYZPPP) / 4.0F;
//				f10 = (lightingInfo.aoLightValueScratchYZNP + f7 + lightingInfo.aoLightValueScratchXYZPNP + lightingInfo.aoLightValueScratchXZPP) / 4.0F;
//				f11 = (lightingInfo.aoLightValueScratchXYZNNP + lightingInfo.aoLightValueScratchXZNP + lightingInfo.aoLightValueScratchYZNP + f7) / 4.0F;
//				f3 = (float)((double)f8 * RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMinX) + (double)f9 * RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMinX + (double)f10 * (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMinX + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMinX));
//				f4 = (float)((double)f8 * RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMinX) + (double)f9 * RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMinX + (double)f10 * (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMinX + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMinX));
//				f5 = (float)((double)f8 * RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMaxX) + (double)f9 * RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMaxX + (double)f10 * (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMaxX + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMaxX));
//				f6 = (float)((double)f8 * RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMaxX) + (double)f9 * RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMaxX + (double)f10 * (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMaxX + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMaxX));
//				j1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXZNP, lightingInfo.aoBrightnessXYZNPP, lightingInfo.aoBrightnessYZPP, i1);
//				k1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessYZPP, lightingInfo.aoBrightnessXZPP, lightingInfo.aoBrightnessXYZPPP, i1);
//				l1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessYZNP, lightingInfo.aoBrightnessXYZPNP, lightingInfo.aoBrightnessXZPP, i1);
//				i2 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXYZNNP, lightingInfo.aoBrightnessXZNP, lightingInfo.aoBrightnessYZNP, i1);
//				lightingInfo.brightnessTopLeft = lightingInfo.mixAoBrightness(j1, i2, l1, k1, RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMinX), (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMinX), (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMinX, RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMinX);
//				lightingInfo.brightnessBottomLeft = lightingInfo.mixAoBrightness(j1, i2, l1, k1, RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMinX), (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMinX), (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMinX, RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMinX);
//				lightingInfo.brightnessBottomRight = lightingInfo.mixAoBrightness(j1, i2, l1, k1, RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMaxX), (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMaxX), (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMaxX, RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMaxX);
//				lightingInfo.brightnessTopRight = lightingInfo.mixAoBrightness(j1, i2, l1, k1, RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMaxX), (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMaxX), (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMaxX, RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMaxX);
//
//				lightingInfo.colorRedTopLeft = lightingInfo.colorRedBottomLeft = lightingInfo.colorRedBottomRight = lightingInfo.colorRedTopRight = 0.8F;
//				lightingInfo.colorGreenTopLeft = lightingInfo.colorGreenBottomLeft = lightingInfo.colorGreenBottomRight = lightingInfo.colorGreenTopRight = 0.8F;
//				lightingInfo.colorBlueTopLeft = lightingInfo.colorBlueBottomLeft = lightingInfo.colorBlueBottomRight = lightingInfo.colorBlueTopRight = 0.8F;
//
//				lightingInfo.colorRedTopLeft *= f3;
//				lightingInfo.colorGreenTopLeft *= f3;
//				lightingInfo.colorBlueTopLeft *= f3;
//				lightingInfo.colorRedBottomLeft *= f4;
//				lightingInfo.colorGreenBottomLeft *= f4;
//				lightingInfo.colorBlueBottomLeft *= f4;
//				lightingInfo.colorRedBottomRight *= f5;
//				lightingInfo.colorGreenBottomRight *= f5;
//				lightingInfo.colorBlueBottomRight *= f5;
//				lightingInfo.colorRedTopRight *= f6;
//				lightingInfo.colorGreenTopRight *= f6;
//				lightingInfo.colorBlueTopRight *= f6;
//			}
//
//			if(side==4)
//				//		if (lightingInfo.renderAllFaces || block.shouldSideBeRendered(world, x - 1, y, z, 4))
//			{
//				if (RenderBlocks.getInstance().renderMinX <= 0.0D)
//				{
//					--x;
//				}
//
//				lightingInfo.aoLightValueScratchXYNN = world.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchXZNN = world.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchXZNP = world.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchXYNP = world.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoBrightnessXYNN = block.getMixedBrightnessForBlock(world, x, y - 1, z);
//				lightingInfo.aoBrightnessXZNN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
//				lightingInfo.aoBrightnessXZNP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
//				lightingInfo.aoBrightnessXYNP = block.getMixedBrightnessForBlock(world, x, y + 1, z);
//				flag2 = world.getBlock(x - 1, y + 1, z).getCanBlockGrass();
//				flag3 = world.getBlock(x - 1, y - 1, z).getCanBlockGrass();
//				flag4 = world.getBlock(x - 1, y, z - 1).getCanBlockGrass();
//				flag5 = world.getBlock(x - 1, y, z + 1).getCanBlockGrass();
//
//				if (!flag4 && !flag3)
//				{
//					lightingInfo.aoLightValueScratchXYZNNN = lightingInfo.aoLightValueScratchXZNN;
//					lightingInfo.aoBrightnessXYZNNN = lightingInfo.aoBrightnessXZNN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNNN = world.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(world, x, y - 1, z - 1);
//				}
//
//				if (!flag5 && !flag3)
//				{
//					lightingInfo.aoLightValueScratchXYZNNP = lightingInfo.aoLightValueScratchXZNP;
//					lightingInfo.aoBrightnessXYZNNP = lightingInfo.aoBrightnessXZNP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNNP = world.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(world, x, y - 1, z + 1);
//				}
//
//				if (!flag4 && !flag2)
//				{
//					lightingInfo.aoLightValueScratchXYZNPN = lightingInfo.aoLightValueScratchXZNN;
//					lightingInfo.aoBrightnessXYZNPN = lightingInfo.aoBrightnessXZNN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNPN = world.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(world, x, y + 1, z - 1);
//				}
//
//				if (!flag5 && !flag2)
//				{
//					lightingInfo.aoLightValueScratchXYZNPP = lightingInfo.aoLightValueScratchXZNP;
//					lightingInfo.aoBrightnessXYZNPP = lightingInfo.aoBrightnessXZNP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZNPP = world.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(world, x, y + 1, z + 1);
//				}
//
//				if (RenderBlocks.getInstance().renderMinX <= 0.0D)
//				{
//					++x;
//				}
//
//				i1 = l;
//
//				if (RenderBlocks.getInstance().renderMinX <= 0.0D || !world.getBlock(x - 1, y, z).isOpaqueCube())
//				{
//					i1 = block.getMixedBrightnessForBlock(world, x - 1, y, z);
//				}
//
//				f7 = world.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
//				f8 = (lightingInfo.aoLightValueScratchXYNN + lightingInfo.aoLightValueScratchXYZNNP + f7 + lightingInfo.aoLightValueScratchXZNP) / 4.0F;
//				f9 = (f7 + lightingInfo.aoLightValueScratchXZNP + lightingInfo.aoLightValueScratchXYNP + lightingInfo.aoLightValueScratchXYZNPP) / 4.0F;
//				f10 = (lightingInfo.aoLightValueScratchXZNN + f7 + lightingInfo.aoLightValueScratchXYZNPN + lightingInfo.aoLightValueScratchXYNP) / 4.0F;
//				f11 = (lightingInfo.aoLightValueScratchXYZNNN + lightingInfo.aoLightValueScratchXYNN + lightingInfo.aoLightValueScratchXZNN + f7) / 4.0F;
//				f3 = (float)((double)f9 * RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMaxZ + (double)f10 * RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMaxZ) + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMaxZ) + (double)f8 * (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMaxZ);
//				f4 = (float)((double)f9 * RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMinZ + (double)f10 * RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMinZ) + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMinZ) + (double)f8 * (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMinZ);
//				f5 = (float)((double)f9 * RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMinZ + (double)f10 * RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMinZ) + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMinZ) + (double)f8 * (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMinZ);
//				f6 = (float)((double)f9 * RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMaxZ + (double)f10 * RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMaxZ) + (double)f11 * (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMaxZ) + (double)f8 * (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMaxZ);
//				j1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXYNN, lightingInfo.aoBrightnessXYZNNP, lightingInfo.aoBrightnessXZNP, i1);
//				k1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXZNP, lightingInfo.aoBrightnessXYNP, lightingInfo.aoBrightnessXYZNPP, i1);
//				l1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXZNN, lightingInfo.aoBrightnessXYZNPN, lightingInfo.aoBrightnessXYNP, i1);
//				i2 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXYZNNN, lightingInfo.aoBrightnessXYNN, lightingInfo.aoBrightnessXZNN, i1);
//				lightingInfo.brightnessTopLeft = lightingInfo.mixAoBrightness(k1, l1, i2, j1, RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMaxZ, RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMaxZ), (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMaxZ), (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMaxZ);
//				lightingInfo.brightnessBottomLeft = lightingInfo.mixAoBrightness(k1, l1, i2, j1, RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMinZ, RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMinZ), (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMinZ), (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMinZ);
//				lightingInfo.brightnessBottomRight = lightingInfo.mixAoBrightness(k1, l1, i2, j1, RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMinZ, RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMinZ), (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMinZ), (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMinZ);
//				lightingInfo.brightnessTopRight = lightingInfo.mixAoBrightness(k1, l1, i2, j1, RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMaxZ, RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMaxZ), (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMaxZ), (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMaxZ);
//
//				lightingInfo.colorRedTopLeft = lightingInfo.colorRedBottomLeft = lightingInfo.colorRedBottomRight = lightingInfo.colorRedTopRight = 0.6F;
//				lightingInfo.colorGreenTopLeft = lightingInfo.colorGreenBottomLeft = lightingInfo.colorGreenBottomRight = lightingInfo.colorGreenTopRight = 0.6F;
//				lightingInfo.colorBlueTopLeft = lightingInfo.colorBlueBottomLeft = lightingInfo.colorBlueBottomRight = lightingInfo.colorBlueTopRight = 0.6F;
//
//				lightingInfo.colorRedTopLeft *= f3;
//				lightingInfo.colorGreenTopLeft *= f3;
//				lightingInfo.colorBlueTopLeft *= f3;
//				lightingInfo.colorRedBottomLeft *= f4;
//				lightingInfo.colorGreenBottomLeft *= f4;
//				lightingInfo.colorBlueBottomLeft *= f4;
//				lightingInfo.colorRedBottomRight *= f5;
//				lightingInfo.colorGreenBottomRight *= f5;
//				lightingInfo.colorBlueBottomRight *= f5;
//				lightingInfo.colorRedTopRight *= f6;
//				lightingInfo.colorGreenTopRight *= f6;
//				lightingInfo.colorBlueTopRight *= f6;
//			}
//
//			if(side==5)
//				//		if (lightingInfo.renderAllFaces || block.shouldSideBeRendered(world, x + 1, y, z, 5))
//			{
//				if (RenderBlocks.getInstance().renderMaxX >= 1.0D)
//				{
//					++x;
//				}
//
//				lightingInfo.aoLightValueScratchXYPN = world.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchXZPN = world.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchXZPP = world.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
//				lightingInfo.aoLightValueScratchXYPP = world.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
//				lightingInfo.aoBrightnessXYPN = block.getMixedBrightnessForBlock(world, x, y - 1, z);
//				lightingInfo.aoBrightnessXZPN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
//				lightingInfo.aoBrightnessXZPP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
//				lightingInfo.aoBrightnessXYPP = block.getMixedBrightnessForBlock(world, x, y + 1, z);
//				flag2 = world.getBlock(x + 1, y + 1, z).getCanBlockGrass();
//				flag3 = world.getBlock(x + 1, y - 1, z).getCanBlockGrass();
//				flag4 = world.getBlock(x + 1, y, z + 1).getCanBlockGrass();
//				flag5 = world.getBlock(x + 1, y, z - 1).getCanBlockGrass();
//
//				if (!flag3 && !flag5)
//				{
//					lightingInfo.aoLightValueScratchXYZPNN = lightingInfo.aoLightValueScratchXZPN;
//					lightingInfo.aoBrightnessXYZPNN = lightingInfo.aoBrightnessXZPN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPNN = world.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(world, x, y - 1, z - 1);
//				}
//
//				if (!flag3 && !flag4)
//				{
//					lightingInfo.aoLightValueScratchXYZPNP = lightingInfo.aoLightValueScratchXZPP;
//					lightingInfo.aoBrightnessXYZPNP = lightingInfo.aoBrightnessXZPP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPNP = world.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(world, x, y - 1, z + 1);
//				}
//
//				if (!flag2 && !flag5)
//				{
//					lightingInfo.aoLightValueScratchXYZPPN = lightingInfo.aoLightValueScratchXZPN;
//					lightingInfo.aoBrightnessXYZPPN = lightingInfo.aoBrightnessXZPN;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPPN = world.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(world, x, y + 1, z - 1);
//				}
//
//				if (!flag2 && !flag4)
//				{
//					lightingInfo.aoLightValueScratchXYZPPP = lightingInfo.aoLightValueScratchXZPP;
//					lightingInfo.aoBrightnessXYZPPP = lightingInfo.aoBrightnessXZPP;
//				}
//				else
//				{
//					lightingInfo.aoLightValueScratchXYZPPP = world.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
//					lightingInfo.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(world, x, y + 1, z + 1);
//				}
//
//				if (RenderBlocks.getInstance().renderMaxX >= 1.0D)
//				{
//					--x;
//				}
//
//				i1 = l;
//
//				if (RenderBlocks.getInstance().renderMaxX >= 1.0D || !world.getBlock(x + 1, y, z).isOpaqueCube())
//				{
//					i1 = block.getMixedBrightnessForBlock(world, x + 1, y, z);
//				}
//
//				f7 = world.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
//				f8 = (lightingInfo.aoLightValueScratchXYPN + lightingInfo.aoLightValueScratchXYZPNP + f7 + lightingInfo.aoLightValueScratchXZPP) / 4.0F;
//				f9 = (lightingInfo.aoLightValueScratchXYZPNN + lightingInfo.aoLightValueScratchXYPN + lightingInfo.aoLightValueScratchXZPN + f7) / 4.0F;
//				f10 = (lightingInfo.aoLightValueScratchXZPN + f7 + lightingInfo.aoLightValueScratchXYZPPN + lightingInfo.aoLightValueScratchXYPP) / 4.0F;
//				f11 = (f7 + lightingInfo.aoLightValueScratchXZPP + lightingInfo.aoLightValueScratchXYPP + lightingInfo.aoLightValueScratchXYZPPP) / 4.0F;
//				f3 = (float)((double)f8 * (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMaxZ + (double)f9 * (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMaxZ) + (double)f10 * RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMaxZ) + (double)f11 * RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMaxZ);
//				f4 = (float)((double)f8 * (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMinZ + (double)f9 * (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMinZ) + (double)f10 * RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMinZ) + (double)f11 * RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMinZ);
//				f5 = (float)((double)f8 * (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMinZ + (double)f9 * (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMinZ) + (double)f10 * RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMinZ) + (double)f11 * RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMinZ);
//				f6 = (float)((double)f8 * (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMaxZ + (double)f9 * (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMaxZ) + (double)f10 * RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMaxZ) + (double)f11 * RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMaxZ);
//				j1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXYPN, lightingInfo.aoBrightnessXYZPNP, lightingInfo.aoBrightnessXZPP, i1);
//				k1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXZPP, lightingInfo.aoBrightnessXYPP, lightingInfo.aoBrightnessXYZPPP, i1);
//				l1 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXZPN, lightingInfo.aoBrightnessXYZPPN, lightingInfo.aoBrightnessXYPP, i1);
//				i2 = lightingInfo.getAoBrightness(lightingInfo.aoBrightnessXYZPNN, lightingInfo.aoBrightnessXYPN, lightingInfo.aoBrightnessXZPN, i1);
//				lightingInfo.brightnessTopLeft = lightingInfo.mixAoBrightness(j1, i2, l1, k1, (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMaxZ, (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMaxZ), RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMaxZ), RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMaxZ);
//				lightingInfo.brightnessBottomLeft = lightingInfo.mixAoBrightness(j1, i2, l1, k1, (1.0D - RenderBlocks.getInstance().renderMinY) * RenderBlocks.getInstance().renderMinZ, (1.0D - RenderBlocks.getInstance().renderMinY) * (1.0D - RenderBlocks.getInstance().renderMinZ), RenderBlocks.getInstance().renderMinY * (1.0D - RenderBlocks.getInstance().renderMinZ), RenderBlocks.getInstance().renderMinY * RenderBlocks.getInstance().renderMinZ);
//				lightingInfo.brightnessBottomRight = lightingInfo.mixAoBrightness(j1, i2, l1, k1, (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMinZ, (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMinZ), RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMinZ), RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMinZ);
//				lightingInfo.brightnessTopRight = lightingInfo.mixAoBrightness(j1, i2, l1, k1, (1.0D - RenderBlocks.getInstance().renderMaxY) * RenderBlocks.getInstance().renderMaxZ, (1.0D - RenderBlocks.getInstance().renderMaxY) * (1.0D - RenderBlocks.getInstance().renderMaxZ), RenderBlocks.getInstance().renderMaxY * (1.0D - RenderBlocks.getInstance().renderMaxZ), RenderBlocks.getInstance().renderMaxY * RenderBlocks.getInstance().renderMaxZ);
//
//				lightingInfo.colorRedTopLeft = lightingInfo.colorRedBottomLeft = lightingInfo.colorRedBottomRight = lightingInfo.colorRedTopRight = 0.6F;
//				lightingInfo.colorGreenTopLeft = lightingInfo.colorGreenBottomLeft = lightingInfo.colorGreenBottomRight = lightingInfo.colorGreenTopRight = 0.6F;
//				lightingInfo.colorBlueTopLeft = lightingInfo.colorBlueBottomLeft = lightingInfo.colorBlueBottomRight = lightingInfo.colorBlueTopRight = 0.6F;
//
//				lightingInfo.colorRedTopLeft *= f3;
//				lightingInfo.colorGreenTopLeft *= f3;
//				lightingInfo.colorBlueTopLeft *= f3;
//				lightingInfo.colorRedBottomLeft *= f4;
//				lightingInfo.colorGreenBottomLeft *= f4;
//				lightingInfo.colorBlueBottomLeft *= f4;
//				lightingInfo.colorRedBottomRight *= f5;
//				lightingInfo.colorGreenBottomRight *= f5;
//				lightingInfo.colorBlueBottomRight *= f5;
//				lightingInfo.colorRedTopRight *= f6;
//				lightingInfo.colorGreenTopRight *= f6;
//				lightingInfo.colorBlueTopRight *= f6;
//			}
//			return lightingInfo;
//		}


}
