import os
MOD_ID = "paradise_lost"

def get_asset_path():
    return (os.getcwd()+"/assets/")

def generate_standard_block(block_id):
    blockstate_file = open(get_asset_path()+MOD_ID+"/blockstates/"+block_id+".json", "w")
    block_model_file = open(get_asset_path()+MOD_ID+"/models/block/"+block_id+".json", "w")
    item_model_file = open(get_asset_path()+MOD_ID+"/models/item/"+block_id+".json", "w")
    
    blockstate_file.write("{\n\t\"variants\": {\n\t\t\"\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\"\n\t\t}\n\t}\n}")
    block_model_file.write("{\n\t\"parent\": \"minecraft:block/cube_all\",\n\t\"textures\": {\n\t\t\"all\": \""+MOD_ID+":block/"+block_id+"\"\n\t}\n}")
    item_model_file.write("{\n\t\"parent\": \""+MOD_ID+":block/"+block_id+"\"\n}")
    
    blockstate_file.close()
    block_model_file.close()
    item_model_file.close()
    
def generate_standard_item(item_id):
    item_model_file = open(get_asset_path()+MOD_ID+"/models/item/"+item_id+".json", "w")
    item_model_file.write("{\n\t\"parent\": \"minecraft:item/generated\",\n\t\"textures\": {\n\t\t\"layer0\": \""+MOD_ID+":item/"+item_id+"\"\n\t}\n}")
    item_model_file.close()
        
def generate_handheld_item(item_id):
    item_model_file = open(get_asset_path()+MOD_ID+"/models/item/"+item_id+".json", "w")
    item_model_file.write("{\n\t\"parent\": \"minecraft:item/handheld\",\n\t\"textures\": {\n\t\t\"layer0\": \""+MOD_ID+":item/"+item_id+"\"\n\t}\n}")
    item_model_file.close()
    
def generate_log_block(block_id):
    blockstate_file = open(get_asset_path()+MOD_ID+"/blockstates/"+block_id+".json", "w")
    block_model_file = open(get_asset_path()+MOD_ID+"/models/block/"+block_id+".json", "w")
    horizontal_block_model_file = open(get_asset_path()+MOD_ID+"/models/block/"+block_id+"_horizontal.json", "w")
    item_model_file = open(get_asset_path()+MOD_ID+"/models/item/"+block_id+".json", "w")
    
    blockstate_file.write("{\n\t\"variants\": {\n\t\t\"axis=x\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_horizontal\",\n\t\t\t\"x\": 90,\n\t\t\t\"y\": 90\n\t\t},\n\t\t\"axis=y\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\"\n\t\t},\n\t\t\"axis=z\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_horizontal\",\n\t\t\t\"x\": 90\n\t\t}\n\t}\n}")
    block_model_file.write("{\n\t\"parent\": \"minecraft:block/cube_column\",\n\t\"textures\": {\n\t\t\"end\": \""+MOD_ID+":block/"+block_id+"_top\",\n\t\t\"side\": \""+MOD_ID+":block/"+block_id+"\"\n\t}\n}")
    horizontal_block_model_file.write("{\n\t\"parent\": \"minecraft:block/cube_column_horizontal\",\n\t\"textures\": {\n\t\t\"end\": \""+MOD_ID+":block/"+block_id+"_top\",\n\t\t\"side\":\""+MOD_ID+":block/"+block_id+"\"\n\t}\n}")
    item_model_file.write("{\n\t\"parent\": \""+MOD_ID+":block/"+block_id+"\"\n}")
    
    blockstate_file.close()
    block_model_file.close()
    horizontal_block_model_file.close()
    item_model_file.close()
    
