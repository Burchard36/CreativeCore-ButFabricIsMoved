package team.creative.creativecore.common.util.math.box;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import net.minecraft.core.AxisCycle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.SliceShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.creative.creativecore.common.util.math.base.Facing;
import team.creative.creativecore.common.util.math.vec.Vec3d;
import team.creative.creativecore.common.util.type.list.SingletonList;
import team.creative.creativecore.common.util.unsafe.CreativeHackery;
import team.creative.creativecore.mixin.VoxelShapeAccessor;

public class AABBVoxelShape extends SliceShape {
    
    public static double get(AABB bb, Facing facing) {
        return switch (facing) {
            case EAST -> bb.maxX;
            case WEST -> bb.minX;
            case UP -> bb.maxY;
            case DOWN -> bb.minY;
            case SOUTH -> bb.maxZ;
            case NORTH -> bb.minZ;
        };
    }
    
    public static double getMin(AABB bb, team.creative.creativecore.common.util.math.base.Axis axis) {
        return switch (axis) {
            case X -> bb.minX;
            case Y -> bb.minY;
            case Z -> bb.minZ;
        };
    }
    
    public static double getMax(AABB bb, team.creative.creativecore.common.util.math.base.Axis axis) {
        return switch (axis) {
            case X -> bb.maxX;
            case Y -> bb.maxY;
            case Z -> bb.maxZ;
        };
    }
    
    public static double getMin(AABB bb, Axis axis) {
        return switch (axis) {
            case X -> bb.minX;
            case Y -> bb.minY;
            case Z -> bb.minZ;
        };
    }
    
    public static double getMax(AABB bb, Axis axis) {
        return switch (axis) {
            case X -> bb.maxX;
            case Y -> bb.maxY;
            case Z -> bb.maxZ;
        };
    }
    
    public static Vec3d getCorner(AABB bb, BoxCorner corner) {
        return new Vec3d(getCornerX(bb, corner), getCornerY(bb, corner), getCornerZ(bb, corner));
    }
    
    public static double getCornerValue(AABB bb, BoxCorner corner, team.creative.creativecore.common.util.math.base.Axis axis) {
        return get(bb, corner.getFacing(axis));
    }
    
    public static double getCornerX(AABB bb, BoxCorner corner) {
        return get(bb, corner.x);
    }
    
    public static double getCornerY(AABB bb, BoxCorner corner) {
        return get(bb, corner.y);
    }
    
    public static double getCornerZ(AABB bb, BoxCorner corner) {
        return get(bb, corner.z);
    }
    
    public static boolean intersectsWithAxis(AABB bb, AABB other, Axis one, Axis two) {
        return bb.min(one) < other.max(one) && bb.max(one) > bb.min(one) && bb.min(two) < bb.max(two) && bb.max(two) > bb.min(two);
    }
    
    public static boolean intersectsWithAxis(AABB bb, Axis one, Axis two, double valueOne, double valueTwo) {
        return bb.min(one) < valueOne && bb.max(one) > valueOne && bb.min(two) < valueTwo && bb.max(two) > valueTwo;
    }
    
    public static double calculateAxisOffset(Axis axis, Axis one, Axis two, AABB bb, AABB other, double offset) {
        if (intersectsWithAxis(bb, other, one, two))
            if (offset > 0.0D && other.max(axis) <= bb.min(axis)) {
                double newDistance = bb.min(axis) - other.maxX;
                if (newDistance < offset)
                    return newDistance;
            } else if (offset < 0.0D && other.min(axis) >= bb.max(axis)) {
                double newDistance = bb.max(axis) - other.min(axis);
                if (newDistance > offset)
                    return newDistance;
            }
        return offset;
    }
    
    public static final DiscreteVoxelShape DISCRETE_SHAPE = new DiscreteVoxelShape(1, 1, 1) {
        
        @Override
        public boolean isFull(int x, int y, int z) {
            return true;
        }
        
        @Override
        public void fill(int x, int y, int z) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int firstFull(Axis p_82827_) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int lastFull(Axis p_82840_) {
            throw new UnsupportedOperationException();
        }
        
    };
    
    public static AABBVoxelShape create(AABB bb) {
        AABBVoxelShape shape = CreativeHackery.allocateInstance(AABBVoxelShape.class);
        shape.bb = bb;
        ((VoxelShapeAccessor) shape).setShape(DISCRETE_SHAPE);
        return shape;
    }
    
    public AABB bb;
    
    protected AABBVoxelShape() {
        super(null, null, 0);
    }
    
    public boolean contains(Vec3d vec) {
        if (vec.x > bb.minX && vec.x < bb.maxX)
            if (vec.y > bb.minY && vec.y < bb.maxY)
                return vec.z > bb.minZ && vec.z < bb.maxZ;
            else
                return false;
        return false;
    }
    
