package com.github.startsmercury.simply_no_shading.util;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockStateRaycastContext;
import net.minecraft.world.LightType;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.level.ColorResolver;

/**
 * Delegates all methods for a given {@link BlockRenderView block render view},
 * but changes {@link #getBrightness(Direction, boolean)}.
 *
 * @see com.github.startsmercury.simply_no_shading.util
 * @see BlockRenderView
 * @see #getBrightness(Direction, boolean)
 */
public class ShadelessBlockRenderView implements BlockRenderView {
	/**
	 * Creates a new {@link ShadelessBlockRenderView} if the given {@code world}
	 * isn't.
	 *
	 * @param world delegatee
	 * @return an instance of {@link ShadelessBlockRenderView}
	 * @see #ShadelessBlockRenderView(BlockRenderView)
	 */
	public static ShadelessBlockRenderView of(final BlockRenderView world) {
		return world instanceof final ShadelessBlockRenderView instance ? instance
				: new ShadelessBlockRenderView(world);
	}

	/**
	 * The delegated block render view.
	 *
	 * @see #ShadelessBlockRenderView(BlockRenderView)
	 */
	private final BlockRenderView world;

	/**
	 * @param world delegatee
	 * @see #world
	 * @see #of(BlockRenderView)
	 */
	public ShadelessBlockRenderView(final BlockRenderView world) {
		this.world = world;
	}

	@Override
	public int countVerticalSections() {
		return this.world.countVerticalSections();
	}

	@Override
	public int getBaseLightLevel(final BlockPos pos, final int ambientDarkness) {
		return this.world.getBaseLightLevel(pos, ambientDarkness);
	}

	@Override
	public BlockEntity getBlockEntity(final BlockPos arg0) {
		return this.world.getBlockEntity(arg0);
	}

	@Override
	public <T extends BlockEntity> Optional<T> getBlockEntity(final BlockPos pos, final BlockEntityType<T> type) {
		return this.world.getBlockEntity(pos, type);
	}

	@Override
	public BlockState getBlockState(final BlockPos pos) {
		return this.world.getBlockState(pos);
	}

	@Override
	public int getBottomSectionCoord() {
		return this.world.getBottomSectionCoord();
	}

	@Override
	public int getBottomY() {
		return this.world.getBottomY();
	}

	/**
	 * This calls the delegated method as if passing it with {@link Direction#UP}
	 * and {@code false}.
	 *
	 * @param direction side to get brightness from
	 * @param shaded    decreases brightness to some sides
	 * @return most brightness possible
	 * @see Direction#UP
	 * @see #world
	 * @see ShadelessBlockRenderView
	 * @see BlockRenderView#getBrightness(Direction, boolean)
	 */
	@Override
	public float getBrightness(final Direction direction, final boolean shaded) {
		return this.world.getBrightness(Direction.UP, false);
	}

	@Override
	public int getColor(final BlockPos pos, final ColorResolver colorResolver) {
		return this.world.getColor(pos, colorResolver);
	}

	@Override
	public double getDismountHeight(final BlockPos pos) {
		return this.world.getDismountHeight(pos);
	}

	@Override
	public double getDismountHeight(final VoxelShape blockCollisionShape,
			final Supplier<VoxelShape> belowBlockCollisionShapeGetter) {
		return this.world.getDismountHeight(blockCollisionShape, belowBlockCollisionShapeGetter);
	}

	@Override
	public FluidState getFluidState(final BlockPos pos) {
		return this.world.getFluidState(pos);
	}

	@Override
	public int getHeight() {
		return this.world.getHeight();
	}

	@Override
	public LightingProvider getLightingProvider() {
		return this.world.getLightingProvider();
	}

	@Override
	public int getLightLevel(final LightType type, final BlockPos pos) {
		return this.world.getLightLevel(type, pos);
	}

	@Override
	public int getLuminance(final BlockPos pos) {
		return this.world.getLuminance(pos);
	}

	@Override
	public int getMaxLightLevel() {
		return this.world.getMaxLightLevel();
	}

	@Override
	public int getSectionIndex(final int y) {
		return this.world.getSectionIndex(y);
	}

	@Override
	public Stream<BlockState> getStatesInBox(final Box box) {
		return this.world.getStatesInBox(box);
	}

	@Override
	public int getTopSectionCoord() {
		return this.world.getTopSectionCoord();
	}

	@Override
	public int getTopY() {
		return this.world.getTopY();
	}

	public BlockRenderView getWorld() {
		return this.world;
	}

	@Override
	public boolean isOutOfHeightLimit(final BlockPos pos) {
		return this.world.isOutOfHeightLimit(pos);
	}

	@Override
	public boolean isOutOfHeightLimit(final int y) {
		return this.world.isOutOfHeightLimit(y);
	}

	@Override
	public boolean isSkyVisible(final BlockPos pos) {
		return this.world.isSkyVisible(pos);
	}

	@Override
	public BlockHitResult raycast(final BlockStateRaycastContext context) {
		return this.world.raycast(context);
	}

	@Override
	public BlockHitResult raycast(final RaycastContext context) {
		return this.world.raycast(context);
	}

	@Override
	public BlockHitResult raycastBlock(final Vec3d start, final Vec3d end, final BlockPos pos, final VoxelShape shape,
			final BlockState state) {
		return this.world.raycastBlock(start, end, pos, shape, state);
	}

	@Override
	public int sectionCoordToIndex(final int coord) {
		return this.world.sectionCoordToIndex(coord);
	}

	@Override
	public int sectionIndexToCoord(final int index) {
		return this.world.sectionIndexToCoord(index);
	}
}
