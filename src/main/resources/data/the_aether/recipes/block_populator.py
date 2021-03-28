import os

  
def pop_block_set(parent_id, fence_bool, wall_bool, cracked_bool):
    stair_file = open(parent_id + "_stairs.json", "x")
    slab_file = open(parent_id + "_slab.json", "x")
    if fence_bool:
        fence_file = open(parent_id + "_fence.json", "x")
        fence_gate_file = open(parent_id + "_fence_gate.json", "x")
        door_file = open(parent_id + "_door.json", "x")
        trapdoor_file = open(parent_id + "_trapdoor.json", "x")
    if wall_bool:
        wall_file = open(parent_id + "_wall.json", "x")
    if cracked_bool:
        cracked_file = open("cracked_" + parent_id + ".json", "x")
    
    if fence_bool:
        stair_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_stairs",
  "pattern": [
    "#  ",
    "## ",
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+"""_planks"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_stairs",
    "count": 4
  }
}
        """)
        fence_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_fence",
  "pattern": [
    "W#W",
    "W#W"
  ],
  "key": {
    "W": {
      "item": "the_aether:"""+parent_id+"""_planks"
    },
    "#": {
      "item": "minecraft:stick"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_fence",
    "count": 3
  }
}
        """)
        fence_gate_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_fence_gate",
  "pattern": [
    "#W#",
    "#W#"
  ],
  "key": {
    "#": {
      "item": "minecraft:stick"
    },
    "W": {
      "item": "the_aether:"""+parent_id+"""_planks"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_fence_gate"
  }
}
        """)
        door_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_door",
  "pattern": [
    "##",
    "##",
    "##"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+"""_planks"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_door",
    "count": 3
  }
}
        """)
        trapdoor_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_trapdoor",
  "pattern": [
    "###",
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+"""_planks"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_trapdoor",
    "count": 2
  }
}
        """)
    else:
        stair_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "#  ",
    "## ",
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+""""
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_stairs",
    "count": 4
  }
}
        """)
    if fence_bool:
        slab_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_slab",
  "pattern": [
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+"""_planks"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_slab",
    "count": 6
  }
}
        """)
    else:
        slab_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+""""
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_slab",
    "count": 6
  }
}
        """)
    if wall_bool:
        wall_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "###",
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+""""
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_wall",
    "count": 6
  }
}
        """)
    if cracked_bool:
        cracked_file.write(
        """
{
  "type": "minecraft:smelting",
  "ingredient": {
    "item": "the_aether:"""+parent_id+""""
  },
  "result": "the_aether:cracked_"""+parent_id+"""",
  "experience": 0.1,
  "cookingtime": 200
}        
        """)
    
    stair_file.close()
    slab_file.close()
    if fence_bool:
        fence_file.close()
        fence_gate_file.close()
        door_file.close()
        trapdoor_file.close()
    if wall_bool:
        wall_file.close()
    if cracked_bool:
        cracked_file.close()
    
    
while True:
    selection = input("\n\n What do you want to add?\n  0.) Quit\n  1.) Populate a block set\n ")
    if selection == "0":
        break
    elif selection == "1":
        id = input("  What is the parent block's name?\n ").lower()
        fence_bool = (input("  Are there fences, fencegates, and doors/trapdoors (wood set)?\n ").lower() == "y")
        wall_bool = (input("  Are there walls?\n ").lower() == "y")
        cracked_bool = (input("  Are there cracked variants?\n ").lower() == "y")
        pop_block_set(id, fence_bool, wall_bool, cracked_bool)
        os.system('cls')
    else:
        print(" Enter valid selection!")
  

"""
def print_block_commands(block_id, name, logic_bool, model_num):
    print("\n block_world_logic text:\n# "+block_id.upper()+"\nexecute as @e[tag=spawn_"+block_id+"] at @s run function tellurian:blocks/"+block_id+"/place\nexecute as @e[tag="+block_id+"] at @s if block ~ ~ ~ air run function tellurian:blocks/"+block_id+"/break")
    if logic_bool:
        print("execute as @e[tag="+block_id+"] at @s run function tellurian:blocks/"+block_id+"/logic")
    setup_block_files(block_id, name, logic_bool,model_num)

def setup_block_files(block_id, name, logic_bool, model_num):
    block_directory = directory+"//blocks//"+block_id
    give_file = open(directory+"//give_stuff.mcfunction","a")
    os.mkdir(block_directory)
    
    break_file = open(block_directory+"//break.mcfunction", "x")
    break_file.write('execute if block ~ ~ ~ air run particle minecraft:item dolphin_spawn_egg{CustomModelData:'+model_num+'} ~ ~.5 ~ 0.3 0.3 0.3 0.05 40\nexecute if block ~ ~ ~ air unless entity @p[gamemode=creative] run summon item ~ ~ ~ {Motion:[0.0,0.25,0.0],Item:{id:"minecraft:dolphin_spawn_egg",Count:1b,tag:{CustomModelData:'+model_num+',display:{Name:"[{\\"text\\":\\"'+name+'\\",\\"italic\\":\\"false\\"}]"},EntityTag:{id:"minecraft:area_effect_cloud",Tags:["ent_spawn","spawn_'+block_id+'"],Duration:0,Radius:0f,Particle:"minecraft:dust 0 0 0 0"}}}}\nexecute if block ~ ~ ~ air run kill @s')
    break_file.close()
    
    place_file = open(block_directory+"//place.mcfunction", "x")
    place_file.write('execute unless block ~ ~ ~ air unless entity @p[gamemode=creative] run summon item ~ ~ ~ {Motion:[0.0,0.25,0.0],Item:{id:"minecraft:dolphin_spawn_egg",Count:1b,tag:{CustomModelData:'+model_num+',display:{Name:"[{\\"text\\":\\"'+name+'\\",\\"italic\\":\\"false\\"}]"},EntityTag:{id:"minecraft:area_effect_cloud",Tags:["ent_spawn","spawn_'+block_id+'"],Duration:0,Radius:0f,Particle:"minecraft:dust 0 0 0 0"}}}}\nexecute if block ~ ~ ~ air run summon minecraft:item_frame ~ ~ ~ {Tags:["'+block_id+'"],Fixed:1b,Facing:1b,Item:{id:"minecraft:dolphin_spawn_egg", Count:1b, tag:{CustomModelData:'+model_num+'}}}\nexecute if block ~ ~ ~ air run setblock ~ ~ ~ glass')
    place_file.close()
    
    if logic_bool:
        logic_file = open(block_directory+"//logic.mcfunction", "x")
        logic_file.close()  
        
    give_file.write('\ngive @p minecraft:dolphin_spawn_egg{CustomModelData:'+model_num+',display:{Name:"[{\\"text\\":\\"'+name+'\\",\\"italic\\":\\"false\\"}]"},EntityTag:{id:"minecraft:area_effect_cloud",Tags:["ent_spawn","spawn_'+block_id+'"],Duration:0,Radius:0f,Particle:"minecraft:dust 0 0 0 0"}}')
    give_file.close()

selection = ""
while True:
    selection = input("\n\n What do you want to do?\n  0.) Quit\n  1.) Add new block\n ")
    if selection == "0":
        break
    elif selection == "1":
        id = input("  What is the block's id?\n ").lower()
        name = input("  What is the block's actual name?\n ")
        logic = input("  Does the block have logic? (y/n)\n ").lower()
        model_num = input("  What's the block's model value?\n ").lower()
        logic_bool = (logic == "y")
        print_block_commands(id, name, logic_bool,model_num)
    else:
        print(" Enter valid selection!")
"""
