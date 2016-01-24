package com.velocity.mod.common.items.engine;

import com.velocity.mod.common.entity.car.EntityNissanSkylineR34;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Alex on 1/24/2016.
 */
public class ItemNewNissanEngine extends Item implements iEngine {

    public ItemNewNissanEngine(iEngine iengine) {
        super();

    }

    @Override
    public int topSpeed() {
        return 49;
    }

    @Override
    public int horsePower() {
        return 327;
    }

    @Override
    public int Cylinders() {
        return 6;
    }

    @Override
    public Boolean supercharged() {
        return true;
    }

    @Override
    public Item getGas() {
        return Items.apple;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean p_77624_4_) {
        info.add("Engine Type: 1999 Nissan GT-R R34 Supercharged Engine");
        info.add("§4 *** Specs ***");
        info.add("");
        info.add("§a  v" + Cylinders());
        info.add("§a  HP: " + horsePower());
        info.add("§a  Supercharged: " + supercharged());
        info.add("§9 Top Speed: " + topSpeed());
    }



    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)f + 1.62D - (double)player.yOffset;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        MovingObjectPosition movingobjectposition = world.rayTraceBlocks(vec3, vec31, true);

        if (movingobjectposition == null)
        {
            return stack;
        }
        else
        {
            Vec3 lookVec = player.getLook(f);
            boolean flag = false;
            float f9 = 1.0F;
            List list = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(lookVec.xCoord * d3, lookVec.yCoord * d3, lookVec.zCoord * d3).expand((double)f9, (double)f9, (double)f9));
            int i;

            for (i = 0; i < list.size(); ++i)
            {
                Entity entity = (Entity)list.get(i);

                if (entity.canBeCollidedWith())
                {
                    float f10 = entity.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f10, (double)f10, (double)f10);

                    if (axisalignedbb.isVecInside(vec3))
                    {
                        flag = true;
                    }
                }
            }

            if (flag)
            {
                return stack;
            }
            else
            {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (world.getBlock(i, j, k) == Blocks.snow_layer)
                    {
                        --j;
                    }

                    EntityNissanSkylineR34 car = new EntityNissanSkylineR34(world);
                    car.setPosition(i + 0.5F, j + 1, k + 0.5F);

                    car.rotationYaw = (float)(((MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                    if (!world.getCollidingBoundingBoxes(car, car.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                    {
                        return stack;
                    }

                    if (!world.isRemote)
                    {
                        world.spawnEntityInWorld(car);
                    }

                    if (!player.capabilities.isCreativeMode)
                    {
                        --stack.stackSize;
                    }
                }

                return stack;
            }
        }
    }
}