    protected double get(Facing facing) {
        return switch (facing) {
            case EAST -> bb.maxX;
            case WEST -> bb.minX;
            case UP -> bb.maxY;
            case DOWN -> bb.minY;
            case SOUTH -> bb.maxZ;
            case NORTH -> bb.minZ;
        };
    }
    
    public Vec3d getCorner(BoxCorner corner) {
        return new Vec3d(getCornerX(corner), getCornerY(corner), getCornerZ(corner));
    }
    
    public double getCornerValue(BoxCorner corner, team.creative.creativecore.common.util.math.base.Axis axis) {
        return get(corner.getFacing(axis));
    }
    
    public double getCornerX(BoxCorner corner) {
        return get(corner.x);
    }
    
    public double getCornerY(BoxCorner corner) {
        return get(corner.y);
    }
    
    public double getCornerZ(BoxCorner corner) {
        return get(corner.z);
    }
    
    public Vec3d getSizeVec() {
        return new Vec3d(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ);
    }
    
    public double getVolume() {
        return (bb.maxX - bb.minX) * (bb.maxY - bb.minY) * (bb.maxZ - bb.minZ);
    }
    
    public double getIntersectionVolume(AABB other) {
        double d0 = Math.max(bb.minX, other.minX);
        double d1 = Math.max(bb.minY, other.minY);
        double d2 = Math.max(bb.minZ, other.minZ);
        double d3 = Math.min(bb.maxX, other.maxX);
        double d4 = Math.min(bb.maxY, other.maxY);
        double d5 = Math.min(bb.maxZ, other.maxZ);
        if (d0 < d3 && d1 < d4 && d2 < d5)
            return Math.abs((d3 - d0) * (d4 - d1) * (d5 - d2));
        return 0;
    }
    
    public double getSize(Axis axis) {
        return switch (axis) {
            case X -> bb.maxX - bb.minX;
            case Y -> bb.maxY - bb.minY;
            case Z -> bb.maxZ - bb.minZ;
        };
    }
    
    public double getMin(Axis axis) {
        return switch (axis) {
            case X -> bb.minX;
            case Y -> bb.minY;
            case Z -> bb.minZ;
        };
    }
    
    public double getMax(Axis axis) {
        return switch (axis) {
            case X -> bb.maxX;
            case Y -> bb.maxY;
            case Z -> bb.maxZ;
        };
    }
    
    public double getMin(team.creative.creativecore.common.util.math.base.Axis axis) {
        return switch (axis) {
            case X -> bb.minX;
            case Y -> bb.minY;
            case Z -> bb.minZ;
        };
    }
    
    public double getMax(team.creative.creativecore.common.util.math.base.Axis axis) {
        return switch (axis) {
            case X -> bb.maxX;
            case Y -> bb.maxY;
            case Z -> bb.maxZ;
        };
    }
    
    @Override
    protected DoubleList getCoords(Axis axis) {
        return new AABBDoubleList(axis);
    }
    
    @Override
    public double min(Direction.Axis axis) {
        return bb.min(axis);
    }
    
    @Override
    public double max(Direction.Axis axis) {
        return bb.max(axis);
    }
    
    @Override
    public AABB bounds() {
        return bb;
    }
    
