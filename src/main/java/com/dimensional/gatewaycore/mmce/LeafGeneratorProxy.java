package com.dimensional.gatewaycore.mmce;

import github.kasuminova.mmce.common.machine.component.MachineComponentProxy;
import hellfirepvp.modularmachinery.common.machine.IOType;
import net.minecraft.tileentity.TileEntity;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityLeafGenerator;

public class LeafGeneratorProxy implements MachineComponentProxy<CustomEnergyBlockComponent> {
    public static final LeafGeneratorProxy INSTANCE = new LeafGeneratorProxy();

    @Override
    public boolean isSupported(TileEntity arg0) {
        return arg0 instanceof TileEntityLeafGenerator;
    }

    @Override
    public CustomEnergyBlockComponent proxyComponent(TileEntity arg0) {
        if (arg0 instanceof TileEntityLeafGenerator) {
            TileEntityLeafGenerator leafGen = (TileEntityLeafGenerator) arg0;
            return new CustomEnergyBlockComponent(IOType.INPUT, leafGen.storage);
        }
        return null;
    } 
}
