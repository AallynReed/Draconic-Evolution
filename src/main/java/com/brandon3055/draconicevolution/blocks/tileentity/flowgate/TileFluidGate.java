package com.brandon3055.draconicevolution.blocks.tileentity.flowgate;

import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.inventory.FlowGateMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;


/**
 * Created by brandon3055 on 15/11/2016.
 */
public class TileFluidGate extends TileFlowGate {

    private FlowHandler inputHandler = new FlowHandler(this, true);
    private FlowHandler outputHandler = new FlowHandler(this, false);

    public TileFluidGate(BlockPos pos, BlockState state) {
        super(DEContent.TILE_FLUID_GATE.get(), pos, state);
    }

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Fluid.BLOCK, DEContent.TILE_FLUID_GATE.get(), (tile, side) -> {
            if (side != null && side.getAxis() == tile.getDirection().getAxis()) {
                return side == tile.getDirection() ? tile.outputHandler : tile.inputHandler;
            }
            return null;
        });
    }

    @Override
    public String getUnits() {
        return "MB/t";
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new FlowGateMenu(id, player.getInventory(), this);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Player player, BlockHitResult hit) {
        if (player instanceof ServerPlayer) {
            player.openMenu(this, worldPosition);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    private class FlowHandler implements ResourceHandler<FluidResource> {
        private final TileFluidGate gate;
        private final boolean isInput;

        public FlowHandler(TileFluidGate gate, boolean isInput) {
            this.gate = gate;
            this.isInput = isInput;
        }

        @Override
        public int size() {
            if (isInput) {
                BlockEntity tile = getTarget();
                if (tile != null) {
                    ResourceHandler<FluidResource> fluidHandler = Capabilities.Fluid.BLOCK.getCapability(tile.getLevel(), tile.getBlockPos(), tile.getBlockState(), tile, getDirection().getOpposite());
                    if (fluidHandler != null) {
                        return fluidHandler.size();
                    }
                }
            }
            return 1;
        }

        @Override
        public FluidResource getResource(int index) {
            if (!isInput) {
                BlockEntity tile = getSource();
                if (tile != null) {
                    ResourceHandler<FluidResource> fluidHandler = Capabilities.Fluid.BLOCK.getCapability(tile.getLevel(), tile.getBlockPos(), tile.getBlockState(), tile, getDirection().getOpposite());
                    if (fluidHandler != null) {
                        return fluidHandler.getResource(index);
                    }
                }
            }

            return FluidResource.EMPTY;
        }

        @Override
        public long getAmountAsLong(int index) {
            if (!isInput) {
                BlockEntity tile = getSource();
                if (tile != null) {
                    ResourceHandler<FluidResource> fluidHandler = Capabilities.Fluid.BLOCK.getCapability(tile.getLevel(), tile.getBlockPos(), tile.getBlockState(), tile, getDirection().getOpposite());
                    if (fluidHandler != null) {
                        return fluidHandler.getAmountAsLong(index);
                    }
                }
            }

            return 0;
        }

        @Override
        public long getCapacityAsLong(int index, FluidResource resource) {
            if (isInput) {
                BlockEntity tile = getTarget();
                if (tile != null) {
                    ResourceHandler<FluidResource> fluidHandler = Capabilities.Fluid.BLOCK.getCapability(tile.getLevel(), tile.getBlockPos(), tile.getBlockState(), tile, getDirection().getOpposite());
                    if (fluidHandler != null) {
                        return fluidHandler.getCapacityAsLong(index, resource);
                    }
                }
            }
            return 0;
        }

        @Override
        public boolean isValid(int index, FluidResource resource) {
            if (isInput) {
                BlockEntity tile = getTarget();
                if (tile != null) {
                    ResourceHandler<FluidResource> fluidHandler = Capabilities.Fluid.BLOCK.getCapability(tile.getLevel(), tile.getBlockPos(), tile.getBlockState(), tile, getDirection().getOpposite());
                    if (fluidHandler != null) {
                        return fluidHandler.isValid(index, resource);
                    }
                }
            }
            return false;
        }

        @Override
        public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
            if (isInput) {
                BlockEntity tile = getTarget();
                if (tile != null) {
                    ResourceHandler<FluidResource> handler = Capabilities.Fluid.BLOCK.getCapability(tile.getLevel(), tile.getBlockPos(), tile.getBlockState(), tile, getDirection().getOpposite());
                    if (handler != null) {
                        return handler.insert(index, resource, (int) Math.min(getFlow(), amount), transaction);
                    }
                }
            }
            return 0;
        }

        @Override
        public int insert(FluidResource resource, int amount, TransactionContext transaction) {
            if (isInput) {
                BlockEntity tile = getTarget();
                if (tile != null) {
                    ResourceHandler<FluidResource> handler = Capabilities.Fluid.BLOCK.getCapability(tile.getLevel(), tile.getBlockPos(), tile.getBlockState(), tile, getDirection().getOpposite());
                    if (handler != null) {
                        return handler.insert(resource, (int) Math.min(getFlow(), amount), transaction);
                    }
                }
            }
            return 0;
        }

        @Override
        public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
            if (!isInput) {
                BlockEntity tile = getSource();
                if (tile != null) {
                    ResourceHandler<FluidResource> fluidHandler = Capabilities.Fluid.BLOCK.getCapability(tile.getLevel(), tile.getBlockPos(), tile.getBlockState(), tile, getDirection().getOpposite());
                    if (fluidHandler != null) {
                        return fluidHandler.extract(index, resource, (int) Math.min(getFlow(), amount), transaction);
                    }
                }
            }
            return 0;
        }

        @Override
        public int extract(FluidResource resource, int amount, TransactionContext transaction) {
            if (!isInput) {
                BlockEntity tile = getSource();
                if (tile != null) {
                    ResourceHandler<FluidResource> fluidHandler = Capabilities.Fluid.BLOCK.getCapability(tile.getLevel(), tile.getBlockPos(), tile.getBlockState(), tile, getDirection().getOpposite());
                    if (fluidHandler != null) {
                        return fluidHandler.extract(resource, (int) Math.min(getFlow(), amount), transaction);
                    }
                }
            }
            return 0;
        }
    }
}
