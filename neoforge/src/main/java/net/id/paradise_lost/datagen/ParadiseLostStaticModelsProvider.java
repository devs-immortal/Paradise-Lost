package net.id.paradise_lost.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.id.paradise_lost.ModConstants;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder.FaceRotation;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ParadiseLostStaticModelsProvider extends ModelProvider<ParadiseLostStaticModelsProvider.Builder> {
    public ParadiseLostStaticModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModConstants.MODID, BLOCK_FOLDER, Builder::new, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Paradise Lost Models";
    }

    @Override
    protected void registerModels() {
        bluePortal("block/blue_portal_ew", 6, 0, 0, 10, 16, 16, Direction.EAST, Direction.WEST);
        bluePortal("block/blue_portal_flat", 0, 6, 0, 16, 10, 16, Direction.UP, Direction.DOWN);
        bluePortal("block/blue_portal_ns", 0, 0, 6, 16, 16, 10, Direction.NORTH, Direction.SOUTH);

        Builder calcite_flower_pot = getBuilder("block/calcite_flower_pot");
        calcite_flower_pot.ao(false);
        calcite_flower_pot.texture("particle", "paradise_lost:block/calcite_flower_pot");
        calcite_flower_pot.texture("flowerpot", "paradise_lost:block/calcite_flower_pot");
        calcite_flower_pot.texture("dirt", "paradise_lost:block/dirt");
        calcite_flower_pot.element().from(5, 0, 5).to(6, 6, 11)
                .face(Direction.DOWN)
                        .uvs(5, 5, 6, 11)
                        .texture("#flowerpot")
                        .cullface(Direction.DOWN)
                        .end()
                .face(Direction.UP)
                        .uvs(5, 5, 6, 11)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.NORTH)
                        .uvs(10, 10, 11, 16)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.SOUTH)
                        .uvs(5, 10, 6, 16)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.WEST)
                        .uvs(5, 10, 11, 16)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.EAST)
                        .uvs(5, 10, 11, 16)
                        .texture("#flowerpot")
                        .end()
                .end();
        calcite_flower_pot.element().from(10, 0, 5).to(11, 6, 11)
                .face(Direction.DOWN)
                        .uvs(10, 5, 11, 11)
                        .texture("#flowerpot")
                        .cullface(Direction.DOWN)
                        .end()
                .face(Direction.UP)
                        .uvs(10, 5, 11, 11)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.NORTH)
                        .uvs(5, 10, 6, 16)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.SOUTH)
                        .uvs(10, 10, 11, 16)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.WEST)
                        .uvs(5, 10, 11, 16)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.EAST)
                        .uvs(5, 10, 11, 16)
                        .texture("#flowerpot")
                        .end()
                .end();
        calcite_flower_pot.element().from(6, 0, 5).to(10, 6, 6)
                .face(Direction.DOWN)
                        .uvs(6, 10, 10, 11)
                        .texture("#flowerpot")
                        .cullface(Direction.DOWN)
                        .end()
                .face(Direction.UP)
                        .uvs(6, 5, 10, 6)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.NORTH)
                        .uvs(6, 10, 10, 16)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.SOUTH)
                        .uvs(6, 10, 10, 16)
                        .texture("#flowerpot")
                        .end()
                .end();
        calcite_flower_pot.element().from(6, 0, 10).to(10, 6, 11)
                .face(Direction.DOWN)
                        .uvs(6, 5, 10, 6)
                        .texture("#flowerpot")
                        .cullface(Direction.DOWN)
                        .end()
                .face(Direction.UP)
                        .uvs(6, 10, 10, 11)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.NORTH)
                        .uvs(6, 10, 10, 16)
                        .texture("#flowerpot")
                        .end()
                .face(Direction.SOUTH)
                        .uvs(6, 10, 10, 16)
                        .texture("#flowerpot")
                        .end()
                .end();
        calcite_flower_pot.element().from(6, 0, 6).to(10, 4, 10)
                .face(Direction.DOWN)
                        .uvs(6, 12, 10, 16)
                        .texture("#flowerpot")
                        .cullface(Direction.DOWN)
                        .end()
                .face(Direction.UP)
                        .uvs(6, 6, 10, 10)
                        .texture("#dirt")
                        .end()
                .end();

        cherineCampfire(true);
        cherineCampfire(false);

        cherineLantern(false);
        cherineLantern(true);

        foodBowl(false);
        foodBowl(true);

        goldenAmberBarsPost();
        goldenAmberBarsPostEnds();
        goldenAmberBarsSide(false);
        goldenAmberBarsSide(true);

        Builder halflight_cheesecake = getBuilder("block/halflight_cheesecake");
        cakeTextures(halflight_cheesecake, false);
        halflight_cheesecake.element().from(1, 0, 1).to(15, 8, 15)
                .face(Direction.DOWN)
                        .texture("#bottom")
                        .cullface(Direction.DOWN)
                        .end()
                .face(Direction.UP)
                        .texture("#top")
                        .end()
                .face(Direction.NORTH)
                        .texture("#side")
                        .end()
                .face(Direction.SOUTH)
                        .texture("#side")
                        .end()
                .face(Direction.WEST)
                        .texture("#side")
                        .end()
                .face(Direction.EAST)
                        .texture("#side")
                        .end()
                .end();

        Builder halflight_cheesecake_slice1 = getBuilder("block/halflight_cheesecake_slice1");
        cakeTextures(halflight_cheesecake_slice1, true);
        cakeRightSlab(halflight_cheesecake_slice1);
        halflight_cheesecake_slice1.element().from(1, 0, 8).to(8, 8, 15)
                .face(Direction.NORTH)
                        .uvs(8, 8, 15, 16)
                        .texture("#inside")
                        .end()
                .face(Direction.SOUTH)
                        .uvs(1, 8, 8, 16)
                        .texture("#side")
                        .end()
                .face(Direction.WEST)
                        .uvs(8, 8, 15, 16)
                        .texture("#side")
                        .end()
                .face(Direction.UP)
                        .uvs(1, 8, 8, 15)
                        .texture("#top")
                        .end()
                .face(Direction.DOWN)
                        .uvs(1, 1, 8, 8)
                        .texture("#bottom")
                        .cullface(Direction.DOWN)
                        .end()
                .end();

        Builder halflight_cheesecake_slice2 = getBuilder("block/halflight_cheesecake_slice2");
        cakeTextures(halflight_cheesecake_slice2, true);
        cakeRightSlab(halflight_cheesecake_slice2);

        Builder halflight_cheesecake_slice3 = getBuilder("block/halflight_cheesecake_slice3");
        cakeTextures(halflight_cheesecake_slice3, true);
        halflight_cheesecake_slice3.element().from(8, 0, 8).to(15, 8, 15)
                .face(Direction.NORTH)
                        .uvs(1, 8, 8, 16)
                        .texture("#inside")
                        .end()
                .face(Direction.EAST)
                        .uvs(1, 8, 8, 16)
                        .texture("#side")
                        .end()
                .face(Direction.SOUTH)
                        .uvs(8, 8, 15, 16)
                        .texture("#side")
                        .end()
                .face(Direction.WEST)
                        .uvs(8, 8, 15, 16)
                        .texture("#inside")
                        .end()
                .face(Direction.UP)
                        .uvs(8, 8, 15, 15)
                        .texture("#top")
                        .end()
                .face(Direction.DOWN)
                        .uvs(8, 1, 15, 8)
                        .texture("#bottom")
                        .cullface(Direction.DOWN)
                        .end()
                .end();

        Builder highlands_grass = getBuilder("block/highlands_grass");
        highlands_grass.parent(getExistingFile(mcLoc("block/block")));
        highlands_grass.texture("particle", blockTex("dirt"));
        highlands_grass.texture("bottom", blockTex("dirt"));
        highlands_grass.texture("top", blockTex("highlands_grass_top"));
        highlands_grass.texture("side", blockTex("highlands_grass_side"));
        highlands_grass.texture("overlay", blockTex("highlands_grass_side_overlay"));
        var grassBase = highlands_grass.element().from(0, 0, 0).to(16, 16, 16);
        grassBase.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").cullface(Direction.DOWN).end();
        grassBase.face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").cullface(Direction.UP).tintindex(0).end();
        for (Direction side : HORIZONTAL) {
            grassBase.face(side).uvs(0, 0, 16, 16).texture("#side").cullface(side).end();
        }
        grassBase.end();
        var grassOverlay = highlands_grass.element().from(0, 0, 0).to(16, 16, 16);
        for (Direction side : HORIZONTAL) {
            grassOverlay.face(side).uvs(0, 0, 16, 16).texture("#overlay").cullface(side).tintindex(0).end();
        }
        grassOverlay.end();

        Builder incubator = getBuilder("block/incubator");
        incubator.texture("0", blockTex("incubator_bottom"));
        incubator.texture("1", blockTex("incubator_side"));
        incubator.texture("2", blockTex("incubator_top"));
        incubator.texture("3", blockTex("incubator_tuft"));
        incubator.texture("particle", blockTex("incubator_top"));
        var incubatorBase = incubator.element().from(0, 0, 0).to(16, 5, 16);
        for (Direction side : NESW) {
            incubatorBase.face(side).uvs(0, 11, 16, 16).texture("#1").end();
        }
        incubatorBase.face(Direction.UP).uvs(0, 0, 16, 16).texture("#2").end();
        incubatorBase.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#0").end();
        incubatorBase.end();
        incubatorTuft(incubator, 10, 4, 1, 17, 4, 15, Direction.Axis.Z, 22.5f, FaceRotation.CLOCKWISE_90, FaceRotation.CLOCKWISE_90);
        incubatorTuft(incubator, 1, 4, 10, 15, 4, 17, Direction.Axis.X, -22.5f, FaceRotation.UPSIDE_DOWN, null);
        incubatorTuft(incubator, -1, 4, 1, 6, 4, 15, Direction.Axis.Z, -22.5f, FaceRotation.COUNTERCLOCKWISE_90, FaceRotation.COUNTERCLOCKWISE_90);
        incubatorTuft(incubator, 1, 4, -1, 15, 4, 6, Direction.Axis.X, 22.5f, null, FaceRotation.UPSIDE_DOWN);
        standardItemDisplays(incubator);

        Builder leaf_pile = getBuilder("block/leaf_pile");
        leaf_pile.parent(getExistingFile(mcLoc("block/thin_block")));
        leaf_pile.texture("particle", "#leaves");
        var leaf = leaf_pile.element().from(0, 0, 0).to(16, 1, 16);
        leaf.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#leaves").cullface(Direction.DOWN).tintindex(0).end();
        leaf.face(Direction.UP).uvs(0, 0, 16, 16).texture("#leaves").tintindex(0).end();
        for (Direction side : HORIZONTAL) {
            leaf.face(side).uvs(0, 15, 16, 16).texture("#leaves").cullface(side).tintindex(0).end();
        }
        leaf.end();

        Builder mottled_aurel_fallen_log = getBuilder("block/mottled_aurel_fallen_log");
        mottled_aurel_fallen_log.ao(false);
        mottled_aurel_fallen_log.texture("0", blockTex("stripped_aurel_log_side"));
        mottled_aurel_fallen_log.texture("1", blockTex("mottled_aurel_log_top_hollow"));
        mottled_aurel_fallen_log.texture("2", blockTex("mottled_aurel_log_side"));
        mottled_aurel_fallen_log.texture("particle", blockTex("stripped_aurel_log_side"));
        var mottledOuter = mottled_aurel_fallen_log.element().from(0, 0, 0).to(16, 16, 16);
        for (Direction side : NESW) {
            mottledOuter.face(side).uvs(0, 0, 16, 16).texture("#2").end();
        }
        mottledOuter.face(Direction.UP).uvs(0, 0, 16, 16).texture("#1").end();
        mottledOuter.face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#1").end();
        mottledOuter.end();
        var mottledInner = mottled_aurel_fallen_log.element().from(14, 16, 14).to(2, 0, 2);
        for (Direction side : NESW) {
            mottledInner.face(side).uvs(2, 1, 14, 15).texture("#0").rotation(FaceRotation.UPSIDE_DOWN).end();
        }
        mottledInner.face(Direction.UP).uvs(2, 2, 14, 14).texture("#1").rotation(FaceRotation.UPSIDE_DOWN).end();
        mottledInner.face(Direction.DOWN).uvs(2, 2, 14, 14).texture("#1").rotation(FaceRotation.UPSIDE_DOWN).end();
        mottledInner.end();
        standardItemDisplays(mottled_aurel_fallen_log);

        Builder olvite_chain = getBuilder("block/olvite_chain");
        olvite_chain.parent(getExistingFile(mcLoc("block/block")));
        olvite_chain.texture("particle", blockTex("olvite_chain"));
        olvite_chain.texture("all", blockTex("olvite_chain"));
        addRotatedBillboard(olvite_chain, 6.5f, 0, 8, 9.5f, 16, 8, Direction.NORTH, Direction.SOUTH, "#all", 0, 0, 3, 16);
        addRotatedBillboard(olvite_chain, 8, 0, 6.5f, 8, 16, 9.5f, Direction.WEST, Direction.EAST, "#all", 3, 0, 6, 16);

        pinkSporecap();
        rootcap();

        Builder tree_tap = getBuilder("block/tree_tap");
        tree_tap.parent(getExistingFile(mcLoc("block/block")));
        tree_tap.texture("0", "paradise_lost:block/tree_tap");
        tree_tap.texture("particle", "paradise_lost:block/tree_tap");
        tree_tap.element().from(10.5f, 10, 6).to(18.5f, 13, 10)
                .rotation().origin(10.5f, 10, 8).axis(Direction.Axis.Z).angle(22.5f)
                .end()
                .face(Direction.NORTH)
                        .uvs(8, 4, 0, 7)
                        .texture("#0")
                        .end()
                .face(Direction.EAST)
                        .uvs(8, 2, 12, 5)
                        .texture("#0")
                        .end()
                .face(Direction.SOUTH)
                        .uvs(0, 4, 8, 7)
                        .texture("#0")
                        .end()
                .face(Direction.WEST)
                        .uvs(8, 2, 12, 5)
                        .texture("#0")
                        .end()
                .face(Direction.UP)
                        .uvs(0, 0, 8, 4)
                        .texture("#0")
                        .end()
                .face(Direction.DOWN)
                        .uvs(0, 0, 8, 4)
                        .texture("#0")
                        .end()
                .end();
        tree_tap.element().from(10.5f, 10.95f, 9.05f).to(18.5f, 13, 6.95f)
                .rotation().origin(10.5f, 10, 11).axis(Direction.Axis.Z).angle(22.5f)
                .end()
                .face(Direction.NORTH)
                        .uvs(0, 4, 8, 6)
                        .texture("#0")
                        .end()
                .face(Direction.SOUTH)
                        .uvs(8, 4, 0, 6)
                        .texture("#0")
                        .end()
                .face(Direction.DOWN)
                        .uvs(8, 0, 16, 2)
                        .texture("#0")
                        .end()
                .end();
        tree_tap.element().from(3, 0, 3).to(13, 2, 13)
                .rotation().origin(5, 0, 3).axis(Direction.Axis.Y).angle(0)
                .end()
                .face(Direction.NORTH)
                        .uvs(6, 6, 16, 8)
                        .texture("#0")
                        .end()
                .face(Direction.EAST)
                        .uvs(6, 6, 16, 8)
                        .texture("#0")
                        .end()
                .face(Direction.SOUTH)
                        .uvs(6, 6, 16, 8)
                        .texture("#0")
                        .end()
                .face(Direction.WEST)
                        .uvs(6, 6, 16, 8)
                        .texture("#0")
                        .end()
                .face(Direction.UP)
                        .uvs(6, 6, 16, 16)
                        .texture("#0")
                        .end()
                .face(Direction.DOWN)
                        .uvs(6, 6, 16, 16)
                        .texture("#0")
                        .end()
                .end();

        Builder wisteria_hanger = getBuilder("block/wisteria_hanger");
        wisteria_hanger.ao(false);
        wisteria_hanger.texture("particle", "#leaves");
        float[][] wisteriaPlanes = {
                {0, 0, 14, 16, 16, 14},
                {14, 0, 0, 14, 16, 16},
                {0, 0, 2, 16, 16, 2},
                {2, 0, 0, 2, 16, 16},
        };
        Direction[][] wisteriaFaces = {
                {Direction.NORTH, Direction.SOUTH},
                {Direction.EAST, Direction.WEST},
                {Direction.NORTH, Direction.SOUTH},
                {Direction.EAST, Direction.WEST},
        };
        for (int i = 0; i < wisteriaPlanes.length; i++) {
            float[] box = wisteriaPlanes[i];
            addTintedPlane(wisteria_hanger, box[0], box[1], box[2], box[3], box[4], box[5], wisteriaFaces[i][0], wisteriaFaces[i][1], "#leaves");
        }

        Builder olvite_spyglass_in_hand = getBuilder("item/olvite_spyglass_in_hand");
        olvite_spyglass_in_hand.guiLight(BlockModel.GuiLight.FRONT);
        olvite_spyglass_in_hand.texture("spyglass", "paradise_lost:item/olvite_spyglass_model");
        var spyEyepiece = olvite_spyglass_in_hand.element().from(7, 8.5f, 7).to(9, 13.5f, 9);
        for (Direction side : NESW) {
            spyEyepiece.face(side).uvs(0, 2, 2, 7).texture("#spyglass").end();
        }
        spyEyepiece.face(Direction.UP).uvs(0, 0, 2, 2).texture("#spyglass").end();
        spyEyepiece.end();
        var spyBarrel = olvite_spyglass_in_hand.element().from(6.9f, 2.4f, 6.9f).to(9.1f, 8.6f, 9.1f);
        for (Direction side : NESW) {
            spyBarrel.face(side).uvs(0, 7, 2, 13).texture("#spyglass").end();
        }
        spyBarrel.face(Direction.UP).uvs(0, 5, 2, 7).texture("#spyglass").end();
        spyBarrel.face(Direction.DOWN).uvs(0, 13, 2, 15).texture("#spyglass").end();
        spyBarrel.end();
        olvite_spyglass_in_hand.display(null, "thirdperson_righthand", new float[]{0, 0, 0, 0}, new float[]{0, -2, 0}, new float[]{1, 1, 1});
        olvite_spyglass_in_hand.display(null, "ground", new float[]{90, 0, 0}, new float[]{0, 0, 0}, new float[]{1, 1, 1});
        olvite_spyglass_in_hand.display(null, "gui", new float[]{-67.5f, 0, 45}, new float[]{0, 0, 0}, new float[]{1.5f, 1.5f, 1.5f});
        olvite_spyglass_in_hand.display(null, "head", new float[]{90, 0, 0}, new float[]{0, 0, -16}, new float[]{1.6f, 1.6f, 1.6f});
        olvite_spyglass_in_hand.display(null, "fixed", new float[]{0, 0, 0, 0}, new float[]{0, 0, -1.5f}, new float[]{1.5f, 1.5f, 1.5f});

        Builder portal = getBuilder("item/portal");
        portal.parent(getExistingFile(mcLoc("block/cube_all")));
        portal.ao(false);
        portal.texture("0", blockTex("bloomed_calcite"));
        portal.texture("1", blockTex("blue_portal"));
        portal.texture("particle", blockTex("bloomed_calcite"));
        // Frame cubes in original element order
        float[][] portalFrame = {
                {2, 0, 6, 6, 4, 10},
                {2, 12, 6, 6, 16, 10},
                {2, 4, 6, 6, 8, 10},
                {2, 8, 6, 6, 12, 10},
                {6, 0, 6, 10, 4, 10},
                {6, 12, 6, 10, 16, 10},
                {10, 0, 6, 14, 4, 10},
                {10, 12, 6, 14, 16, 10},
                {10, 4, 6, 14, 8, 10},
                {10, 8, 6, 14, 12, 10},
        };
        for (float[] box : portalFrame) {
            addFullCube(portal, box[0], box[1], box[2], box[3], box[4], box[5], "#0");
        }
        float[][] portalPanes = {
                {6, 4, 7, 10, 8, 9},
                {6, 8, 7, 10, 12, 9},
        };
        for (float[] box : portalPanes) {
            addNsFaces(portal, box[0], box[1], box[2], box[3], box[4], box[5], "#1");
        }

        Builder bilayer_cross = getBuilder("template/bilayer_cross");
        bilayer_cross.ao(false);
        bilayer_cross.texture("particle", "#cross_tint");
        addCrossLayer(bilayer_cross, "#cross_tint", true);
        addCrossLayer(bilayer_cross, "#cross", false);

        Builder bilayer_stellated = getBuilder("template/bilayer_stellated");
        bilayer_stellated.ao(false);
        bilayer_stellated.texture("particle", "#face_tint");
        addStellatedPlanes(bilayer_stellated, "#face_tint", true);
        addStellatedPlanes(bilayer_stellated, "#face", false);

        Builder generic_groundcover = getBuilder("template/generic_groundcover");
        generic_groundcover.texture("0", "#top_lower");
        generic_groundcover.texture("1", "#stalk");
        generic_groundcover.texture("2", "#top_upper");
        generic_groundcover.texture("3", "#stalk_inner");
        generic_groundcover.texture("particle", "#top_lower");
        tintedCuboid(generic_groundcover, 0, 2, 0, 16, 2, 16, "#0", 0, 0, 16, 16, 0, 0, 16, 16, 0, 0, 16, 16, null, null, null);
        tintedCuboid(generic_groundcover, 0, 3, 0, 16, 3, 16, "#2", 0, 0, 8, 0, 0, 0, 16, 16, 0, 0, 16, 16, FaceRotation.CLOCKWISE_90, null, null);
        tintedCuboid(generic_groundcover, 1, 0, 1, 15, 3, 15, "#1", 1, 0, 15, 3, 1, 13, 15, 16, 1, 13, 15, 16, null, null, null);
        // Inverted stalk: unique DOWN UVs
        var invertedStalk = generic_groundcover.element().from(15, 3, 15).to(1, 0, 1);
        for (Direction side : NESW) {
            invertedStalk.face(side).uvs(1, 0, 15, 3).texture("#1").tintindex(0).rotation(FaceRotation.UPSIDE_DOWN).end();
        }
        invertedStalk.face(Direction.UP).uvs(1, 13, 15, 16).texture("#1").tintindex(0).end();
        invertedStalk.face(Direction.DOWN).uvs(0, 9, 7, 16).texture("#1").tintindex(0).end();
        invertedStalk.end();
        tintedCuboid(generic_groundcover, 3, 0, 3, 13, 3, 13, "#3", 2, 0, 12, 3, 1, 13, 11, 16, 1, 13, 11, 16, null, null, null);
        standardBlockDisplays(generic_groundcover);

        Builder stellated = getBuilder("template/stellated");
        stellated.ao(false);
        stellated.texture("particle", "#face");
        addStellatedPlanes(stellated, "#face", false);

        Builder stellated_tint = getBuilder("template/stellated_tint");
        stellated_tint.ao(false);
        stellated_tint.texture("particle", "#face");
        addStellatedPlanes(stellated_tint, "#face", true);

        registerSimpleModels();
    }




    private static final Direction[] HORIZONTAL = {
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    private static final Direction[] NESW = {
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
    };


    private void incubatorTuft(Builder incubator, float x1, float y1, float z1, float x2, float y2, float z2,
                               Direction.Axis axis, float angle, FaceRotation upRot, FaceRotation downRot) {
        boolean zAxis = axis == Direction.Axis.Z;
        float thin = 3.5f;
        float thick = 7.0f;
        float ns = zAxis ? thin : thick;
        float ew = zAxis ? thick : thin;
        var element = incubator.element().from(x1, y1, z1).to(x2, y2, z2)
                .rotation().origin(8, 5, 8).axis(axis).angle(angle).end();
        element.face(Direction.NORTH).uvs(0, 0, ns, 0).texture("#3").end();
        element.face(Direction.EAST).uvs(0, 0, ew, 0).texture("#3").end();
        element.face(Direction.SOUTH).uvs(0, 0, ns, 0).texture("#3").end();
        element.face(Direction.WEST).uvs(0, 0, ew, 0).texture("#3").end();
        var up = element.face(Direction.UP).uvs(1, 9, 15, 16).texture("#3");
        if (upRot != null) {
            up.rotation(upRot);
        }
        up.end();
        var down = element.face(Direction.DOWN).uvs(1, 9, 15, 16).texture("#3");
        if (downRot != null) {
            down.rotation(downRot);
        }
        down.end();
        element.end();
    }

    private void tintedCuboid(Builder model,
                              float x1, float y1, float z1, float x2, float y2, float z2,
                              String texture,
                              float sideU0, float sideV0, float sideU1, float sideV1,
                              float upU0, float upV0, float upU1, float upV1,
                              float downU0, float downV0, float downU1, float downV1,
                              FaceRotation upRot, FaceRotation downRot, FaceRotation sideRot) {
        var element = model.element().from(x1, y1, z1).to(x2, y2, z2);
        for (Direction side : NESW) {
            var face = element.face(side).uvs(sideU0, sideV0, sideU1, sideV1).texture(texture).tintindex(0);
            if (sideRot != null) {
                face.rotation(sideRot);
            }
            face.end();
        }
        var up = element.face(Direction.UP).uvs(upU0, upV0, upU1, upV1).texture(texture).tintindex(0);
        if (upRot != null) {
            up.rotation(upRot);
        }
        up.end();
        var down = element.face(Direction.DOWN).uvs(downU0, downV0, downU1, downV1).texture(texture).tintindex(0);
        if (downRot != null) {
            down.rotation(downRot);
        }
        down.end();
        element.end();
    }

    private void cherineLantern(boolean hanging) {
        Builder lantern = getBuilder(hanging ? "block/cherine_lantern_hanging" : "block/cherine_lantern");
        lantern.parent(getExistingFile(mcLoc("block/block")));
        lantern.texture("particle", blockTex("cherine_lantern"));
        lantern.texture("lantern", blockTex("cherine_lantern"));
        if (hanging) {
            lantern.element().from(5, 2, 5).to(11, 9, 11)
                    .face(Direction.NORTH).uvs(0, 2, 6, 9).texture("#lantern").end()
                    .face(Direction.EAST).uvs(0, 2, 6, 9).texture("#lantern").end()
                    .face(Direction.SOUTH).uvs(0, 2, 6, 9).texture("#lantern").end()
                    .face(Direction.WEST).uvs(0, 2, 6, 9).texture("#lantern").end()
                    .end();
            lantern.element().from(6, 8, 6).to(10, 10, 10)
                    .face(Direction.NORTH).uvs(1, 0, 5, 2).texture("#lantern").end()
                    .face(Direction.EAST).uvs(1, 0, 5, 2).texture("#lantern").end()
                    .face(Direction.SOUTH).uvs(1, 0, 5, 2).texture("#lantern").end()
                    .face(Direction.WEST).uvs(1, 0, 5, 2).texture("#lantern").end()
                    .face(Direction.UP).uvs(1, 10, 5, 14).texture("#lantern").end()
                    .face(Direction.DOWN).uvs(1, 10, 5, 14).texture("#lantern").end()
                    .end();
            lantern.element().from(6.5f, 11, 8).to(9.5f, 15, 8)
                    .shade(false)
                    .rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).end()
                    .face(Direction.NORTH).uvs(8, 1, 11, 5).texture("#lantern").end()
                    .face(Direction.SOUTH).uvs(8, 1, 11, 5).texture("#lantern").end()
                    .end();
            lantern.element().from(8, 10, 6.5f).to(8, 16, 9.5f)
                    .shade(false)
                    .rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).end()
                    .face(Direction.EAST).uvs(12, 1, 15, 7).texture("#lantern").end()
                    .face(Direction.WEST).uvs(12, 1, 15, 7).texture("#lantern").end()
                    .end();
            lanternRim(lantern, 7);
            lanternRim(lantern, 1);
        } else {
            lantern.element().from(5, 1, 5).to(11, 7, 11)
                    .face(Direction.NORTH).uvs(0, 3, 6, 9).texture("#lantern").end()
                    .face(Direction.EAST).uvs(0, 3, 6, 9).texture("#lantern").end()
                    .face(Direction.SOUTH).uvs(0, 3, 6, 9).texture("#lantern").end()
                    .face(Direction.WEST).uvs(0, 3, 6, 9).texture("#lantern").end()
                    .face(Direction.UP).uvs(0, 9, 6, 15).texture("#lantern").end()
                    .face(Direction.DOWN).uvs(0, 9, 6, 15).texture("#lantern").cullface(Direction.DOWN).end()
                    .end();
            lantern.element().from(6, 8, 6).to(10, 9, 10)
                    .face(Direction.NORTH).uvs(1, 0, 5, 1).texture("#lantern").end()
                    .face(Direction.EAST).uvs(1, 0, 5, 1).texture("#lantern").end()
                    .face(Direction.SOUTH).uvs(1, 0, 5, 1).texture("#lantern").end()
                    .face(Direction.WEST).uvs(1, 0, 5, 1).texture("#lantern").end()
                    .face(Direction.UP).uvs(1, 10, 5, 14).texture("#lantern").end()
                    .end();
            lantern.element().from(6.5f, 9, 8).to(9.5f, 11, 8)
                    .shade(false)
                    .rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).end()
                    .face(Direction.NORTH).uvs(8, 1, 11, 3).texture("#lantern").end()
                    .face(Direction.SOUTH).uvs(8, 1, 11, 3).texture("#lantern").end()
                    .end();
            lantern.element().from(8, 9, 6.5f).to(8, 11, 9.5f)
                    .shade(false)
                    .rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).end()
                    .face(Direction.EAST).uvs(8, 1, 11, 3).texture("#lantern").end()
                    .face(Direction.WEST).uvs(8, 1, 11, 3).texture("#lantern").end()
                    .end();
            lanternRim(lantern, 6);
            lanternRim(lantern, 0);
        }
    }

    private void lanternRim(Builder lantern, float y) {
        lantern.element().from(4, y, 4).to(12, y + 2, 12)
                .face(Direction.NORTH).uvs(0, 14, 8, 16).texture("#lantern").end()
                .face(Direction.EAST).uvs(0, 14, 8, 16).texture("#lantern").end()
                .face(Direction.SOUTH).uvs(0, 14, 8, 16).texture("#lantern").end()
                .face(Direction.WEST).uvs(0, 14, 8, 16).texture("#lantern").end()
                .face(Direction.UP).uvs(8, 7, 16, 15).texture("#lantern").end()
                .face(Direction.DOWN).uvs(8, 7, 16, 15).texture("#lantern").end()
                .end();
    }

    private void goldenAmberBarsPost() {
        Builder post = getBuilder("block/golden_amber_bars_post");
        post.ao(false);
        post.texture("particle", blockTex("golden_amber_bars"));
        post.texture("bars", blockTex("golden_amber_bars"));
        post.element().from(8, 0, 6).to(8, 16, 10)
                .rotation().origin(0, 0, -1).axis(Direction.Axis.Y).angle(0).end()
                .face(Direction.EAST).uvs(6, 0, 2, 16).texture("#bars").end()
                .face(Direction.WEST).uvs(10, 0, 14, 16).texture("#bars").end()
                .end();
        post.element().from(6, 0, 8).to(10, 16, 8)
                .face(Direction.NORTH).uvs(10, 0, 14, 16).texture("#bars").end()
                .face(Direction.SOUTH).uvs(6, 0, 2, 16).texture("#bars").end()
                .end();
    }

    private void goldenAmberBarsPostEnds() {
        Builder ends = getBuilder("block/golden_amber_bars_post_ends");
        ends.ao(false);
        ends.texture("particle", blockTex("golden_amber_bars"));
        ends.texture("edge", blockTex("golden_amber_bars"));
        for (float y : new float[]{0.001f, 15.999f}) {
            ends.element().from(6, y, 6).to(10, y, 10)
                    .face(Direction.UP).uvs(2, 8, 6, 12).texture("#edge").end()
                    .face(Direction.DOWN).uvs(2, 8, 6, 12).texture("#edge").end()
                    .end();
        }
    }

    private void goldenAmberBarsSide(boolean alt) {
        Builder side = getBuilder(alt ? "block/golden_amber_bars_side_alt" : "block/golden_amber_bars_side");
        side.ao(false);
        side.texture("particle", blockTex("golden_amber_bars"));
        side.texture("edge", blockTex("golden_amber_bars"));
        if (alt) {
            side.element().from(8, 0, 8).to(8, 16, 16)
                    .face(Direction.EAST).uvs(0, 0, 8, 16).texture("#edge").end()
                    .face(Direction.WEST).uvs(8, 0, 0, 16).texture("#edge").end()
                    .end();
            side.element().from(7, 0, 9).to(9, 16, 16)
                    .face(Direction.SOUTH).uvs(7, 0, 9, 16).texture("#edge").cullface(Direction.SOUTH).end()
                    .end();
            for (float y : new float[]{0.001f, 15.999f}) {
                side.element().from(7, y, 9).to(9, y, 16)
                        .face(Direction.UP).uvs(8, 0, 15, 2).texture("#edge").rotation(FaceRotation.COUNTERCLOCKWISE_90).end()
                        .face(Direction.DOWN).uvs(8, 2, 15, 0).texture("#edge").rotation(FaceRotation.CLOCKWISE_90).end()
                        .end();
            }
        } else {
            side.element().from(8, 0, 0).to(8, 16, 8)
                    .face(Direction.EAST).uvs(8, 0, 16, 16).texture("#edge").end()
                    .face(Direction.WEST).uvs(16, 0, 8, 16).texture("#edge").end()
                    .end();
            side.element().from(7, 0, 0).to(9, 16, 7)
                    .face(Direction.NORTH).uvs(7, 0, 9, 16).texture("#edge").cullface(Direction.NORTH).end()
                    .end();
            for (float y : new float[]{0.001f, 15.999f}) {
                side.element().from(7, y, 0).to(9, y, 7)
                        .face(Direction.UP).uvs(9, 0, 16, 2).texture("#edge").rotation(FaceRotation.COUNTERCLOCKWISE_90).end()
                        .face(Direction.DOWN).uvs(9, 2, 16, 0).texture("#edge").rotation(FaceRotation.CLOCKWISE_90).end()
                        .end();
            }
        }
    }

    private void pinkSporecap() {
        Builder sporecap = getBuilder("block/pink_sporecap");
        sporecap.texture("0", blockTex("pink_sporecap"));
        sporecap.texture("1", blockTex("pink_sporecap_small"));
        sporecap.texture("particle", blockTex("pink_sporecap"));
        sporecapPlane(sporecap, true, "#0", true, false);
        sporecapPlane(sporecap, false, "#0", true, true);
        sporecapPlane(sporecap, false, "#1", false, true);
        sporecapPlane(sporecap, true, "#1", false, false);
    }

    private void sporecapPlane(Builder model, boolean xThin, String texture, boolean angled, boolean rotateEnds) {
        var element = xThin
                ? model.element().from(8, 0, 0).to(8, 16, 16)
                : model.element().from(0, 0, 8).to(16, 16, 8);
        if (angled) {
            element.rotation().origin(8, 0, 8).axis(Direction.Axis.Y).angle(-45).end();
        }
        if (xThin) {
            element.face(Direction.NORTH).uvs(0, 0, 0, 16).texture(texture).end();
            element.face(Direction.EAST).uvs(0, 0, 16, 16).texture(texture).end();
            element.face(Direction.SOUTH).uvs(0, 0, 0, 16).texture(texture).end();
            element.face(Direction.WEST).uvs(0, 0, 16, 16).texture(texture).end();
            element.face(Direction.UP).uvs(0, 0, 0, 16).texture(texture).end();
            element.face(Direction.DOWN).uvs(0, 0, 0, 16).texture(texture).end();
        } else {
            element.face(Direction.NORTH).uvs(0, 0, 16, 16).texture(texture).end();
            element.face(Direction.EAST).uvs(0, 0, 0, 16).texture(texture).end();
            element.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture(texture).end();
            element.face(Direction.WEST).uvs(0, 0, 0, 16).texture(texture).end();
            var up = element.face(Direction.UP).uvs(0, 0, 0, 16).texture(texture);
            if (rotateEnds) {
                up.rotation(FaceRotation.CLOCKWISE_90);
            }
            up.end();
            var down = element.face(Direction.DOWN).uvs(0, 0, 0, 16).texture(texture);
            if (rotateEnds) {
                down.rotation(FaceRotation.COUNTERCLOCKWISE_90);
            }
            down.end();
        }
        element.end();
    }

    private void rootcap() {
        Builder rootcap = getBuilder("block/rootcap");
        rootcap.ao(false);
        rootcap.texture("1", blockTex("rootcap1"));
        rootcap.texture("2", blockTex("rootcap2"));
        rootcap.texture("3", blockTex("rootcap0"));
        rootcap.texture("particle", blockTex("rootcap0"));
        // y, angle, rescale, texture, flipUD, sideFaces
        Object[][] layers = {
                {3f, -22.5f, false, "#2", false, true},
                {5f, -22.5f, true, "#1", false, true},
                {8f, -22.5f, true, "#3", true, false},
                {13f, 22.5f, false, "#2", true, true},
                {11f, 22.5f, true, "#1", true, true},
                {8f, 22.5f, true, "#3", false, false},
        };
        for (Object[] layer : layers) {
            float y = (Float) layer[0];
            float angle = (Float) layer[1];
            boolean rescale = (Boolean) layer[2];
            String texture = (String) layer[3];
            boolean flip = (Boolean) layer[4];
            boolean sides = (Boolean) layer[5];
            var element = rootcap.element().from(0, y, 0).to(16, y, 16).shade(false);
            var rotation = element.rotation().origin(8, y, 14).axis(Direction.Axis.X).angle(angle);
            if (rescale) {
                rotation.rescale(true);
            }
            rotation.end();
            if (sides) {
                for (Direction side : NESW) {
                    element.face(side).uvs(0, 0, 0, 0).texture(texture).end();
                }
            }
            if (flip) {
                element.face(Direction.UP).uvs(16, 0, 0, 16).texture(texture).end();
                element.face(Direction.DOWN).uvs(16, 16, 0, 0).texture(texture).end();
            } else {
                element.face(Direction.UP).uvs(0, 0, 16, 16).texture(texture).end();
                element.face(Direction.DOWN).uvs(0, 16, 16, 0).texture(texture).end();
            }
            element.end();
        }
    }

    private void addRotatedBillboard(Builder model, float x1, float y1, float z1, float x2, float y2, float z2,
                                      Direction faceA, Direction faceB, String texture,
                                      float u0, float v0, float u1, float v1) {
        model.element().from(x1, y1, z1).to(x2, y2, z2)
                .shade(false)
                .rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).end()
                .face(faceA).uvs(u0, v0, u1, v1).texture(texture).end()
                .face(faceB).uvs(u0, v0, u1, v1).texture(texture).end()
                .end();
    }

    private void cherineCampfire(boolean lit) {
        Builder campfire = getBuilder(lit ? "block/cherine_campfire" : "block/cherine_campfire_off");
        campfire.parent(getExistingFile(mcLoc("block/block")));
        campfire.texture("particle", blockTex("cherine_campfire_log"));
        campfire.texture("log", blockTex("cherine_campfire_log"));
        if (lit) {
            campfire.texture("fire", blockTex("cherine_campfire_fire"));
            campfire.texture("lit_log", blockTex("cherine_campfire_log_lit"));
        }
        String hot = lit ? "#lit_log" : "#log";
        float crossDownV0 = lit ? 4 : 0;
        float crossDownV1 = lit ? 8 : 4;

        campfire.element().from(1, 0, 0).to(5, 4, 16)
                .face(Direction.NORTH).uvs(0, 4, 4, 8).texture("#log").cullface(Direction.NORTH).end()
                .face(Direction.EAST).uvs(0, 1, 16, 5).texture(hot).end()
                .face(Direction.SOUTH).uvs(0, 4, 4, 8).texture("#log").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).uvs(16, 0, 0, 4).texture("#log").end()
                .face(Direction.UP).uvs(0, 0, 16, 4).texture("#log").rotation(FaceRotation.CLOCKWISE_90).end()
                .face(Direction.DOWN).uvs(0, 0, 16, 4).texture("#log").cullface(Direction.DOWN).rotation(FaceRotation.CLOCKWISE_90).end()
                .end();
        campfire.element().from(0, 3, 11).to(16, 7, 15)
                .face(Direction.NORTH).uvs(16, 0, 0, 4).texture(hot).end()
                .face(Direction.EAST).uvs(0, 4, 4, 8).texture("#log").cullface(Direction.EAST).end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 4).texture(hot).end()
                .face(Direction.WEST).uvs(0, 4, 4, 8).texture("#log").cullface(Direction.WEST).end()
                .face(Direction.UP).uvs(0, 0, 16, 4).texture("#log").rotation(FaceRotation.UPSIDE_DOWN).end()
                .face(Direction.DOWN).uvs(0, crossDownV0, 16, crossDownV1).texture(hot).end()
                .end();
        campfire.element().from(11, 0, 0).to(15, 4, 16)
                .face(Direction.NORTH).uvs(0, 4, 4, 8).texture("#log").cullface(Direction.NORTH).end()
                .face(Direction.EAST).uvs(0, 0, 16, 4).texture("#log").end()
                .face(Direction.SOUTH).uvs(0, 4, 4, 8).texture("#log").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).uvs(16, 1, 0, 5).texture(hot).end()
                .face(Direction.UP).uvs(0, 0, 16, 4).texture("#log").rotation(FaceRotation.CLOCKWISE_90).end()
                .face(Direction.DOWN).uvs(0, 0, 16, 4).texture("#log").cullface(Direction.DOWN).rotation(FaceRotation.CLOCKWISE_90).end()
                .end();
        campfire.element().from(0, 3, 1).to(16, 7, 5)
                .face(Direction.NORTH).uvs(0, 0, 16, 4).texture(hot).end()
                .face(Direction.EAST).uvs(0, 4, 4, 8).texture("#log").cullface(Direction.EAST).end()
                .face(Direction.SOUTH).uvs(16, 0, 0, 4).texture(hot).end()
                .face(Direction.WEST).uvs(0, 4, 4, 8).texture("#log").cullface(Direction.WEST).end()
                .face(Direction.UP).uvs(0, 0, 16, 4).texture("#log").rotation(FaceRotation.UPSIDE_DOWN).end()
                .face(Direction.DOWN).uvs(0, crossDownV0, 16, crossDownV1).texture(hot).end()
                .end();
        campfire.element().from(5, 0, 0).to(11, 1, 16)
                .face(Direction.NORTH).uvs(0, 15, 6, 16).texture("#log").cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).uvs(10, 15, 16, 16).texture("#log").cullface(Direction.SOUTH).end()
                .face(Direction.UP).uvs(0, 8, 16, 14).texture(hot).rotation(FaceRotation.CLOCKWISE_90).end()
                .face(Direction.DOWN).uvs(0, 8, 16, 14).texture("#log").cullface(Direction.DOWN).rotation(FaceRotation.CLOCKWISE_90).end()
                .end();
        if (lit) {
            campfire.element().from(0.8f, 1, 8).to(15.2f, 17, 8)
                    .shade(false)
                    .rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end()
                    .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#fire").end()
                    .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#fire").end()
                    .end();
            campfire.element().from(8, 1, 0.8f).to(8, 17, 15.2f)
                    .shade(false)
                    .rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end()
                    .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#fire").end()
                    .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#fire").end()
                    .end();
            // Preserve original 4-component rotation array on lit campfire
            campfire.display(null, "head", new float[]{0, 0, 0, 0}, new float[]{0, 10.5f, 0}, new float[]{1, 1, 1});
        } else {
            campfire.display(null, "head", new float[]{0, 0, 0}, new float[]{0, 10.5f, 0}, new float[]{1, 1, 1});
        }
    }

    private void foodBowl(boolean full) {
        Builder bowl = getBuilder(full ? "block/food_bowl_full" : "block/food_bowl");
        bowl.texture("0", blockTex("food_bowl_bottom"));
        bowl.texture("1", blockTex(full ? "food_bowl_full" : "food_bowl_empty"));
        bowl.texture("4", blockTex("food_bowl_top"));
        String side = full ? "#5" : "#3";
        if (full) {
            bowl.texture("5", blockTex("food_bowl_side"));
        } else {
            bowl.texture("3", blockTex("food_bowl_side"));
        }
        bowl.texture("particle", blockTex("food_bowl_side"));
        float innerY = full ? 7 : 3;
        float nsV1 = full ? 1 : 5;
        float ewV1 = full ? 9 : 13;
        bowl.element().from(0, 0, 1).to(16, 8, 15)
                .face(Direction.NORTH).uvs(0, 0, 16, 8).texture(side).end()
                .face(Direction.EAST).uvs(1, 8, 15, 16).texture(side).end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 8).texture(side).end()
                .face(Direction.WEST).uvs(1, 8, 15, 16).texture(side).end()
                .face(Direction.UP).uvs(16, 15, 0, 1).texture("#4").end()
                .face(Direction.DOWN).uvs(16, 1, 0, 15).texture("#0").end()
                .end();
        bowl.element().from(15.1f, 8, 14.1f).to(0.9f, innerY, 1.9f)
                .face(Direction.NORTH).uvs(2, 0, 16, nsV1).texture(side).rotation(FaceRotation.UPSIDE_DOWN).end()
                .face(Direction.EAST).uvs(2, 8, 14, ewV1).texture(side).rotation(FaceRotation.UPSIDE_DOWN).end()
                .face(Direction.SOUTH).uvs(2, 0, 16, nsV1).texture(side).rotation(FaceRotation.UPSIDE_DOWN).end()
                .face(Direction.WEST).uvs(2, 8, 14, ewV1).texture(side).rotation(FaceRotation.UPSIDE_DOWN).end()
                .face(Direction.UP).uvs(1, 2, 15, 14).texture("#1").end()
                .end();
        standardBlockDisplays(bowl);
    }

    private void standardItemDisplays(Builder model) {
        model.display(null, "thirdperson_righthand", new float[]{75, 45, 0}, new float[]{0, 2.5f, 0}, new float[]{0.375f, 0.375f, 0.375f});
        model.display(null, "thirdperson_lefthand", new float[]{75, 45, 0}, new float[]{0, 2.5f, 0}, new float[]{0.375f, 0.375f, 0.375f});
        model.display(null, "firstperson_righthand", new float[]{0, 45, 0}, new float[]{0, 0, 0}, new float[]{0.4f, 0.4f, 0.4f});
        model.display(null, "firstperson_lefthand", new float[]{0, 225, 0}, new float[]{0, 0, 0}, new float[]{0.4f, 0.4f, 0.4f});
        model.display(null, "ground", new float[]{0, 0, 0, 0}, new float[]{0, 3, 0}, new float[]{0.25f, 0.25f, 0.25f});
        model.display(null, "gui", new float[]{30, 225, 0}, new float[]{0, 0, 0}, new float[]{0.625f, 0.625f, 0.625f});
        model.display(null, "fixed", new float[]{0, 0, 0, 0}, new float[]{0, 0, 0}, new float[]{0.5f, 0.5f, 0.5f});
    }

    private void standardBlockDisplays(Builder model) {
        standardItemDisplays(model);
        model.display(null, "head", new float[]{0, 180, 0}, new float[]{0, 13, 7}, new float[]{1, 1, 1});
    }

    private void cakeTextures(Builder cake, boolean withInside) {
        cake.texture("particle", blockTex("halflight_cheesecake_side"));
        cake.texture("bottom", blockTex("halflight_cheesecake_bottom"));
        cake.texture("top", blockTex("halflight_cheesecake_top"));
        cake.texture("side", blockTex("halflight_cheesecake_side"));
        if (withInside) {
            cake.texture("inside", blockTex("halflight_cheesecake_inner"));
        }
    }

    private void cakeRightSlab(Builder cake) {
        cake.element().from(8, 0, 1).to(15, 8, 15)
                .face(Direction.NORTH).uvs(1, 8, 8, 16).texture("#side").end()
                .face(Direction.EAST).uvs(1, 8, 15, 16).texture("#side").end()
                .face(Direction.SOUTH).uvs(8, 8, 15, 16).texture("#side").end()
                .face(Direction.WEST).uvs(1, 8, 15, 16).texture("#inside").end()
                .face(Direction.UP).uvs(8, 1, 15, 15).texture("#top").end()
                .face(Direction.DOWN).uvs(8, 1, 15, 15).texture("#bottom").cullface(Direction.DOWN).end()
                .end();
    }

    private static final Direction[] CUBE_FACES = {
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN
    };

    private void addFullCube(Builder model, float x1, float y1, float z1, float x2, float y2, float z2, String texture) {
        var element = model.element().from(x1, y1, z1).to(x2, y2, z2)
                .rotation().origin(x1 + 8, y1 + 8, z1 + 8).axis(Direction.Axis.Y).angle(0).end();
        for (Direction direction : CUBE_FACES) {
            element.face(direction).uvs(0, 0, 16, 16).texture(texture).end();
        }
        element.end();
    }

    private void addNsFaces(Builder model, float x1, float y1, float z1, float x2, float y2, float z2, String texture) {
        var element = model.element().from(x1, y1, z1).to(x2, y2, z2)
                .rotation().origin(x1 + 8, y1 + 8, z1 + 8).axis(Direction.Axis.Y).angle(0).end();
        element.face(Direction.NORTH).uvs(0, 0, 16, 16).texture(texture).end();
        element.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture(texture).end();
        element.end();
    }

    private void addCrossLayer(Builder model, String texture, boolean tint) {
        addRotatedCrossPlane(model, 0.8f, 0, 8, 15.2f, 16, 8, Direction.NORTH, Direction.SOUTH, texture, tint);
        addRotatedCrossPlane(model, 8, 0, 0.8f, 8, 16, 15.2f, Direction.WEST, Direction.EAST, texture, tint);
    }

    private void addRotatedCrossPlane(Builder model, float x1, float y1, float z1, float x2, float y2, float z2,
                                       Direction faceA, Direction faceB, String texture, boolean tint) {
        var element = model.element().from(x1, y1, z1).to(x2, y2, z2)
                .shade(false)
                .rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end();
        var a = element.face(faceA).uvs(0, 0, 16, 16).texture(texture);
        if (tint) {
            a.tintindex(0);
        }
        a.end();
        var b = element.face(faceB).uvs(0, 0, 16, 16).texture(texture);
        if (tint) {
            b.tintindex(0);
        }
        b.end();
        element.end();
    }

    private void addStellatedPlanes(Builder model, String texture, boolean tint) {
        addStellatedPlane(model, 4, 0, 0, 4, 16, 16, Direction.WEST, false, Direction.EAST, true, texture, tint);
        addStellatedPlane(model, 12, 0, 0, 12, 16, 16, Direction.WEST, true, Direction.EAST, false, texture, tint);
        addStellatedPlane(model, 0, 0, 4, 16, 16, 4, Direction.NORTH, false, Direction.SOUTH, true, texture, tint);
        addStellatedPlane(model, 0, 0, 12, 16, 16, 12, Direction.NORTH, true, Direction.SOUTH, false, texture, tint);
    }

    private void addStellatedPlane(Builder model, float x1, float y1, float z1, float x2, float y2, float z2,
                                   Direction faceA, boolean flipA, Direction faceB, boolean flipB,
                                   String texture, boolean tint) {
        var element = model.element().from(x1, y1, z1).to(x2, y2, z2).shade(false);
        var a = element.face(faceA).uvs(flipA ? 16 : 0, 0, flipA ? 0 : 16, 16).texture(texture);
        if (tint) {
            a.tintindex(0);
        }
        a.end();
        var b = element.face(faceB).uvs(flipB ? 16 : 0, 0, flipB ? 0 : 16, 16).texture(texture);
        if (tint) {
            b.tintindex(0);
        }
        b.end();
        element.end();
    }

    private void addTintedPlane(Builder model, float x1, float y1, float z1, float x2, float y2, float z2,
                                Direction faceA, Direction faceB, String texture) {
        var element = model.element().from(x1, y1, z1).to(x2, y2, z2);
        element.face(faceA).uvs(0, 0, 16, 16).texture(texture).tintindex(0).end();
        element.face(faceB).uvs(0, 0, 16, 16).texture(texture).tintindex(0).end();
        element.end();
    }

    private void bluePortal(String name, float x1, float y1, float z1, float x2, float y2, float z2, Direction faceA, Direction faceB) {
        Builder portal = getBuilder(name);
        portal.texture("particle", blockTex("blue_portal"));
        portal.texture("portal", blockTex("blue_portal"));
        var element = portal.element().from(x1, y1, z1).to(x2, y2, z2);
        element.face(faceA).uvs(0, 0, 16, 16).texture("#portal").end();
        element.face(faceB).uvs(0, 0, 16, 16).texture("#portal").end();
        element.end();
    }

    private void registerSimpleModels() {
        ModelFile crop = existingMc("block/crop");
        ModelFile cross = existingMc("block/cross");
        ModelFile tintedCross = existingMc("block/tinted_cross");
        ModelFile cubeAll = existingMc("block/cube_all");
        ModelFile cubeColumn = existingMc("block/cube_column");
        ModelFile cubeBottomTop = existingMc("block/cube_bottom_top");
        ModelFile singleFace = existingMc("block/template_single_face");
        ModelFile dirtPath = existingMc("block/dirt_path");
        ModelFile farmland = existingMc("block/template_farmland");
        ModelFile bilayerCross = existingMod("template/bilayer_cross");
        ModelFile groundcover = existingMod("template/generic_groundcover");

        staged("amadrys_stage", 8, crop, "crop");
        staged("blackcurrant_bush_stage", 4, cross, "cross");
        staged("flax_bottom_", 8, crop, "crop");
        staged("flax_top_", 4, crop, "crop");
        staged("suspicious_dirt_", 4, cubeAll, "all");
        staged("swedroot_stage", 4, cross, "cross");

        parented("brown_sporecap_block_inventory", cubeColumn)
                .texture("end", blockTex("brown_sporecap_block_top"))
                .texture("side", blockTex("brown_sporecap_block_side"));
        parented("brown_sporecap_block_side", singleFace).texture("texture", blockTex("brown_sporecap_block_side"));
        parented("brown_sporecap_block_top", singleFace).texture("texture", blockTex("brown_sporecap_block_top"));

        for (char c = 'a'; c <= 'z'; c++) {
            String name = "burnished_stone_" + c;
            parented(name, cubeColumn)
                    .texture("end", blockTex("burnished_stone"))
                    .texture("side", blockTex(name));
        }

        getBuilder("block/calcite_decorated_pot").texture("particle", "minecraft:block/calcite");
        parented("cherine_torch", existingMc("block/template_torch")).texture("torch", blockTex("cherine_torch"));
        parented("cherine_wall_torch", existingMc("block/template_torch_wall")).texture("torch", blockTex("cherine_torch"));
        parented("farmland", farmland)
                .texture("particle", blockTex("dirt"))
                .texture("dirt", blockTex("dirt"))
                .texture("top", blockTex("farmland"));
        parented("farmland_moist", farmland)
                .texture("particle", blockTex("dirt"))
                .texture("dirt", blockTex("dirt"))
                .texture("top", blockTex("farmland_moist"));
        parented("frozen_grass", cubeBottomTop)
                .texture("top", blockTex("frozen_grass_top"))
                .texture("bottom", blockTex("permafrost"))
                .texture("side", blockTex("frozen_grass_side"));
        parented("frozen_path", dirtPath)
                .texture("particle", blockTex("permafrost"))
                .texture("top", blockTex("frozen_path_top"))
                .texture("side", blockTex("frozen_path_side"))
                .texture("bottom", blockTex("permafrost"));
        parented("grass_flowering", bilayerCross)
                .texture("cross_tint", blockTex("grass"))
                .texture("cross", blockTex("grass_flowers"));
        parented("grass_path", dirtPath)
                .texture("particle", blockTex("dirt"))
                .texture("top", blockTex("grass_path_top"))
                .texture("side", blockTex("grass_path_side"))
                .texture("bottom", blockTex("dirt"));
        parented("grass_snowy", cubeBottomTop)
                .texture("top", blockTex("highlands_grass_top"))
                .texture("bottom", blockTex("dirt"))
                .texture("side", blockTex("grass_side_snowy"))
                .texture("particle", blockTex("dirt"));
        parented("honey_nettle_bottom", tintedCross).texture("cross", blockTex("honey_nettle_bottom"));
        parented("honey_nettle_top", bilayerCross)
                .texture("cross", blockTex("honey_nettle_top_flower"))
                .texture("cross_tint", blockTex("honey_nettle_top_stalk"));
        parented("malt_sprig", groundcover)
                .texture("top_lower", blockTex("malt_sprig"))
                .texture("top_upper", blockTex("blank"))
                .texture("stalk", blockTex("malt_sprig_stalks_small"))
                .texture("stalk_inner", blockTex("malt_sprig_stalks_small_inner"));
        parented("malt_sprig_tall", groundcover)
                .texture("top_lower", blockTex("blank"))
                .texture("top_upper", blockTex("malt_sprig"))
                .texture("stalk", blockTex("malt_sprig_stalks"))
                .texture("stalk_inner", blockTex("malt_sprig_stalks_inner"));
        parented("nest", existingMc("block/coral_fan")).texture("fan", blockTex("thatch_fan"));

        Builder crossCrop = getBuilder("block/cross_crop");
        crossCrop.ao(false);
        crossCrop.variableTexture("particle", "#crop");
        crossCrop.element().from(0.8F, -1.0F, 8.0F).to(15.2F, 15.0F, 8.0F)
                .rotation().origin(8.0F, 8.0F, 8.0F).axis(Direction.Axis.Y).angle(45.0F).rescale(true).end()
                .shade(false)
                .face(Direction.NORTH).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#crop").end()
                .face(Direction.SOUTH).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#crop").end()
                .end();
        crossCrop.element().from(8.0F, -1.0F, 0.8F).to(8.0F, 15.0F, 15.2F)
                .rotation().origin(8.0F, 8.0F, 8.0F).axis(Direction.Axis.Y).angle(45.0F).rescale(true).end()
                .shade(false)
                .face(Direction.WEST).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#crop").end()
                .face(Direction.EAST).uvs(0.0F, 0.0F, 16.0F, 16.0F).texture("#crop").end()
                .end();

        staged("nitra_stage", 4, existingMod("block/cross_crop"), "crop");
        parented("olvite_pressure_plate", existingMc("block/pressure_plate_up")).texture("texture", blockTex("olvite_block"));
        parented("olvite_pressure_plate_down", existingMc("block/pressure_plate_down")).texture("texture", blockTex("olvite_block"));
        getBuilder("block/palace_door").texture("particle", blockTex("golden_amber_tile"));
        parented("pink_sporecap_block", singleFace).texture("texture", blockTex("pink_sporecap_block"));
        parented("pink_sporecap_block_inventory", cubeAll).texture("all", blockTex("pink_sporecap_block"));
        parented("rootcap_block", singleFace).texture("texture", blockTex("rootcap_block"));
        parented("rootcap_block_inventory", cubeAll).texture("all", blockTex("rootcap_block"));
        parented("shamrock", groundcover)
                .texture("top_lower", blockTex("shamrock"))
                .texture("top_upper", blockTex("shamrock"))
                .texture("stalk", blockTex("shamrock_stalks"))
                .texture("stalk_inner", blockTex("shamrock_stalks_inner"));
        parented("tall_grass_bottom", tintedCross).texture("cross", blockTex("tall_grass_bottom"));
        parented("tall_grass_top", tintedCross).texture("cross", blockTex("tall_grass_top"));
        parented("wild_flax_bottom", tintedCross).texture("cross", blockTex("wild_flax_bottom"));
        parented("wild_flax_top", cross).texture("cross", blockTex("wild_flax_top"));

        ModelFile generated = existingMc("item/generated");
        Builder handheldSmall = getBuilder("item/handheld_small").parent(generated);
        handheldSmall.display(null, "thirdperson_righthand", new float[]{0, -90, 55}, new float[]{0, 4, 0.5f}, new float[]{0.725f, 0.725f, 0.725f});
        handheldSmall.display(null, "thirdperson_lefthand", new float[]{0, 90, -55}, new float[]{0, 4, 0.5f}, new float[]{0.725f, 0.725f, 0.725f});
        handheldSmall.display(null, "firstperson_righthand", new float[]{0, -90, 25}, new float[]{1.13f, 3.2f, 1.13f}, new float[]{0.58f, 0.58f, 0.58f});
        handheldSmall.display(null, "firstperson_lefthand", new float[]{0, 90, -25}, new float[]{1.13f, 3.2f, 1.13f}, new float[]{0.58f, 0.68f, 0.58f});

        Builder handheldLarge = getBuilder("template/handheld_large").parent(generated);
        handheldLarge.display(null, "thirdperson_righthand", new float[]{0, -90, 55}, new float[]{0, 12, 0.5f}, new float[]{1.7f, 1.7f, 0.85f});
        handheldLarge.display(null, "thirdperson_lefthand", new float[]{0, 90, -55}, new float[]{0, 12, 0.5f}, new float[]{1.7f, 1.7f, 0.85f});
        handheldLarge.display(null, "firstperson_righthand", new float[]{0, -90, 25}, new float[]{1.13f, 9, 1.13f}, new float[]{1.36f, 1.36f, 0.8f});
        handheldLarge.display(null, "firstperson_lefthand", new float[]{0, 90, -25}, new float[]{1.13f, 9, 1.13f}, new float[]{1.36f, 1.36f, 0.8f});
    }

    private ModelFile existingMc(String path) {
        return getExistingFile(mcLoc(path));
    }

    private ModelFile existingMod(String path) {
        return getExistingFile(modLoc(path));
    }

    private static String blockTex(String name) {
        return ModConstants.MODID + ":block/" + name;
    }

    private Builder parented(String name, ModelFile parent) {
        return getBuilder("block/" + name).parent(parent);
    }

    private void staged(String prefix, int count, ModelFile parent, String textureKey) {
        for (int i = 0; i < count; i++) {
            String name = prefix + i;
            parented(name, parent).texture(textureKey, blockTex(name));
        }
    }

    public static final class Builder extends ModelBuilder<Builder> {
        private JsonObject display;

        private Builder(ResourceLocation location, ExistingFileHelper existingFileHelper) {
            super(location, existingFileHelper);
        }

        public Builder variableTexture(String key, String variable) {
            this.textures.put(key, variable);
            return this;
        }

        public Builder display(String model, String key, float[] rotation, float[] translation, float[] scale) {
            if (this.display == null) {
                this.display = new JsonObject();
            }

            JsonObject transform = new JsonObject();
            if (model != null) {
                transform.addProperty("model", model);
            }

            if (!isIdentity(rotation, 0.0F)) {
                transform.add("rotation", floats(rotation));
            }
            if (!isIdentity(translation, 0.0F)) {
                transform.add("translation", floats(translation));
            }
            if (!isIdentity(scale, 1.0F)) {
                transform.add("scale", floats(scale));
            }
            this.display.add(key, transform);
            return this;
        }

        private static boolean isIdentity(float[] values, float identity) {
            for (float value : values) {
                if (value != identity) {
                    return false;
                }
            }
            return true;
        }

        private static JsonArray floats(float[] values) {
            JsonArray array = new JsonArray();
            for (float value : values) {
                array.add(value);
            }
            return array;
        }

        @Override
        public JsonObject toJson() {
            JsonObject json = super.toJson();
            if (this.display != null) {
                json.add("display", this.display);
            }
            return json;
        }
    }
}