def generate_stairs_block(block_id, texture):
    blockstate_file = open(get_asset_path()+MOD_ID+"/blockstates/"+block_id+".json", "w")
    base_block_model_file = open(get_asset_path()+MOD_ID+"/models/block/"+block_id+".json", "w")
    inner_block_model_file = open(get_asset_path()+MOD_ID+"/models/block/"+block_id+"_inner.json", "w")
    outer_block_model_file = open(get_asset_path()+MOD_ID+"/models/block/"+block_id+"_outer.json", "w")
    item_model_file = open(get_asset_path()+MOD_ID+"/models/item/"+block_id+".json", "w")
    
    blockstate_file.write("{\n\t\"variants\": {\n\t\t\"facing=east,half=bottom,shape=inner_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"y\": 270,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=east,half=bottom,shape=inner_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\"\n\t\t},\n\t\t\"facing=east,half=bottom,shape=outer_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"y\": 270,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=east,half=bottom,shape=outer_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\"\n\t\t},\n\t\t\"facing=east,half=bottom,shape=straight\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\"\n\t\t},\n\t\t\"facing=east,half=top,shape=inner_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"x\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=east,half=top,shape=inner_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 90,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=east,half=top,shape=outer_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"x\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=east,half=top,shape=outer_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 90,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=east,half=top,shape=straight\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\",\n\t\t\t\"x\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=north,half=bottom,shape=inner_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"y\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=north,half=bottom,shape=inner_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"y\": 270,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=north,half=bottom,shape=outer_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"y\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=north,half=bottom,shape=outer_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"y\": 270,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=north,half=bottom,shape=straight\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\",\n\t\t\t\"y\": 270,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=north,half=top,shape=inner_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 270,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=north,half=top,shape=inner_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"x\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=north,half=top,shape=outer_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 270,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=north,half=top,shape=outer_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"x\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=north,half=top,shape=straight\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 270,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=south,half=bottom,shape=inner_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\"\n\t\t},\n\t\t\"facing=south,half=bottom,shape=inner_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"y\": 90,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=south,half=bottom,shape=outer_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\"\n\t\t},\n\t\t\"facing=south,half=bottom,shape=outer_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"y\": 90,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=south,half=bottom,shape=straight\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\",\n\t\t\t\"y\": 90,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=south,half=top,shape=inner_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 90,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=south,half=top,shape=inner_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=south,half=top,shape=outer_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 90,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=south,half=top,shape=outer_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=south,half=top,shape=straight\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 90,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=west,half=bottom,shape=inner_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"y\": 90,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=west,half=bottom,shape=inner_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"y\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=west,half=bottom,shape=outer_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"y\": 90,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=west,half=bottom,shape=outer_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"y\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=west,half=bottom,shape=straight\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\",\n\t\t\t\"y\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=west,half=top,shape=inner_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=west,half=top,shape=inner_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_inner\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 270,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=west,half=top,shape=outer_left\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 180,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=west,half=top,shape=outer_right\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_outer\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 270,\n\t\t\t\"uvlock\": true\n\t\t},\n\t\t\"facing=west,half=top,shape=straight\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\",\n\t\t\t\"x\": 180,\n\t\t\t\"y\": 180,\n\t\t\t\"uvlock\": true\n\t\t}\n\t}\n}")
    base_block_model_file.write("{\n\t\"parent\": \"minecraft:block/stairs\",\n\t\"textures\": {\n\t\t\"bottom\": \""+MOD_ID+":block/"+texture+"\",\n\t\t\"top\": \""+MOD_ID+":block/"+texture+"\",\n\t\t\"side\": \""+MOD_ID+":block/"+texture+"\"\n\t}\n}")
    inner_block_model_file.write("{\n\t\"parent\": \"minecraft:block/inner_stairs\",\n\t\"textures\": {\n\t\t\"bottom\": \""+MOD_ID+":block/"+texture+"\",\n\t\t\"top\": \""+MOD_ID+":block/"+texture+"\",\n\t\t\"side\": \""+MOD_ID+":block/"+texture+"\"\n\t}\n}")
    outer_block_model_file.write("{\n\t\"parent\": \"minecraft:block/outer_stairs\",\n\t\"textures\": {\n\t\t\"bottom\": \""+MOD_ID+":block/"+texture+"\",\n\t\t\"top\": \""+MOD_ID+":block/"+texture+"\",\n\t\t\"side\": \""+MOD_ID+":block/"+texture+"\"\n\t}\n}")
    item_model_file.write("{\n\t\"parent\": \""+MOD_ID+":block/"+block_id+"\"\n}")
    
    blockstate_file.close()
    base_block_model_file.close()
    inner_block_model_file.close()
    outer_block_model_file.close()
    item_model_file.close()
    
def generate_slab_block(block_id, base_block_id, texture):
    blockstate_file = open(get_asset_path()+MOD_ID+"/blockstates/"+block_id+".json", "w")
    bottom_block_model_file = open(get_asset_path()+MOD_ID+"/models/block/"+block_id+".json", "w")
    top_block_model_file = open(get_asset_path()+MOD_ID+"/models/block/"+block_id+"_top.json", "w")
    item_model_file = open(get_asset_path()+MOD_ID+"/models/item/"+block_id+".json", "w")
    
    blockstate_file.write("{\n\t\"variants\": {\n\t\t\"type=bottom\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"\"\n\t\t},\n\t\t\"type=double\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+base_block_id+"\"\n\t\t},\n\t\t\"type=top\": {\n\t\t\t\"model\": \""+MOD_ID+":block/"+block_id+"_top\"\n\t\t}\n\t}\n}")
    bottom_block_model_file.write("{\n\t\"parent\": \"minecraft:block/slab\",\n\t\"textures\": {\n\t\t\"bottom\": \""+MOD_ID+":block/"+texture+"\",\n\t\t\"top\": \""+MOD_ID+":block/"+texture+"\",\n\t\t\"side\": \""+MOD_ID+":block/"+texture+"\"\n\t}\n}")
    top_block_model_file.write("{\n\t\"parent\": \"minecraft:block/slab_top\",\n\t\"textures\": {\n\t\t\"bottom\": \""+MOD_ID+":block/"+texture+"\",\n\t\t\"top\": \""+MOD_ID+":block/"+texture+"\",\n\t\t\"side\": \""+MOD_ID+":block/"+texture+"\"\n\t}\n}")
    item_model_file.write("{\n\t\"parent\": \""+MOD_ID+":block/"+block_id+"\"\n}")
    
    blockstate_file.close()
    bottom_block_model_file.close()
    top_block_model_file.close()
    item_model_file.close()
    


generate_standard_block("heliolith")
generate_stairs_block("heliolith_stairs", "heliolith")
generate_slab_block("heliolith_slab", "heliolith", "heliolith")
generate_standard_block("smooth_heliolith")
generate_stairs_block("smooth_heliolith_stairs", "smooth_heliolith")
generate_slab_block("smooth_heliolith_slab", "smooth_heliolith", "smooth_heliolith")



