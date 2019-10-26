package com.fox.ancientchina.core.capabilities.base;

import com.fox.ancientchina.core.api.capability.ISerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 这个参考了BL的代码，但没有使用观察者模式
 * 这个类负责将Entity的capabilities内部实现和包装，
 * TileEntity都有自己的实现了，为什么Entity不能有（滑稽）？
 * @param <F> 默认实现
 * @param <C> capability接口
 * @param <E> 绑定实体
 */
public abstract class EntityCapability<F extends EntityCapability<F,C,E>,C,E extends Entity> extends AbstractCapability<F,C,E> {
    private E entity;

    void setEntity(Entity entity){
        this.entity = (E) entity;
    }

    public final E getEntity() {
        return this.entity;
    }

    protected void init(){

    }

    /**
     * 设置使用的实体范围
     * 比如可以判定entity是否属于玩家等等。
     */
    public boolean isApplicable(Entity entity) {
        return false;
    }

    @SuppressWarnings("unchecked")
    public EntityCapability<?,?,E> getEntityCapability(E entity) {
        if (entity.hasCapability(this.getCapability(),null)){
            return (EntityCapability<?,?,E>)entity.getCapability(this.getCapability(),null);
        }
        return null;
    }

    /**
     * 判断这个能力是否对玩家永久绑定，即是否有必要实现接口{@link com.fox.ancientchina.core.api.capability.ISerializableData}
     */
    public boolean isPersistent(EntityPlayer oldPlayer,EntityPlayer newPlayer,boolean wasDead){
        return !wasDead;
    }

    /**
     * 玩家在死亡重生或者从一个维度来到另一个维度时，
     * 在原版中是先复制一个玩家，再把玩家数据转移到这个复制品上面,
     * 所以需要这个方法将数据转移至玩家上面
     */
    public void cloneDataToPlayer(EntityPlayer oldPlayer, EntityPlayer newPlayer, boolean wasDead, ISerializableData capability){
        if (this instanceof ISerializableData){
            NBTTagCompound nbt = new NBTTagCompound();
            ((ISerializableData)this).writeToNBT(nbt);
            capability.readFromNBT(nbt);
        }
    }
}