    @Override
    protected double get(Direction.Axis axis, int index) {
        return this.getCoords(axis).getDouble(index);
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public VoxelShape move(double x, double y, double z) {
        return create(bb.move(x, y, z));
    }
    
    @Override
    public VoxelShape optimize() {
        return this;
    }
    
    @Override
    public void forAllEdges(Shapes.DoubleLineConsumer consumer) {
        for (BoxFace face : BoxFace.values()) {
            forAllEdgesCorner(face.corners[0], face.corners[1], consumer);
            forAllEdgesCorner(face.corners[1], face.corners[2], consumer);
            forAllEdgesCorner(face.corners[2], face.corners[3], consumer);
            forAllEdgesCorner(face.corners[3], face.corners[0], consumer);
        }
    }
    
    private void forAllEdgesCorner(BoxCorner corner1, BoxCorner corner2, Shapes.DoubleLineConsumer consumer) {
        consumer.consume(getCornerX(corner1), getCornerY(corner1), getCornerZ(corner1), getCornerX(corner2), getCornerY(corner2), getCornerZ(corner2));
    }
    
    @Override
    public void forAllBoxes(Shapes.DoubleLineConsumer consumer) {
        consumer.consume(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }
    
    @Override
    public List<AABB> toAabbs() {
        return new SingletonList<>(bb);
    }
    
    @Override
    public double min(Direction.Axis axis, double one, double two) {
        // Only used with Axis.Y, one as X, two as Z
        Axis axisOne = Axis.X;
        Axis axisTwo = Axis.Z;
        double min = Double.POSITIVE_INFINITY;
        if (AABBVoxelShape.intersectsWithAxis(bb, axisOne, axisTwo, one, two))
            min = Math.min(min, bb.min(axis));
        return min;
    }
    
    @Override
    public double max(Direction.Axis axis, double one, double two) {
        // Only used with Axis.Y, one as X, two as Z
        Axis axisOne = Axis.X;
        Axis axisTwo = Axis.Z;
        double max = Double.NEGATIVE_INFINITY;
        if (AABBVoxelShape.intersectsWithAxis(bb, axisOne, axisTwo, one, two))
            max = Math.max(max, bb.max(axis));
        return max;
    }
    
    @Override
    protected int findIndex(Direction.Axis axis, double value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    @Nullable
    public BlockHitResult clip(Vec3 pos, Vec3 look, BlockPos block) {
        return AABB.clip(new SingletonList<>(bb), pos, look, block);
    }
    
    @Override
    public Optional<Vec3> closestPointTo(Vec3 vec) {
        Vec3 avec3 = null;
        double d0 = Mth.clamp(vec.x(), bb.minX, bb.maxX);
        double d1 = Mth.clamp(vec.y(), bb.minY, bb.maxY);
        double d2 = Mth.clamp(vec.z(), bb.minZ, bb.maxZ);
        if (avec3 == null || vec.distanceToSqr(d0, d1, d2) < vec.distanceToSqr(avec3))
            avec3 = new Vec3(d0, d1, d2);
        return Optional.of(avec3);
    }
    
    @Override
    public VoxelShape getFaceShape(Direction direction) {
        return this;
    }
    
    public double collideStepUp(AABB other, AABB otherY, double offset) {
        double newOffset = collide(Axis.Y, otherY, offset);
        if (offset > 0) {
            if (newOffset < offset)
                return newOffset / 2;
        } else {
            if (newOffset > offset)
                return newOffset / 2;
        }
        
        return newOffset;
    }
    
    @Override
    public double collide(Direction.Axis axis, AABB bb, double value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected double collideX(AxisCycle cycle, AABB bb, double distance) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return "AABBVoxelShape[" + bb + "]";
    }
    
    public class AABBDoubleList implements DoubleList {
        
        public final Axis axis;
        
        public AABBDoubleList(Axis axis) {
            this.axis = axis;
        }
        
        @Override
        public int size() {
            return 2;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public Object[] toArray() {
            return new Object[] { bb.min(axis), bb.max(axis) };
        }
        
        @Override
        public <T> T[] toArray(T[] a) {
            if (a.length < 2)
                a = (T[]) Array.newInstance(a.getClass().getComponentType(), 2);
            a[0] = (T) Double.valueOf(bb.min(axis));
            a[1] = (T) Double.valueOf(bb.max(axis));
            while (a.length > 2)
                a[2] = null;
            return a;
        }
        
        @Override
        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(Collection<? extends Double> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(int index, Collection<? extends Double> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compareTo(List<? extends Double> o) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(double key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean rem(double key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double[] toDoubleArray() {
            return new double[] { bb.min(axis), bb.max(axis) };
        }
        
        @Override
        public double[] toArray(double[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean containsAll(DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public DoubleListIterator iterator() {
            return new DoubleListIterator() {
                
                private int previousIndex = 2;
                private int nextIndex;
                
                @Override
                public int previousIndex() {
                    return previousIndex;
                }
                
                @Override
                public int nextIndex() {
                    return nextIndex;
                }
                
                @Override
                public boolean hasPrevious() {
                    return previousIndex > 0;
                }
                
                @Override
                public boolean hasNext() {
                    return nextIndex < 2;
                }
                
                @Override
                public double nextDouble() {
                    nextIndex++;
                    return nextIndex == 1 ? bb.min(axis) : bb.max(axis);
                }
                
                @Override
                public double previousDouble() {
                    previousIndex--;
                    return previousIndex == 1 ? bb.min(axis) : bb.max(axis);
                }
            };
        }
        
        @Override
        public DoubleListIterator listIterator() {
            return iterator();
        }
        
        @Override
        public DoubleListIterator listIterator(int index) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public DoubleList subList(int from, int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(int size) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(int from, double[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void removeElements(int from, int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(int index, double[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(int index, double[] a, int offset, int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean add(double key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(int index, double key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(int index, DoubleCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double set(int index, double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double getDouble(int index) {
            return index == 0 ? bb.min(axis) : bb.max(axis);
        }
        
        @Override
        public int indexOf(double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int lastIndexOf(double k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double removeDouble(int index) {
            throw new UnsupportedOperationException();
        }
        
    }
}
