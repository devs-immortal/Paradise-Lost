package net.id.paradiselost.util;

import com.mojang.datafixers.util.Function5;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class AStarManager {

    public static Builder createBuilder() {
        return new Builder();
    }

    public static class APather {

        private final PriorityQueue<Node> queue;
        private final WorldView world;
        private final BlockPosProvider start, goal;
        private final BiFunction<WorldView, BlockPos, BlockPos> checkAdjuster;
        private final BiPredicate<WorldView, BlockPos> validator;
        private final Function5<BlockPos, BlockPos, WorldView, BlockPos, Double, Integer> costMapper;
        private final BiPredicate<WorldView, Optional<Node>> outputValidator;
        private final boolean allowRecompute;
        private final boolean allowDiagionalMovement;
        private final int expectedPathLength;
        private final double heuristic;

        private Optional<PathingOutput> lastOutput;
        private boolean complete;

        private APather(WorldView world, BlockPosProvider start, BlockPosProvider goal, BiFunction<WorldView, BlockPos, BlockPos> checkAdjuster, BiPredicate<WorldView, BlockPos> validator, Function5<BlockPos, BlockPos, WorldView, BlockPos, Double, Integer> costMapper, BiPredicate<WorldView, Optional<Node>> outputValidator, int expectedPathLength, boolean allowDiagionalMovement, boolean allowRecompute, double heuristic) {
            this.heuristic = heuristic;
            Comparator<Node> comparator = Comparator.comparing(node -> node.cost);
            this.queue = new PriorityQueue<>(expectedPathLength, comparator);
            this.world = world;
            this.start = start;
            this.goal = goal;
            this.checkAdjuster = checkAdjuster;
            this.validator = validator;
            this.costMapper = costMapper;
            this.outputValidator = outputValidator;
            this.allowDiagionalMovement = allowDiagionalMovement;
            this.allowRecompute = allowRecompute;
            this.expectedPathLength = expectedPathLength;
        }

        public void compute() {
            if(complete && !allowRecompute) {
                throw new IllegalStateException("attempted to compute a completed path, did you forget to set allowRecompute?");
            }

            var rootPos = start.get(world);
            var goalPos = goal.get(world);

            Node root = new Node(rootPos, Integer.MIN_VALUE, null, true);
            Optional<Node> endNode = Optional.empty();
            Set<BlockPos> pastNodes = new HashSet<>();

            queue.add(root);

            int iterations = 0;

            while(!queue.isEmpty()) {
                if(iterations > expectedPathLength * 20) {
                    break;
                }

                var currentNode = queue.poll();
                pastNodes.add(currentNode.pos);

                if(currentNode.pos.equals(goalPos)) {
                    endNode = Optional.of(currentNode);
                    break;
                }

                process(rootPos, goalPos, currentNode, pastNodes);

                iterations++;
            }

            lastOutput = constructOutput(rootPos, goalPos, endNode, pastNodes);

            complete = true;
        }

        private void process(BlockPos start, BlockPos goal, Node current, Set<BlockPos> pastNodes) {
            var curPos = current.pos;

            for (Direction direction : Direction.values()) {

                var testPos = curPos.offset(direction);

                if(allowDiagionalMovement) {
                    for (Direction diagonal : Direction.values()) {
                        if(diagonal.getAxis() != direction.getAxis()) {
                            var diagonalPos = checkAdjuster.apply(world, testPos.offset(diagonal));

                            if(!pastNodes.contains(diagonalPos) && validator.test(world, diagonalPos)) {

                                var cost = costMapper.apply(start, goal, world, diagonalPos, heuristic);

                                queue.add(new Node(diagonalPos, cost, current, false));
                            }
                        }
                    }
                }
                else {
                    testPos = checkAdjuster.apply(world, testPos);

                    if(!pastNodes.contains(testPos) && validator.test(world, testPos)) {

                        var cost = costMapper.apply(start, goal, world, testPos, heuristic);

                        queue.add(new Node(testPos, cost, current, false));
                    }
                }
            }
        }

        private Optional<PathingOutput> constructOutput(BlockPos start, BlockPos goal, Optional<Node> endNode, Set<BlockPos> pastNodes) {
            if(outputValidator.test(world, endNode) && endNode.isPresent()) {
                var node = endNode.get();
                var path = new LinkedList<Node>();

                while(!node.root) {
                    path.add(node);
                    node = node.parent;
                }

                path.add(node);

                return Optional.of(new PathingOutput(path, start, goal, pastNodes));
            }
            return Optional.empty();
        }

        public boolean hasComputed() {
            return complete;
        }

        public Optional<PathingOutput> getLastOutput() {
            return lastOutput;
        }
    }

    public static class Builder {

        private BlockPosProvider start, goal;
        private BiFunction<WorldView, BlockPos, BlockPos> checkAdjuster = a_noop;
        private BiPredicate<WorldView, BlockPos> validator = v_alwaysTrue;
        private Function5<BlockPos, BlockPos, WorldView, BlockPos, Double, Integer> costMapper = c_simple;
        private BiPredicate<WorldView, Optional<Node>> outputValidator = o_pass;
        private boolean allowDiagonalMovement;
        private boolean allowRecompute;
        private int expectedPathLength = 32;
        private double heuristic = 1.334;

        private Builder() {}

        public void checkAdjuster(@NotNull BiFunction<WorldView, BlockPos, BlockPos> checkAdjuster) {
            this.checkAdjuster = checkAdjuster;
        }

        public void validator(@NotNull BiPredicate<WorldView, BlockPos> validator) {
            this.validator = validator;
        }

        public void costMapper(@NotNull Function5<BlockPos, BlockPos, WorldView, BlockPos, Double, Integer> costMapper) {
            this.costMapper = costMapper;
        }

        public void outputValidator(@NotNull BiPredicate<WorldView, Optional<Node>> outputValidator) {
            this.outputValidator = outputValidator;
        }

        public void start(@NotNull BlockPosProvider start) {
            this.start = start;
        }

        public void goal(@NotNull BlockPosProvider goal) {
            this.goal = goal;
        }

        public void expectedLength(int expectedPathLength) {
            this.expectedPathLength = expectedPathLength;
        }

        public void heuristic(double heuristic) {
            this.heuristic = heuristic;
        }

        public void allowRecompute() {
            this.allowRecompute = true;
        }

        public void allowDiagonalMovement() {
            this.allowDiagonalMovement = true;
        }

        public APather build(@NotNull WorldView world) {
            if(start == null) {
                throw new IllegalArgumentException("start must not be null");
            }
            if(goal == null) {
                throw new IllegalArgumentException("goal must not be null");
            }

            return new APather(world, start, goal, checkAdjuster, validator, costMapper, outputValidator, expectedPathLength, allowDiagonalMovement, allowRecompute, heuristic);
        }

        public static final BiFunction<WorldView, BlockPos, BlockPos> a_noop = (worldView, pos) -> pos;
        public static final BiFunction<WorldView, BlockPos, BlockPos> a_simpleGround = (worldView, pos) -> {
            if(worldView.isAir(pos.down())) {
                return pos.down();
            }
            return pos;
        };

        public static final BiPredicate<WorldView, BlockPos> v_alwaysTrue = (worldView, blockPos) -> true;
        public static final BiPredicate<WorldView, BlockPos> v_replaceable = (worldView, blockPos) -> worldView.getBlockState(blockPos).getMaterial().isReplaceable();
        public static final BiPredicate<WorldView, BlockPos> v_noFly = (worldView, blockPos) -> !worldView.isAir(blockPos.down());
        public static final Function5<BlockPos, BlockPos, WorldView, BlockPos, Double, Integer> c_simple = (start, goal, worldView, current, heuristic) -> (int) (current.getManhattanDistance(start) / heuristic + current.getManhattanDistance(goal));
        public static final Function5<BlockPos, BlockPos, WorldView, BlockPos, Double, Integer> c_favorReplaceable = (start, goal, worldView, current, heuristic) -> (int) (current.getManhattanDistance(start) / heuristic + current.getManhattanDistance(goal) + (worldView.getBlockState(current).getMaterial().isReplaceable() ? 0 : 10));
        public static final BiPredicate<WorldView, Optional<Node>> o_pass = (worldView, node) -> true;
        public static final BiPredicate<WorldView, Optional<Node>> o_present = (worldView, node) -> node.isPresent();
    }

    public record Node(BlockPos pos, int cost, @Nullable Node parent, boolean root) {}

    public record PathingOutput(LinkedList<Node> path, BlockPos start, BlockPos goal, Set<BlockPos> checkedBlocks) {}

    @FunctionalInterface
    public interface BlockPosProvider {
        @NotNull BlockPos get(WorldView world);

        static BlockPosProvider simple(BlockPos pos) {
            return world -> pos;
        }
    }
}
