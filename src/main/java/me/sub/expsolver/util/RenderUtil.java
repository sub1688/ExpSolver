package me.sub.expsolver.util;

import me.sub.expsolver.accessor.IEntityRendererAccessor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

@SuppressWarnings("unused")
public class RenderUtil {

    public static float renderPartialTicks = 1.0f;

    private static final Frustum FRUSTUM = new Frustum();
    private static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer MODEL_VIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer SCREEN_COORDINATES = GLAllocation.createDirectFloatBuffer(4);

    private static final int SPHERE_STACKS = 10;
    private static final int SPHERE_SLICES = 10;


    private static void renderFilledBoundingBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION);

        // Bottom face
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();

        // Top face
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

        // North face
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();

        // South face
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();

        // West face
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

        // East face
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBox(BlockPos pos, int color, boolean depth, float renderPartialTicks) {
        final Minecraft mc = Minecraft.getMinecraft();
        final RenderManager renderManager = mc.getRenderManager();

        final double x = pos.getX() - renderManager.viewerPosX;
        final double y = pos.getY() - renderManager.viewerPosY;
        final double z = pos.getZ() - renderManager.viewerPosZ;

        AxisAlignedBB axisAlignedBB;
        final Block block = mc.theWorld.getBlockState(pos).getBlock();

        if (block != null) {
            final EntityPlayer player = mc.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(mc.theWorld, pos).expand(.002, .002, .002).offset(-posX, -posY, -posZ);

            drawAxisAlignedBB(axisAlignedBB, color, depth);
        }
    }


    public static void drawBox(BlockPos pos, int color, Block block, boolean depth, float renderPartialTicks) {
        final Minecraft mc = Minecraft.getMinecraft();
        final RenderManager renderManager = mc.getRenderManager();

        final double x = pos.getX() - renderManager.viewerPosX;
        final double y = pos.getY() - renderManager.viewerPosY;
        final double z = pos.getZ() - renderManager.viewerPosZ;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0);

        if (block != null) {
            final EntityPlayer player = mc.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBox(mc.theWorld, pos).expand(.002, .002, .002).offset(-posX, -posY, -posZ);

            drawAxisAlignedBB(axisAlignedBB, color, depth);
        }
    }


    public static void drawAxisAlignedBB(AxisAlignedBB axisAlignedBB, int color, boolean depth) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        if (depth) glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(1);


        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;

        GlStateManager.color(f, f1, f2, f3);

        drawSelectionBoundingBox(axisAlignedBB);
        GlStateManager.resetColor();
        glEnable(GL_TEXTURE_2D);
        if (depth) glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public static void drawBox(Entity eman, int color, float partialTicks) {
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
        final double posX1 = (eman.posX - eman.lastTickPosX) * (double) partialTicks;
        final double posY1 = (eman.posY - eman.lastTickPosY) * (double) partialTicks;
        final double posZ1 = (eman.posZ - eman.lastTickPosZ) * (double) partialTicks;
        AxisAlignedBB bb = eman.getEntityBoundingBox().offset(-posX, -posY, -posZ).offset(posX1, posY1, posZ1);
        RenderUtil.drawAxisAlignedBB(bb, color, true);
    }

    public static void drawStringOnEntity(Entity e, String str, float renderPartialTicks) {
        final Minecraft mc = Minecraft.getMinecraft();
        double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * renderPartialTicks - mc.getRenderManager().viewerPosX;
        double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * renderPartialTicks - mc.getRenderManager().viewerPosY;
        double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * renderPartialTicks - mc.getRenderManager().viewerPosZ;


        GL11.glPushMatrix();
        GL11.glTranslated(x, y + 2.5D, z);
        GL11.glScalef(-0.03f, -0.03f, -0.03f);

        GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0d, 1.0d, 0.0d);
        GL11.glRotated(mc.getRenderManager().playerViewX, 1.0d, 0.0d, 0.0d);
        GlStateManager.disableDepth();

        mc.fontRendererObj.drawString(str, -(mc.fontRendererObj.getStringWidth(str) / 2), -mc.fontRendererObj.FONT_HEIGHT / 2, 0xFFFFFFFF);
        GL11.glTranslated(-x, -(y + 2.5D), -z);
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GlStateManager.enableDepth();
        GL11.glPopMatrix();

    }

    public static void drawStringOnPos(BlockPos e, String str, float scale) {
        final Minecraft mc = Minecraft.getMinecraft();
        double x = e.getX() - mc.getRenderManager().viewerPosX;
        double y = e.getY() - mc.getRenderManager().viewerPosY;
        double z = e.getZ() - mc.getRenderManager().viewerPosZ;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y + 2.5D, z);
        GL11.glScalef(-0.03f, -0.03f, -0.03f);
        GL11.glScalef(scale, scale, scale);

        GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0d, 1.0d, 0.0d);
        GL11.glRotated(mc.getRenderManager().playerViewX, 1.0d, 0.0d, 0.0d);
        GlStateManager.disableDepth();

        mc.fontRendererObj.drawStringWithShadow(str, -(mc.fontRendererObj.getStringWidth(str) / 2.0f), -mc.fontRendererObj.FONT_HEIGHT / 2.0f, 0xFFFFFFFF);
        GL11.glTranslated(-x, -(y + 2.5D), -z);
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GlStateManager.enableDepth();
        GL11.glPopMatrix();

    }

    public static void drawLine(int firstColorRGB, int secondColorRGB, BlockPos firstPosition, BlockPos secondPosition) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        drawLine(firstColorRGB, secondColorRGB, firstPosition.getX() - renderManager.viewerPosX, firstPosition.getY() - renderManager.viewerPosY, firstPosition.getZ() - renderManager.viewerPosZ, secondPosition.getX() - renderManager.viewerPosX, secondPosition.getY() - renderManager.viewerPosY, secondPosition.getZ() - renderManager.viewerPosZ);
    }

    public static void drawLine(int color, BlockPos firstPosition, BlockPos secondPosition) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        drawLine(color, firstPosition.getX() - renderManager.viewerPosX, firstPosition.getY() - renderManager.viewerPosY, firstPosition.getZ() - renderManager.viewerPosZ, secondPosition.getX() - renderManager.viewerPosX, secondPosition.getY() - renderManager.viewerPosY, secondPosition.getZ() - renderManager.viewerPosZ);
    }

    public static void drawLine(int firstColorRGB, int secondColorRGB, Vec3 first, Vec3 second) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        drawLine(firstColorRGB, secondColorRGB, first.xCoord - renderManager.viewerPosX, first.yCoord - renderManager.viewerPosY, first.zCoord - renderManager.viewerPosZ, second.xCoord - renderManager.viewerPosX, second.yCoord - renderManager.viewerPosY, second.zCoord - renderManager.viewerPosZ);
    }

    public static void drawLine(int color, Vec3 first, Vec3 second) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        drawLine(color, first.xCoord - renderManager.viewerPosX, first.yCoord - renderManager.viewerPosY, first.zCoord - renderManager.viewerPosZ, second.xCoord - renderManager.viewerPosX, second.yCoord - renderManager.viewerPosY, second.zCoord - renderManager.viewerPosZ);
    }

    public static void drawLine(int color, double x, double y, double z, double playerX, double playerY, double playerZ) {
        drawLine(color, color, x, y, z, playerX, playerY, playerZ);
    }

    public static void drawLine(int firstColorRGB, int secondColorRGB, double x, double y, double z, double playerX, double playerY, double playerZ) {
        Color firstColor = new Color(firstColorRGB);
        Color secondColor = new Color(secondColorRGB);

        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glEnable(GL_LINE_SMOOTH);
        glShadeModel(GL_SMOOTH);
        glLineWidth(1);
        glBegin(GL_LINE_STRIP);

        glColor4f(secondColor.getRed() / 255.F, secondColor.getGreen() / 255.F, secondColor.getBlue() / 255.F, secondColor.getAlpha() / 255.F);

        glVertex3d(playerX, playerY, playerZ);

        glColor4f(firstColor.getRed() / 255.F, firstColor.getGreen() / 255.F, firstColor.getBlue() / 255.F, firstColor.getAlpha() / 255.F);

        glVertex3d(x, y, z);

        glEnd();
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glPopMatrix();
    }


    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }


    public static void drawPoint(int c, double x, double y, double z) {
        Color color = new Color(c);

        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glEnable(GL_POINT_SMOOTH);
        glShadeModel(GL_SMOOTH);
        glPointSize(5);
        glBegin(GL_POINTS);

        glColor4f(color.getRed() / 255.F, color.getGreen() / 255.F, color.getBlue() / 255.F, color.getAlpha() / 255.F);
        glVertex3d(x, y, z);

        glEnd();
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    public static void drawOutline(float left, float top, float right, float bottom, float outwards, int color) {
        drawRect(left - outwards, top - outwards, right + outwards, bottom + outwards, color);
    }

    public static void drawRect(float left, float top, float right, float bottom, int color)
    {
        if (left < right)
        {
            float i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            float j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawTexture(ResourceLocation resourceLocation, float x, float y, float width, float height) {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.alphaFunc(516, 0.003921569F);
        GlStateManager.enableTexture2D();

        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(0, 1f).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex( 1f,  1f).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex(1f, 0).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(0, 0).endVertex();
        tessellator.draw();

        GlStateManager.popMatrix();
    }


    /**
     * Simple method to draw a gradient.
     * @param colors 0 - bottom left, 1 - bottom right, 2 - top right, 3 - top left
     */
    public static void drawGradient(float x, float y, float width, float height, int[] colors) {
        if (colors.length < 3) {
            return;
        }

        GlStateManager.pushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        applyColorToVertex(worldrenderer.pos(x, y + height, 0.0D), colors[0]).endVertex(); // bottom left
        applyColorToVertex(worldrenderer.pos(x + width, y + height, 0.0D), colors[1]).endVertex(); // bottom right
        applyColorToVertex(worldrenderer.pos(x + width, y, 0.0D), colors[2]).endVertex(); // top right
        applyColorToVertex(worldrenderer.pos(x, y, 0.0D), colors[3]).endVertex(); // top left
        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static WorldRenderer applyColorToVertex(WorldRenderer worldRenderer, int color) {
        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;
        float alpha = (float)(color >> 24 & 255) / 255.0F;
        return worldRenderer.color(red, green, blue, alpha);
    }


    public static void drawSelector(float x, float y, float size, int color) {

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;

        GlStateManager.pushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);

        worldrenderer.pos(x + size / 2.0F, y + size / 1.5F, 0.0D).endVertex();
        worldrenderer.pos(x + size, y, 0.0D).endVertex();
        worldrenderer.pos(x, y, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();


        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(1.0F);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        worldrenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, y, 0.0D).endVertex();
        worldrenderer.pos(x + size / 2.0F, y + size / 1.5F, 0.0D).endVertex();
        worldrenderer.pos(x + size, y, 0.0D).endVertex();
        worldrenderer.pos(x, y, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.popMatrix();
    }

    public static void drawSphere(double x, double y, double z, float radius, int color) {
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = ((color >> 24) & 0xFF) / 255.0f;

        GlStateManager.pushMatrix();
        final Minecraft mc = Minecraft.getMinecraft();
        x = x - mc.getRenderManager().viewerPosX;
        y = y - mc.getRenderManager().viewerPosY;
        z = z - mc.getRenderManager().viewerPosZ;
        GlStateManager.translate(x, y, z);

        // Enable necessary GL states
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();

        // Set color
        GlStateManager.color(r, g, b, a);

        for (int stack = 0; stack < SPHERE_STACKS; stack++) {
            float phi1 = (float) Math.PI * ((float) stack / SPHERE_STACKS - 0.5f);
            float phi2 = (float) Math.PI * ((float) (stack + 1) / SPHERE_STACKS - 0.5f);

            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

            for (int slice = 0; slice <= SPHERE_SLICES; slice++) {
                float theta = (float) (2.0f * Math.PI * slice / SPHERE_SLICES);

                // First vertex
                float x1 = (float) (radius * Math.cos(phi1) * Math.cos(theta));
                float y1 = (float) (radius * Math.cos(phi1) * Math.sin(theta));
                float z1 = (float) (radius * Math.sin(phi1));

                // Normal for lighting
                GL11.glNormal3f(x1 / radius, y1 / radius, z1 / radius);
                GL11.glVertex3f(x1, y1, z1);

                // Second vertex
                float x2 = (float) (radius * Math.cos(phi2) * Math.cos(theta));
                float y2 = (float) (radius * Math.cos(phi2) * Math.sin(theta));
                float z2 = (float) (radius * Math.sin(phi2));

                GL11.glNormal3f(x2 / radius, y2 / radius, z2 / radius);
                GL11.glVertex3f(x2, y2, z2);
            }

            GL11.glEnd();
        }

        // Restore GL states
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }

    public static Vector2f worldToScreen(double x, double y, double z) {
        Minecraft mc = Minecraft.getMinecraft();
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int scaleFactor = scaledResolution.getScaleFactor();

        IEntityRendererAccessor IEntityRendererAccessor = (IEntityRendererAccessor) mc.entityRenderer;
        IEntityRendererAccessor.invokeCameraTransform(renderPartialTicks, 0);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, MODEL_VIEW);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, PROJECTION);
        GL11.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT);

        boolean success = GLU.gluProject(
                (float) (x - renderManager.viewerPosX), (float) (y - renderManager.viewerPosY), (float) (z - renderManager.viewerPosZ),
                MODEL_VIEW, PROJECTION, VIEWPORT,
                SCREEN_COORDINATES
        );

        mc.entityRenderer.setupOverlayRendering();

        if (success) {
            return new Vector2f(
                    SCREEN_COORDINATES.get(0) / scaleFactor,
                    (mc.displayHeight - SCREEN_COORDINATES.get(1)) / scaleFactor
            );
        }

        return null;
    }


    public static boolean withinFrustum(BlockPos blockPos) {
        World world = Minecraft.getMinecraft().theWorld;
        return withinFrustum(world.getBlockState(blockPos).getBlock().getSelectedBoundingBox(world, blockPos));
    }


    public static boolean withinFrustum(AxisAlignedBB axisAlignedBB) {
        Minecraft mc = Minecraft.getMinecraft();
        FRUSTUM.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        return FRUSTUM.isBoundingBoxInFrustum(axisAlignedBB);
    }


}


