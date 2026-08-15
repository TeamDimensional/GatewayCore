package com.dimensional.gatewaycore.mmce;

import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.util.IEnergyHandlerAsync;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyBlockComponent extends MachineComponent.EnergyHatch {

    public CustomEnergyBlockComponent(IOType ioType, EnergyStorage handler) {
        super(ioType);
        this.handler = new EnergyHandlerWrapper(handler);
    }

    IEnergyHandlerAsync handler;

    @Override
    public IEnergyHandlerAsync getContainerProvider() {
        return handler;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    private static class EnergyHandlerWrapper implements IEnergyHandlerAsync {
        private final EnergyStorage handler;

        public EnergyHandlerWrapper(EnergyStorage handler) {
            this.handler = handler;
        }

        @Override
        public long getCurrentEnergy() {
            return handler.getEnergyStored();
        }

        @Override
        public long getMaxEnergy() {
            return handler.getMaxEnergyStored();
        }

        @Override
        public void setCurrentEnergy(long arg0) {
            int newEnergy = Math.toIntExact(arg0);
            int delta = newEnergy - handler.getEnergyStored();
            if (delta > 0) {
                handler.receiveEnergy(delta, false);
            } else {
                handler.extractEnergy(-delta, false);
            }
        }

        @Override
        public boolean extractEnergy(long arg0) {
            int cost = Math.toIntExact(arg0);
            if (handler.extractEnergy(cost, true) != cost) {
                return false;
            }
            handler.extractEnergy(cost, false);
            return true;
        }

        @Override
        public boolean receiveEnergy(long arg0) {
            int cost = Math.toIntExact(arg0);
            if (handler.receiveEnergy(cost, true) != cost) {
                return false;
            }
            handler.receiveEnergy(cost, false);
            return true;
        }
    }

}
